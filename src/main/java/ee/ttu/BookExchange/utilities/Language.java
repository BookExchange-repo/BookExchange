package ee.ttu.BookExchange.utilities;

public class Language {
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
                return "Unknown";
        }
    }
}
