package ru.sbtqa.tag.columbo.panes;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.panes.treecomponents.AbstractTreeItemView;

import javax.annotation.PostConstruct;

/**
 * Pane with hotkeys
 *
 * Created by SBT-Razuvaev-SV on 11.01.2017.
 */
@Component
public class HotKeyPane extends MenuBar {

    private TreeItem<? extends AbstractTreeItemView> lastSelectedNode = null;

    @PostConstruct
    private void init() {
        this.setManaged(false);
        this.setVisible(false);
        Menu hotKeyMenu = new Menu();
        MenuItem copySelectedName = new MenuItem();
        copySelectedName.setAccelerator(KeyCombination.keyCombination("Ctrl + c"));
        copySelectedName.setOnAction(event -> {
            if ( lastSelectedNode != null ) {
                final ClipboardContent content = new ClipboardContent();
                AbstractTreeItemView lastSelectedView = lastSelectedNode.getValue();
                String copiedText = lastSelectedView.getName() == null ? lastSelectedView.getInfo().getText() : lastSelectedView.getName().getText();
                content.putString(copiedText);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });
        hotKeyMenu.getItems().add(copySelectedName);
        this.getMenus().add(hotKeyMenu);
    }

    public void setLastSelectedNode(TreeItem<? extends AbstractTreeItemView> lastSelectedNode) {
        this.lastSelectedNode = lastSelectedNode;
    }

}
