package ru.sbtqa.tag.columbo.panes.treecomponents;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import ru.sbtqa.tag.columbo.Ctx;
import ru.sbtqa.tag.columbo.managers.ImageManager;

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

    public AbstractTreeItemView(TreeItemType treeItemType, String name, String info) {
        this.treeItemType = treeItemType;
        this.icon = new Label("", new ImageView(Ctx.get().getBean(ImageManager.class).getImageForTreeItem(treeItemType)));
        this.icon.setTooltip(new Tooltip( treeItemType.getTooltip() ));
        this.add(this.icon, 0, 0, 1, 1);
        if (name != null) {
            this.name = new Label(name);
            setNameLabel(this.name);
        }
        if (info != null) {
            this.info = new Label(info);
            setInfoLabel(this.info);
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
        setNameLabel(name);
    }

    public Label getInfo() {
        return info;
    }

    public void setInfo(Label info) {
        this.getChildren().remove(this.info);
        this.info = info;
        setInfoLabel(info);
    }

    private void setNameLabel(Label name) {
        if (name != null) {
            name.setWrapText(true);
            setContextMenu(name);
            this.add(this.name, 1, 0, 1, 1);
        }
    }

    private void setInfoLabel(Label info) {
        if (info != null) {
            setContextMenu(info);
            this.add(this.info, 2, 0, 1, 1);
        }
    }

    private void setContextMenu(Label element) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyNameMenuItem = new MenuItem("Copy as text");
        copyNameMenuItem.setOnAction(event -> {
            final ClipboardContent content = new ClipboardContent();
            content.putString(element.getText());
            Clipboard.getSystemClipboard().setContent(content);
        });
        contextMenu.getItems().addAll(copyNameMenuItem);
        element.setContextMenu(contextMenu);
    }

}
