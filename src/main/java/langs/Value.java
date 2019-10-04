package langs;

public class Value {

    private String langType;

    public Value(String langType) {
        setLangType(langType);
    }

    public String getLangType() {
        return langType;
    }

    private void setLangType(String langType) {

        this.langType = langType;
    }

}
