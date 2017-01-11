package ru.sbtqa.tag.columbo.panes.treecomponents;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import ru.sbtqa.tag.columbo.Ctx;
import ru.sbtqa.tag.columbo.scanner.GraphBuilder;
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
            this.getName().setId("element-name");
        }
        if (getInfo() != null) {
            this.getInfo().setId("element-class");
        }
        modifyContextMenu();
    }

    public ElementInfoHolder getInfoHolder() {
        return infoHolder;
    }

    private void modifyContextMenu() {
        MenuItem expandElementSubtree = new MenuItem("Expand subtree");
        expandElementSubtree.setOnAction( event -> Ctx.get().getBean(GraphBuilder.class).expandElementSubtree(this) );
        MenuItem collapseElementSubtree = new MenuItem("Collapse subtree");
        collapseElementSubtree.setOnAction( event -> Ctx.get().getBean(GraphBuilder.class).collapseElementSubtree(this) );
        if (getName() != null) {
            getName().getContextMenu().getItems().addAll(new SeparatorMenuItem(), expandElementSubtree, collapseElementSubtree);
        } else if (getInfo() != null) {
            getInfo().getContextMenu().getItems().addAll(new SeparatorMenuItem(), expandElementSubtree, collapseElementSubtree);
        }
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
