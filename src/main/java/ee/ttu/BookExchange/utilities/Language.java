package ee.ttu.BookExchange.utilities;

public class Language {
    public final static int OTHER_LANGUAGE_ID = 15;

    public static String googleLanguageShortToLong(String shortLanguage) {
        switch (shortLanguage) {
            case "ar":
                return "Arabic";
            case "bg":
                return "Bulgarian";
            case "bn":
                return "Bengali";
            case "ca":
                return "Catalan";
            case "cs":
                return "Czech";
            case "da":
                return "Danish";
            case "de":
                return "German";
            case "el":
                return "Greek";
            case "en":
                return "English";
            case "en-AU":
                return "English (Australian)";
            case "en-GB":
                return "English (Great Britain)";
            case "et":
                return "Estonian";
            case "es":
                return "Spanish";
            case "eu":
                return "Basque";
            case "fa":
                return "Farsi";
            case "fi":
                return "Finnish";
            case "fil":
                return "Filipino";
            case "fr":
                return "French";
            case "gl":
                return "Galician";
            case "gu":
                return "Gujarati";
            case "hi":
                return "Hindi";
            case "hr":
                return "Croatian";
            case "hu":
                return "Hungarian";
            case "id":
                return "Indonesian";
            case "it":
                return "Italian";
            case "iw":
                return "Hebrew";
            case "ja":
                return "Japanese";
            case "kn":
                return "Kannada";
            case "ko":
                return "Korean";
            case "lt":
                return "Lithuanian";
            case "lv":
                return "Latvian";
            case "ml":
                return "Malayalam";
            case "mr":
                return "Marathi";
            case "nl":
                return "Dutch";
            case "no":
                return "Norwegian";
            case "pl":
                return "Polish";
            case "pt":
                return "Portuguese";
            case "pt-BR":
                return "Portuguese (Brazil)";
            case "pt-PT":
                return "Portuguese (Portugal)";
            case "ro":
                return "Romanian";
            case "ru":
                return "Russian";
            case "sk":
                return "Slovak";
            case "sl":
                return "Slovenian";
            case "sr":
                return "Serbian";
            case "sv":
                return "Swedish";
            case "ta":
                return "Tamil";
            case "te":
                return "Telugu";
            case "th":
                return "Thai";
            case "tl":
                return "Tagalog";
            case "tr":
                return "Turkish";
            case "uk":
                return "Ukrainian";
            case "vi":
                return "Vietnamese";
            case "zh-CN":
                return "Chinese (Simplified)";
            case "zh-TW":
                return "Chinese (Traditional)";
            default:
                return "Other";
        }
    }

    public static int languageStringToId(String languageString) {
        switch (languageString) {
            case "Danish":
                return 11;
            case "German":
                return 4;
            case "English":
            case "English (Australian)":
            case "English (Great Britain)":
                return 1;
            case "Estonian":
                return 2;
            case "Spanish":
                return 6;
            case "French":
                return 5;
            case "Italian":
                return 7;
            case "Dutch":
                return 9;
            case "Norwegian":
                return 12;
            case "Polish":
                return 14;
            case "Portuguese":
            case "Portuguese (Brazil)":
            case "Portuguese (Portugal)":
                return 8;
            case "Russian":
                return 3;
            case "Swedish":
                return 10;
            case "Turkish":
                return 13;
            default:
                return OTHER_LANGUAGE_ID;
        }
    }

    public static String rahvaLanguageStringConvert(String languageString) {
        switch (languageString) {
            case "eestikeelne":
                return "Estonian";
            case "ingliskeelne":
                return "English";
            case "venekeelne":
                return "Russian";
            default:
                return "Other";
        }
    }
}
