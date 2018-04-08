package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.utilities.Language;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/isbn", produces = "application/json")
public class ISBNController {
    private String[] splitPublisherString(String pubString) {
        String[] output = {null, null};
        String splitted = pubString.substring(
                pubString.split("(, |\\()[0-9]{4}\\)$")[0].length(), pubString.length() - 1);
        if (splitted.charAt(0) == ',') {
            output[1] = splitted.substring(2, splitted.length());
        } else {
            output[1] = splitted.substring(1, splitted.length());
        }
        output[0] = pubString.split(Pattern.quote("("))[0].split(Pattern.quote(";"))[0].trim();
        return output;
    }

    private Map<String, String> getByIsbnAmazon(Map<String, String> map, String isbn) {
        Map<String, String> outputMap = new HashMap<>(map);
        try {
            String amazonPage = "https://www.amazon.com/gp/search/ref=sr_adv_b/?field-isbn=";
            Document document = Jsoup.connect(amazonPage + isbn).get();
            Elements elements = document.select("span[id=s-result-count]");
            if (elements.isEmpty())
                throw new RuntimeException();

            elements = document.select("div[id=atfResults]");
            elements = elements.select("a[href]");
            for (Element link : elements) {
                String hrefElement = link.attr("abs:href");
                if (!hrefElement.contains("-ebook") && hrefElement.contains("/dp/")) {
                    amazonPage = hrefElement.split(Pattern.quote("?"))[0];
                    break;
                }
            }
            document = Jsoup.connect(amazonPage).get();

            // Getting the title
            elements = document.select("span[id=productTitle]");
            outputMap.put("title", Parser.unescapeEntities(elements.first().html(), true));

            // Getting the description
            elements = document.select("noscript");
            for (Element element : elements) {
                if (element.toString().contains("<div>")) {
                    outputMap.put("description", element.select("div").first().html()
                            .replace("\n", "") +
                        "<br><br>" + amazonPage.split("ref=[a-zA-Z]{2}_[0-9]_[0-9]")[0]);
                    elements = null;
                    break;
                }
            }
            if (elements != null)
                outputMap.put("description", "");

            // Getting the author
            elements = document.select("div[id=byline]");
            elements = elements.select(":containsOwn((Author))");
            boolean aSizeFound = false;
            String author = null;
            for (Element element : elements) {
                if (author == null)
                    author = "";
                element = element.parent();
                if (element.className().contains("a-size-")) {
                    author += element.html().split(Pattern.quote("<"))[0].trim() + ", ";
                    aSizeFound = true;
                } else if (!aSizeFound && element.parent().className().contains("author")) {
                    author += element.parent().select("a[href]").first().html() + ", ";
                }
            }
            author = author.substring(0, author.length() - 2);
            outputMap.put("author", author);

            // Getting the publisher & year
            elements = document.select("b:contains(Publisher:)");
            String publisherHtml = elements.first().parent().html();
            publisherHtml = publisherHtml.substring(
                    publisherHtml.lastIndexOf("</b>") + 4, publisherHtml.length()).trim();
            String[] publisherAndYear = splitPublisherString(publisherHtml);
            outputMap.put("publisher", publisherAndYear[0]);
            outputMap.put("pubyear", publisherAndYear[1]);

            // Getting the language
            elements = document.select("b:contains(Language:)");
            String languageHtml = elements.first().parent().html();
            languageHtml = languageHtml.substring(
                    languageHtml.lastIndexOf("</b>") + 4, languageHtml.length()).trim();
            outputMap.put("language", (Language.languageStringToId(languageHtml)
                    == Language.OTHER_LANGUAGE_ID) ? "Other" : languageHtml);
            outputMap.put("languageid", Integer.toString(Language.languageStringToId(languageHtml)));

            // Getting the image
            elements = document.getElementsByClass("a-dynamic-image");
            String imageHtml = elements.first().attr("data-a-dynamic-image");
            String[] imageHtmlArray = imageHtml.split(Pattern.quote("\"http"));
            imageHtml = "http" + imageHtmlArray[1];
            imageHtml = imageHtml.split(Pattern.quote("\""))[0];
            outputMap.put("imagepath", imageHtml);
        } catch (Exception e) {
            return null;
        }

        return outputMap;
    }

    private String arrayStringToSequence(String arrayString) {
        return arrayString.replace("[", "").replace("]", "")
                .replace("\"", "").replace(",", ", ");
    }

