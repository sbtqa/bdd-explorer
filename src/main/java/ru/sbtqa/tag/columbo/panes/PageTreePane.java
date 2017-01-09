package ru.sbtqa.tag.columbo.panes;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.managers.ConfigurationManager;
import ru.sbtqa.tag.columbo.panes.treecomponents.PageTreeItemView;
import ru.sbtqa.tag.columbo.scanner.GraphBuilder;

import javax.annotation.PostConstruct;

/**
 * GUI panel with tree of pages
 *
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@Component
public class PageTreePane extends ScrollPane {
    private static final Logger log = LoggerFactory.getLogger(PageTreePane.class);

    @Autowired private ConfigurationManager configurationManager;
    @Autowired private GraphBuilder graphBuilder;

    private TreeView pageTree;
    private TreeItem mainRoot;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        this.setId("project-tree-pane");
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        pageTree = new TreeView<>();
        pageTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pageTree.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null && ((TreeItem<PageTreeItemView>) newValue).getValue().getInfoHolder() != null ) {
                        graphBuilder.buildElementsTree(((TreeItem<PageTreeItemView>) newValue).getValue().getInfoHolder().getElementClass());
                        if (pageTree.getRoot() == mainRoot) {
                            configurationManager.setProperty("active_page_class", ((TreeItem<PageTreeItemView>) newValue).getValue().getInfoHolder().getElementClass().getName());
                        }
                    }
                }
        );
        this.setContent(pageTree);
    }

    @SuppressWarnings("unchecked")
    public void setMainRoot(TreeItem<PageTreeItemView> mainRoot) {
        this.mainRoot = mainRoot;
        this.pageTree.setRoot(mainRoot);
    }

    @SuppressWarnings("unchecked")
    public void pageSearch(String charSequence) {
        if (charSequence.length() > 0) {
            TreeItem<PageTreeItemView> searchRoot = graphBuilder.buildFilteredPageTree(mainRoot, charSequence);
            pageTree.setRoot(searchRoot);
        } else {
            pageTree.setRoot(mainRoot);
        }
    }

    @SuppressWarnings("unchecked")
    public void openToLevel(String className) {
        TreeItem<PageTreeItemView> root = pageTree.getRoot();
        if (root != null) {
            TreeItem<PageTreeItemView> result = getNodeThatContainsPageClass(root, className);
            if (result != null) {
                do {
                    result.setExpanded(true);
                    result = result.getParent();
                } while ( result != null );
                pageTree.getSelectionModel().select(getNodeThatContainsPageClass(root, className));
            } else {
                log.info("Page tree is not expanded. Page class " + className + " not found into tree");
            }
        }
    }

    private TreeItem<PageTreeItemView> getNodeThatContainsPageClass(TreeItem<PageTreeItemView> node, String pageClassName) {
        if (node.getChildren().size() > 0) {
            for (TreeItem<PageTreeItemView> treeItem : node.getChildren()) {
                if ( (treeItem.getValue().getInfoHolder().getElementClass().getName()).equals(pageClassName) ) {
                    return treeItem;
                }
                TreeItem<PageTreeItemView> innerTreeItem = getNodeThatContainsPageClass(treeItem, pageClassName);
                if (innerTreeItem != null) {
                    return innerTreeItem;
                }
            }
            return null;
        } else {
            return null;
        }
    }

}
