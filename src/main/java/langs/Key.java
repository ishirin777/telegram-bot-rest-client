package langs;

public class Key {

    private String language;

    public Key(String language) {
        setLanguage(language);
    }

    String getLanguage() {
        return language;
    }

    private void setLanguage(String language) {
        this.language = language;
    }
}
