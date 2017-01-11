package ru.sbtqa.tag.columbo.panes;

import javafx.geometry.Orientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.managers.ConfigurationManager;
import ru.sbtqa.tag.columbo.panes.treecomponents.ElementTreeItemView;
import ru.sbtqa.tag.columbo.scanner.GraphBuilder;
import ru.sbtqa.tag.columbo.utils.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * GUI panel with tree of elements
 *
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@Component("pageDetailsPane")
public class PageDetailsPane extends SplitPane {
    private static final Logger log = LoggerFactory.getLogger(PageDetailsPane.class);

    @Autowired private ConfigurationManager configurationManager;
    @Autowired private ElementPropertiesPane elementPropertiesPane;
    @Autowired private GraphBuilder graphBuilder;
    @Autowired private MainToolBar mainToolBar;
    @Autowired private HotKeyPane hotKeyPane;

    private ScrollPane topScrollPane;
    private ScrollPane bottomScrollPane;
    private TreeView pageElementsPane;
    private TreeItem<ElementTreeItemView> root;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        pageElementsPane = new TreeView();
        pageElementsPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    TreeItem<ElementTreeItemView> selectedItem = ((TreeItem<ElementTreeItemView>) newValue);
                    if ( selectedItem != null ) {
                        hotKeyPane.setLastSelectedNode( (TreeItem<ElementTreeItemView>) newValue );
                        if ( selectedItem.getValue().getInfoHolder() != null ) {
                            Map<String, String> fieldProperties = ReflectionUtils.extractPropertiesFromField( ((TreeItem<ElementTreeItemView>) newValue).getValue().getInfoHolder().getElementField() );
                            elementPropertiesPane.setProperties(fieldProperties);
                        } else {
                            elementPropertiesPane.setProperties(null);
                        }
                    }
                }
        );
        topScrollPane = new ScrollPane();
        topScrollPane.setFitToWidth(true);
        topScrollPane.setFitToHeight(true);
        topScrollPane.setContent(pageElementsPane);

        bottomScrollPane = new ScrollPane();
        bottomScrollPane.setFitToWidth(true);
        bottomScrollPane.setFitToHeight(true);
        bottomScrollPane.setContent(elementPropertiesPane);

        this.getItems().addAll(topScrollPane, bottomScrollPane);
        this.setOrientation(Orientation.VERTICAL);
        this.getDividers().get(0).positionProperty().addListener(
                (observable, oldValue, newValue) -> {
                    configurationManager.setGuiProperty("horizontal_divider_position", newValue.toString());
                }
        );
        this.setDividerPosition(0, getDividerPosition());
    }

    public void setRootTreeItem(TreeItem<ElementTreeItemView> root) {
        this.root = root;
        filterRootTreeItem();
    }

    @SuppressWarnings("unchecked")
    public TreeItem<ElementTreeItemView> getRootTreeItem() {
        return this.pageElementsPane.getRoot();
    }

    @SuppressWarnings("unchecked")
    public void filterRootTreeItem() {
        String searchElementText = mainToolBar.getElementSearchText();
        pageElementsPane.setRoot(graphBuilder.buildFilteredElementsTree(root, searchElementText));
    }

    private Double getDividerPosition() {
        String horizontalDividerPositionProperty = configurationManager.getGuiProperty("horizontal_divider_position");
        if (horizontalDividerPositionProperty != null) {
            try {
                return Double.valueOf( horizontalDividerPositionProperty );
            } catch (Exception e) {
                log.warn("Error when converting horizontal divider position from prop: " + configurationManager.getGuiProperty("horizontal_divider_position"), e);
                return 0.75d;
            }
        } else {
            return 0.75d;
        }
    }

}
