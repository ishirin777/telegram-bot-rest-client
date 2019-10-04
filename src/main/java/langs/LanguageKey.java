package langs;

public class LanguageKey {

    private String language;

    public LanguageKey(String language) {
        setLanguage(language);
    }

    String getLanguage() {
        return language;
    }

    private void setLanguage(String language) {
        this.language = language;
    }
}
