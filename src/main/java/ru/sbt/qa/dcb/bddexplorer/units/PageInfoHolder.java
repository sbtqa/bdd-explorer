package ru.sbt.qa.dcb.bddexplorer.units;

/**
 * Created by SBT-Razuvaev-SV on 29.12.2016.
 */
public class PageInfoHolder {

    private Class elementClass;
    private String title;

    public PageInfoHolder(Class elementClass) {
        this.elementClass = elementClass;
    }

    public Class getElementClass() {
        return elementClass;
    }
    public void setElementClass(Class elementClass) {
        this.elementClass = elementClass;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

}
