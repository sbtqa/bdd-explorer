package ru.sbt.qa.dcb.bddexplorer.units;

import java.lang.reflect.Field;

/**
 * Created by SBT-Razuvaev-SV on 03.01.2017.
 */
public class ElementInfoHolder {

    private Field elementField;
    private String title;

    public ElementInfoHolder(Field elementField) {
        this.elementField = elementField;
    }

    public Field getElementField() {
        return elementField;
    }
    public void setElementField(Field elementField) {
        this.elementField = elementField;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