    private Map<String, String> getByIsbnGoogle(Map<String, String> map, String isbn) {
        Map<String, String> outputMap = new HashMap<>(map);
        try {
            String googlePage = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
            Connection.Response response = Jsoup.connect(googlePage + isbn).ignoreContentType(true).execute();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(response.body());
            if (Integer.parseInt(jsonObject.get("totalItems").toString()) == 0)
                throw new RuntimeException();

            JSONObject firstItem = (JSONObject)((JSONArray)jsonObject.get("items")).get(0);
            response = Jsoup.connect(firstItem.get("selfLink").toString()).ignoreContentType(true).execute();
            firstItem = (JSONObject)jsonParser.parse(response.body());
            JSONObject volumeInfo = (JSONObject)firstItem.get("volumeInfo");
            outputMap.put("title", volumeInfo.get("title").toString());
            outputMap.put("description", volumeInfo.get("description").toString() +
                "<br><br>" + "https://books.google.com" + volumeInfo.get("previewLink").toString()
                    .split("http[s]*://books.google.[a-z]+")[1]
                    .split(Pattern.quote("&"))[0]);
            outputMap.put("publisher", volumeInfo.get("publisher").toString());
            outputMap.put("pubyear", volumeInfo.get("publishedDate").toString().substring(0, 4));
            outputMap.put("author", arrayStringToSequence(volumeInfo.get("authors").toString()));
            JSONObject thumbnailImages = (JSONObject)volumeInfo.get("imageLinks");
            outputMap.put("imagepath", thumbnailImages.get("thumbnail")
                    .toString().split(Pattern.quote("&imgtk="))[0]);
            String languageString = Language.googleLanguageShortToLong(
                    volumeInfo.get("language").toString());
            outputMap.put("language", (Language.languageStringToId(languageString)
                    == Language.OTHER_LANGUAGE_ID) ? "Other" : languageString);
            outputMap.put("languageid", Integer.toString(Language.languageStringToId(languageString)));
        } catch (Exception e) {
            return null;
        }

        return outputMap;
    }

    private Map<String, String> getByIsbnEster(Map<String, String> map, String isbn) {
        Map<String, String> outputMap = new HashMap<>(map);
        try {
            String esterPage = "https://www.ester.ee/search~S1*est/X?searchtype=i&searcharg=";
            Document document = Jsoup.connect(esterPage + isbn).get();
            Elements elements = document.select("tr[class=msg]");
            if (!elements.isEmpty())
                throw new RuntimeException();

            outputMap.put("publisher", "ester");
        } catch (Exception e) {
            return null;
        }

        return outputMap;
    }

    private Map<String, String> getDefaultMap() {
        Map<String, String> outputMap = new HashMap<>();
        outputMap.put("title", "");
        outputMap.put("description", "");
        outputMap.put("author", "");
        outputMap.put("publisher", "");
        outputMap.put("pubyear", "");
        outputMap.put("language", "");
        outputMap.put("imagepath", "https://bookmarket.online:18000/images/no-image.svg");
        return outputMap;
    }

    @RequestMapping(value = "getinfo", method = RequestMethod.GET)
    public Map<String, String> getBookIsbnInfo(@RequestParam(value = "isbn") String isbnNumber) {
        Map<String, String> outputMap = new HashMap<>();
        isbnNumber = isbnNumber.trim();
        outputMap.put("title", null);
        outputMap.put("description", null);
        outputMap.put("author", null);
        outputMap.put("publisher", null);
        outputMap.put("pubyear", null);
        outputMap.put("language", null);
        outputMap.put("imagepath", null);
        if (isbnNumber.length() != 10 && isbnNumber.length() != 13)
            return getDefaultMap();

        Map<String, String> intermediateOut = getByIsbnAmazon(outputMap, isbnNumber);
        if (intermediateOut != null) {
            outputMap = intermediateOut;
            return outputMap;
        } else {
            intermediateOut = getByIsbnGoogle(outputMap, isbnNumber);
            if (intermediateOut != null) {
                outputMap = intermediateOut;
                return outputMap;
            } else {
                intermediateOut = getByIsbnEster(outputMap, isbnNumber);
                if (intermediateOut != null) {
                    outputMap = intermediateOut;
                    return outputMap;
                }
            }
        }
        return getDefaultMap();
    }
}