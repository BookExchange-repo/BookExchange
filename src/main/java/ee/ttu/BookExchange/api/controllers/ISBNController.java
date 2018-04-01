package ee.ttu.BookExchange.api.controllers;

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
            Document document = Jsoup.connect(amazonPage + isbn.trim()).get();
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
                    break;
                }
            }

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
            outputMap.put("language", languageHtml);

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

    @RequestMapping(value = "getinfo", method = RequestMethod.GET)
    public Map<String, String> getBookIsbnInfo(@RequestParam(value = "isbn") String isbnNumber) {
        Map<String, String> outputMap = new HashMap<>();
        outputMap.put("title", null);
        outputMap.put("description", null);
        outputMap.put("author", null);
        outputMap.put("publisher", null);
        outputMap.put("pubyear", null);
        outputMap.put("language", null);
        outputMap.put("imagepath", null);
        Map<String, String> intermediateOut = getByIsbnAmazon(outputMap, isbnNumber);
        if (intermediateOut != null) {
            outputMap = intermediateOut;
            return outputMap;
        }
        return outputMap;
    }
}