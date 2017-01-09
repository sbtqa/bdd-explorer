package ru.sbtqa.tag.columbo.panes.treecomponents;

import ru.sbtqa.tag.columbo.units.ElementInfoHolder;

/**
 * Graphics panel for element's node
 *
 * Created by SBT-Razuvaev-SV on 29.12.2016.
 */
public class ElementTreeItemView extends AbstractTreeItemView {

    private ElementInfoHolder infoHolder = null;

    public ElementTreeItemView(TreeItemType treeItemType, String name, String info, ElementInfoHolder infoHolder) {
        super(treeItemType, name, info);
        this.infoHolder = infoHolder;
        if (getName() != null) {
            this.getName().setId("element-name-label");
        }
        if (getInfo() != null) {
            this.getInfo().setId("element-class-label");
        }
    }

    public ElementInfoHolder getInfoHolder() {
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
        return result.toString().toLowerCase();
    }

}
