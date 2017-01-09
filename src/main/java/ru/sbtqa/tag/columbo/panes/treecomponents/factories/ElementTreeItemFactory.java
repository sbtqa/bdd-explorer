package ru.sbtqa.tag.columbo.panes.treecomponents.factories;

import javafx.scene.control.TreeItem;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.panes.treecomponents.ElementTreeItemView;
import ru.sbtqa.tag.columbo.panes.treecomponents.TreeItemType;
import ru.sbtqa.tag.columbo.units.ElementInfoHolder;
import ru.sbtqa.tag.columbo.utils.ReflectionUtils;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TypifiedElement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Factory for building tree nodes for elements
 *
 * Created by SBT-Razuvaev-SV on 06.01.2017.
 */
@Component
public class ElementTreeItemFactory {

    public TreeItem<ElementTreeItemView> create(Field field) {
        TreeItem<ElementTreeItemView> element = null;
        ElementInfoHolder elementHolder = new ElementInfoHolder(field);
        String name = ReflectionUtils.getElementTitle(field);
        String info = Modifier.toString( field.getModifiers() ) + " " + field.getType().getSimpleName() + " " + field.getName();
        if ((HtmlElement.class).isAssignableFrom(field.getType())) {
            ElementTreeItemView elementView = new ElementTreeItemView(TreeItemType.HTML_ELEMENT, name, info, elementHolder);
            element = new TreeItem<>(elementView);
        } else if ((WebElement.class).isAssignableFrom(field.getType())) {
            ElementTreeItemView elementView = new ElementTreeItemView(TreeItemType.WEB_ELEMENT, name, info, elementHolder);
            element = new TreeItem<>(elementView);
        } else if ((TypifiedElement.class).isAssignableFrom(field.getType())) {
            ElementTreeItemView elementView = new ElementTreeItemView(TreeItemType.TYPIFIED_ELEMENT, name, info, elementHolder);
            element = new TreeItem<>(elementView);
        } else if ((List.class).isAssignableFrom(field.getType()) ) {
            ElementTreeItemView elementView = new ElementTreeItemView(TreeItemType.LIST, name, info, elementHolder);
            element = new TreeItem<>(elementView);
        }
        return element;
    }

    public TreeItem<ElementTreeItemView> create(Method method) {
        method.setAccessible(true);
        TreeItem<ElementTreeItemView> actionMethod = null;
        List<String> actionTitles = ReflectionUtils.getActionTitles(method);
        if ( actionTitles.size() > 0 ) {
            actionMethod = new TreeItem<>();
            StringBuilder name = new StringBuilder();
            for( String actionTitle : actionTitles ) {
                name.append(actionTitle).append("\n");
            }
            String info = Modifier.toString( method.getModifiers() ) + " " + method.getName();
            ElementTreeItemView actionMethodView = new ElementTreeItemView(TreeItemType.ACTION_METHOD, name.toString(), info, null);
            actionMethod.setValue(actionMethodView);
        }
        return actionMethod;
    }

    public TreeItem<ElementTreeItemView> create(Class pageClass) {
        TreeItem<ElementTreeItemView> rootPage = new TreeItem<>();
        TreeItemType treeItemType = TreeItemType.PAGE;
        String name = ReflectionUtils.getPageEntry(pageClass);
        String info = pageClass.getSimpleName() + " (" + pageClass.getPackage() + ")";
        if ( ReflectionUtils.isAbstract(pageClass) ) {
            treeItemType = TreeItemType.ABSTRACT_PAGE;
            info = info + " [abstract]";
        }
        ElementTreeItemView pageView = new ElementTreeItemView(treeItemType, name, info, null);
        rootPage.setValue(pageView);
        rootPage.setExpanded(true);
        return rootPage;
    }

    public TreeItem<ElementTreeItemView> create(String folderName) {
        TreeItem<ElementTreeItemView> folder = new TreeItem<>();
        ElementTreeItemView folderView = new ElementTreeItemView(TreeItemType.FOLDER, folderName, null, null);
        folder.setValue(folderView);
        return folder;
    }

}
