package ru.sbtqa.tag.columbo.scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ru.sbt.qa.bdd.pageFactory.Page;
import ru.sbtqa.tag.columbo.panes.MainToolBar;
import ru.sbtqa.tag.columbo.panes.PageDetailsPane;
import ru.sbtqa.tag.columbo.panes.treecomponents.ElementTreeItemView;
import ru.sbtqa.tag.columbo.panes.treecomponents.PageTreeItemView;
import ru.sbtqa.tag.columbo.panes.treecomponents.TreeItemType;
import ru.sbtqa.tag.columbo.panes.treecomponents.factories.ElementTreeItemFactory;
import ru.sbtqa.tag.columbo.panes.treecomponents.factories.PageTreeItemFactory;
import ru.sbtqa.tag.columbo.units.PageInfoHolder;
import ru.sbtqa.tag.columbo.utils.TreeItemElementComparator;
import ru.sbtqa.tag.columbo.utils.TreeItemPageComparator;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for build the object graph.
 *
 * Created by SBT-Razuvaev-SV on 29.12.2016.
 */
@Component
@DependsOn("pageDetailsPane")
public class GraphBuilder {
    private static final Logger log = LoggerFactory.getLogger(GraphBuilder.class);

    @Autowired private MainToolBar mainToolBar;
    @Autowired private PageDetailsPane pageDetailsPane;
    @Autowired private PageTreeItemFactory pageTreeItemFactory;
    @Autowired private ElementTreeItemFactory elementTreeItemFactory;

    /**
     * Entry point for building page tree
     * @param loadedClasses - list of classes from project
     */
    public TreeItem<PageTreeItemView> buildPageTree(List<Class<?>> loadedClasses) {
        TreeItem<PageTreeItemView> root = new TreeItem<>();
        String name = Page.class.getSimpleName() + " (" + Page.class.getPackage().getName() + ") [abstract]";
        root.setValue(new PageTreeItemView(TreeItemType.ABSTRACT_PAGE, name, null, new PageInfoHolder(Page.class)));
        for (Class clazz : loadedClasses) {
            if ((Page.class).isAssignableFrom(clazz)) {
                addPageToPageTree(root, clazz);
            }
        }
        return root;
    }

    /**
     * Add page to page tree
     * @param rootNode - root element of the page tree
     * @param pageClass - page class for processing
     */
    private void addPageToPageTree(TreeItem<PageTreeItemView> rootNode, Class pageClass) {
        LinkedList<Class> stack = new LinkedList<>();
        Class processedClass = pageClass;
        do {
            stack.addFirst(processedClass);
            processedClass = processedClass.getSuperclass();
        } while ( !processedClass.getName().equals("java.lang.Object") );
        for (int i = 0; i < stack.size(); i++) {
            Class nodeClass = stack.get(i);
            if ( !isPageTreeContainsClass(rootNode, nodeClass) ) {
                TreeItem<PageTreeItemView> node = pageTreeItemFactory.create(nodeClass);
                getParentForPageTreeNode(rootNode, nodeClass).getChildren().add(node);
            }
        }
    }

