class Key {

    private String language;

    Key(String language) {
        setLanguage(language);
    }

    String getLanguage() {
        return language;
    }

    private void setLanguage(String language) {
        this.language = language;
    }
}

class Value {

    private String langType;

    Value(String langType) {
        setLangType(langType);
    }

    String getLangType() {
        return langType;
    }

    private void setLangType(String langType) {
        this.langType = langType;
    }
}