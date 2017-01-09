package ru.sbtqa.tag.columbo.panes.treecomponents;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import ru.sbtqa.tag.columbo.Ctx;
import ru.sbtqa.tag.columbo.managers.ImageManager;

import javax.validation.constraints.NotNull;

/**
 * Basic graphics panel for tree node
 *
 * Created by SBT-Razuvaev-SV on 29.12.2016.
 */
public class AbstractTreeItemView extends GridPane {

    private TreeItemType treeItemType;
    private Label icon = null;
    private Label name = null;
    private Label info = null;

    public AbstractTreeItemView(@NotNull TreeItemType treeItemType, String name, String info) {
        this.treeItemType = treeItemType;
        this.icon = new Label("", new ImageView(Ctx.get().getBean(ImageManager.class).getImageForTreeItem(treeItemType)));
        this.icon.setTooltip(new Tooltip( treeItemType.getTooltip() ));
        this.add(this.icon, 0, 0, 1, 1);
        if (name != null) {
            this.name = new Label(name);
            this.name.setWrapText(true);
            this.add(this.name, 1, 0, 1, 1);
        }
        if (info != null) {
            this.info = new Label(info);
            this.add(this.info, 2, 0, 1, 1);
        }
    }

    public TreeItemType getTreeItemType() {
        return this.treeItemType;
    }

    public Label getName() {
        return name;
    }

    public void setName(Label name) {
        this.getChildren().remove(this.name);
        this.name = name;
        this.name.setWrapText(true);
        if (name != null) {
            this.add(this.name, 1, 0, 1, 1);
        }
    }

    public Label getInfo() {
        return info;
    }

    public void setInfo(Label info) {
        this.getChildren().remove(this.info);
        this.info = info;
        if (info != null) {
            this.add(this.info, 2, 0, 1, 1);
        }
    }

}