    /**
     * Is necessary element exist in the page tree
     * @param parentNode - root element of the page tree
     * @param pageClass - page class for processing
     */
    private boolean isPageTreeContainsClass(TreeItem<PageTreeItemView> parentNode, Class pageClass) {
        if ( (parentNode.getValue().getInfoHolder().getElementClass().getName()).equals(pageClass.getName()) ) {
            return true;
        }
        if ( parentNode.getChildren().size() > 0 ) {
            for (TreeItem<PageTreeItemView> treeItem : parentNode.getChildren()) {
                boolean result = isPageTreeContainsClass(treeItem, pageClass);
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns parent node for page class
     * @param parentNode - parent element of the page tree
     * @param pageClass - page class for processing
     */
    private TreeItem<PageTreeItemView> getParentForPageTreeNode(TreeItem<PageTreeItemView> parentNode, Class pageClass) {
        Class parentClass = pageClass.getSuperclass();
        if ( (parentNode.getValue().getInfoHolder().getElementClass().getName()).equals(parentClass.getName()) ) {
            return parentNode;
        }
        if ( parentNode.getChildren().size() > 0 ) {
            for (TreeItem<PageTreeItemView> treeItem : parentNode.getChildren()) {
                TreeItem<PageTreeItemView> parentForNode = getParentForPageTreeNode(treeItem, pageClass);
                if (parentForNode != null) {
                    return parentForNode;
                }
            }
        }
        return null;
    }

    /**
     * Build alternative page tree based by rootNode tree.
     * @param rootNode - element for the construction of a new tree.
     * @param charSequence - the required char combination for name
     */
    public TreeItem<?> buildFilteredPageTree(TreeItem<PageTreeItemView> rootNode, String charSequence) {
        TreeItem<PageTreeItemView> filteredNode = pageCharFilter(rootNode, charSequence);
        if (filteredNode != null) {
            return filteredNode;
        } else {
            return new TreeItem<>("No pages found...");
        }
    }

    /**
     * Recursive method for building filtered page tree
     * @param parentNode - parent node for call
     * @param charSequence - the required char combination for page's name
     */
    private TreeItem<PageTreeItemView> pageCharFilter(TreeItem<PageTreeItemView> parentNode, String charSequence) {
        TreeItem<PageTreeItemView> searchItem = null;
        if ( (parentNode.getValue().toString()).contains(charSequence) ) {
            searchItem = new TreeItem<>(parentNode.getValue());
            searchItem.setExpanded(true);
        }
        if (parentNode.getChildren().size() > 0) {
            for (TreeItem<PageTreeItemView> childItem : parentNode.getChildren()) {
                TreeItem<PageTreeItemView> childSearchItem = pageCharFilter(childItem, charSequence);
                if (childSearchItem != null) {
                    if (searchItem == null) {
                        searchItem = new TreeItem<>(parentNode.getValue());
                        searchItem.setExpanded(true);
                    }
                    searchItem.getChildren().add(childSearchItem);
                }
            }
        }
        return searchItem;
    }

    /**
     * Recursive method to sort the page tree
     * @param parentNode - root tree element for sorting
     */
    public void sortPageTree(TreeItem<PageTreeItemView> parentNode) {
        if ( parentNode.getChildren().size() > 1 ) {
            FXCollections.sort(parentNode.getChildren(), new TreeItemPageComparator());
            for ( TreeItem<PageTreeItemView> treeItem : parentNode.getChildren() ) {
                sortPageTree(treeItem);
            }
        }
    }

    /**
     * Entry point for building elements tree
     * @param processedPage - Page class for processing
     */
    public void buildElementsTree(Class processedPage) {
        TreeItem<ElementTreeItemView> rootNode = elementTreeItemFactory.create(processedPage);
        rootNode.setExpanded(true);
        if ((Page.class).isAssignableFrom(processedPage) ) {
            TreeItem<ElementTreeItemView> pagePanels = findPanels(processedPage);
            if (pagePanels != null) {
                pagePanels.setExpanded(true);
                rootNode.getChildren().add( pagePanels );
            }
            TreeItem<ElementTreeItemView> pageElements = findElements(processedPage);
            if (pageElements != null) {
                pageElements.setExpanded(true);
                rootNode.getChildren().add( pageElements );
            }
            TreeItem<ElementTreeItemView> pageActions = findActionMethods(processedPage);
            if (pageActions != null) {
                pageActions.setExpanded(true);
                rootNode.getChildren().add (pageActions);
            }
        }
        pageDetailsPane.setRootTreeItem(rootNode);
    }

    /**
     * Returns root folder with elements from page
     * @param pageClass - page class for search
     */
    private TreeItem<ElementTreeItemView> findElements(Class pageClass) {
        TreeItem<ElementTreeItemView> pageElements = elementTreeItemFactory.create("Элементы");
        if ((Page.class).isAssignableFrom(pageClass)) {
            Field[] fields = pageClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if ( !(HtmlElement.class).isAssignableFrom(field.getType()) ) {
                    TreeItem<ElementTreeItemView> pageElement = elementTreeItemFactory.create(field);
                    if (pageElement != null) {
                        pageElements.getChildren().add(pageElement);
                    }
                }
            }
        }
        if (pageElements.getChildren().size() > 0) {
            return pageElements;
        } else {
            return null;
        }
    }

    /**
     * Returns root folder with annotated methods from page
     * @param pageClass - page class for search
     */
    private TreeItem<ElementTreeItemView> findActionMethods(Class pageClass) {
        TreeItem<ElementTreeItemView> pageActions = elementTreeItemFactory.create("Action-методы");
        if ((Page.class).isAssignableFrom(pageClass)) {
            Method[] methods = pageClass.getDeclaredMethods();
            for (Method method : methods) {
                TreeItem<ElementTreeItemView> actionMethod = elementTreeItemFactory.create(method);
                if (actionMethod != null) {
                    pageActions.getChildren().add(actionMethod);
                }
            }
        }
        if (pageActions.getChildren().size() > 0) {
            return pageActions;
        } else {
            return null;
        }
    }

    /**
     * Returns root folder with panels from page
     * @param pageClass - page class for search
     */
    private TreeItem<ElementTreeItemView> findPanels(Class pageClass) {
        TreeItem<ElementTreeItemView> pagePanels = elementTreeItemFactory.create("Панели");
        if ( (Page.class).isAssignableFrom(pageClass) ) {
            Field[] fields = pageClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if ( (HtmlElement.class).isAssignableFrom(field.getType()) ) {
                    TreeItem<ElementTreeItemView> pagePanel = elementTreeItemFactory.create(field);
                    if ( pagePanel != null ) {
                        List<TreeItem<ElementTreeItemView>> pagePanelElements = findPagePanelElements(field.getType());
                        for (TreeItem<ElementTreeItemView> panelElement : pagePanelElements) {
                            pagePanel.getChildren().add(panelElement);
                        }
                        pagePanels.getChildren().add(pagePanel);
                    }
                }
            }
        }
        if ( pagePanels.getChildren().size() > 0 ) {
            return pagePanels;
        } else {
            return null;
        }
    }

    /**
     * Recursive method that returns panel's inner elements
     * @param pagePanelClass - panel class for search
     */
    private List<TreeItem<ElementTreeItemView>> findPagePanelElements(Class pagePanelClass) {
        ObservableList<TreeItem<ElementTreeItemView>> listOfElements = FXCollections.observableArrayList();
        Field[] fields = pagePanelClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            TreeItem<ElementTreeItemView> panelElement = elementTreeItemFactory.create(field);
            if ( panelElement != null ) {
                if ((HtmlElement.class).isAssignableFrom(field.getType())) {
                    List<TreeItem<ElementTreeItemView>> pagePanelElements = findPagePanelElements(field.getType());
                    for (TreeItem<ElementTreeItemView> nestedTreeItem : pagePanelElements) {
                        panelElement.getChildren().add(nestedTreeItem);
                    }
                }
                listOfElements.add(panelElement);
            }
        }
        Method[] methods = pagePanelClass.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            TreeItem<ElementTreeItemView> actionMethod = elementTreeItemFactory.create(method);
            if (actionMethod != null) {
                listOfElements.add(actionMethod);
            }
        }
        FXCollections.sort(listOfElements, new TreeItemElementComparator());
        return listOfElements;
    }

    /**
     * Build alternative elements tree based by rootNode tree.
     * @param rootNode - element for the construction of a new tree.
     * @param charSequence - the required char combination for name
     */
    public TreeItem<?> buildFilteredElementsTree(TreeItem<ElementTreeItemView> rootNode, String charSequence) {
        TreeItem<ElementTreeItemView> resultNode = null;
        TreeItem<ElementTreeItemView> filteredNode = elementTypeFilter(rootNode);
        if ( charSequence.length() > 0 ) {
            resultNode = elementCharFilter(filteredNode, charSequence.toLowerCase());
        } else {
            resultNode = filteredNode;
        }
        return resultNode == null ? new TreeItem<>("No elements found...") : resultNode;
    }

    /**
     * Recursive method for building filtered elements tree
     * @param parentFilteredNode - parent node for call
     * @param charSequence - the required char combination for element's name
     */
    private TreeItem<ElementTreeItemView> elementCharFilter(TreeItem<ElementTreeItemView> parentFilteredNode, String charSequence) {
        TreeItem<ElementTreeItemView> searchItem = null;
        if ( charSequence.length() == 0 ) {
            searchItem = new TreeItem<>(parentFilteredNode.getValue());
            searchItem.setExpanded(true);
        } else if ( (parentFilteredNode.getValue().toString()).contains(charSequence) ) {
            searchItem = new TreeItem<>(parentFilteredNode.getValue());
            searchItem.setExpanded(true);
        }
        if (parentFilteredNode.getChildren().size() > 0) {
            for (TreeItem<ElementTreeItemView> childItem : parentFilteredNode.getChildren()) {
                TreeItem<ElementTreeItemView> childSearchItem = elementCharFilter(childItem, charSequence);
                if (childSearchItem != null) {
                    if (searchItem == null) {
                        searchItem = new TreeItem<>(parentFilteredNode.getValue());
                        searchItem.setExpanded(true);
                    }
                    searchItem.getChildren().add(childSearchItem);
                }
            }
        }
        return searchItem;
    }

    /**
     * Recursive method for building filtered element tree
     * @param parentNode - parent node for call
     */
    private TreeItem<ElementTreeItemView> elementTypeFilter(TreeItem<ElementTreeItemView> parentNode) {
        TreeItem<ElementTreeItemView> searchItem = null;
        if ( elementTypeCheck(parentNode) ) {
            searchItem = new TreeItem<>(parentNode.getValue());
            openRootNode(searchItem);
            for ( TreeItem<ElementTreeItemView> childItem : parentNode.getChildren() ) {
                TreeItem<ElementTreeItemView> searchChildItem = elementTypeFilter(childItem);
                if ( searchChildItem != null ) {
                    searchItem.getChildren().add(searchChildItem);
                }
            }
            if ( searchItem.getValue().getTreeItemType() == TreeItemType.FOLDER && searchItem.getChildren().size() == 0 ) {
                return null;
            }
        }
        return searchItem;
    }

    /**
     * Filter by element type
     * @param node - node for check
     */
    private boolean elementTypeCheck(TreeItem<ElementTreeItemView> node) {
        boolean result = false;
        TreeItemType treeItemType = node.getValue().getTreeItemType();
        switch (treeItemType) {
            case ACTION_METHOD: {
                if ( mainToolBar.isActiomMethodSelected() ) result = true; break;
            }
            case HTML_ELEMENT: {
                if ( mainToolBar.isHtmlElementSelected() ) result = true; break;
            }
            case LIST: {
                if ( mainToolBar.isListElementSelected() ) result = true; break;
            }
            case TYPIFIED_ELEMENT: {
                if ( mainToolBar.isTypifiedElementSelected() ) result = true; break;
            }
            case WEB_ELEMENT: {
                if ( mainToolBar.isWebElementSelected() ) result = true; break;
            }
            case PAGE:
            case ABSTRACT_PAGE:
            case FOLDER: {
                result = true; break;
            }
        }
        return result;
    }

    /**
     * Open root element (page, folder) in filtered tree
     * @param node - checked node
     */
    private void openRootNode(TreeItem<ElementTreeItemView> node) {
        switch (node.getValue().getTreeItemType()) {
            case PAGE:
            case ABSTRACT_PAGE:
            case FOLDER: { node.setExpanded(true); break; }
        }
    }

}
