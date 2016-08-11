package controller;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

public class Internationalization extends Observable {
    private Boolean langWasChanged = false;
    private Locale russian = new Locale("ru", "RU");
    ResourceBundle lang_ru = ResourceBundle.getBundle("bundles.Locale_ru", russian);
    private Locale english = new Locale("en", "US");
    ResourceBundle lang_en = ResourceBundle.getBundle("bundles.Locale_en", english);
    private ResourceBundle lang = lang_ru;

    public ResourceBundle getLang() {
        return lang;
    }

    public void setLang(String langType) {
        if (langType == "ru") {
            System.out.println("Change to ru");
            lang = lang_ru;
        } else if (langType == "eng") {
            System.out.println("Change to eng");
            lang = lang_en;
        } else {
            System.err.print("not support languige");
            return;
        }
        super.setChanged();
        notifyObservers();
    }

    Boolean getState() {
        return langWasChanged;
    }
}
