package langs;

public class LanguageValue {

    private String langType;

    public LanguageValue(String langType) {
        setLangType(langType);
    }

    public String getLangType() {
        return langType;
    }

    private void setLangType(String langType) {

        this.langType = langType;
    }

}
