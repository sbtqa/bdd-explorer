package ru.sbtqa.tag.columbo.panes.treecomponents;

/**
 * Type of tree node
 *
 * Created by SBT-Razuvaev-SV on 06.01.2017.
 */
public enum TreeItemType {

    PAGE("Page"),
    ABSTRACT_PAGE("Abstract page"),
    ACTION_METHOD("@ActionTitle method"),
    HTML_ELEMENT("Html element"),
    LIST("List of elements"),
    TYPIFIED_ELEMENT("Typified element (Wraps element)"),
    WEB_ELEMENT("Web element"),
    FOLDER("Folder");

    private String tooltip;

    TreeItemType(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getTooltip() {
        return this.tooltip;
    }

}
