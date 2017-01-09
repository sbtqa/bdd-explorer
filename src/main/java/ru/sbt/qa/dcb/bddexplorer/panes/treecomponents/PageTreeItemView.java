package ru.sbt.qa.dcb.bddexplorer.panes.treecomponents;

import ru.sbt.qa.dcb.bddexplorer.units.PageInfoHolder;

/**
 * Created by SBT-Razuvaev-SV on 29.12.2016.
 */
public class PageTreeItemView extends AbstractTreeItemView {

    private PageInfoHolder infoHolder;

    public PageTreeItemView(TreeItemType treeItemType, String name, String info, PageInfoHolder infoHolder) {
        super(treeItemType, name, info);
        this.infoHolder = infoHolder;
        if (getName() != null) {
            this.getName().setId("page-name-label");
            this.setInfo(null);
        }
        if (getInfo() != null) {
            this.getInfo().setId("page-class-label");
        }
    }

    public PageInfoHolder getInfoHolder() {
        return infoHolder;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (getName() != null) {
            result.append(getName().getText()).append(" ");
        }
        if (getInfo() != null) {
            result.append(getInfo().getText()).append(" ");
        }
        result.append(getInfoHolder().getElementClass().getName());
        return result.toString().toLowerCase();
    }

}
