package ru.sbtqa.tag.columbo.panes.treecomponents.factories;

import javafx.scene.control.TreeItem;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.panes.treecomponents.PageTreeItemView;
import ru.sbtqa.tag.columbo.panes.treecomponents.TreeItemType;
import ru.sbtqa.tag.columbo.units.PageInfoHolder;
import ru.sbtqa.tag.columbo.utils.ReflectionUtils;

/**
 * Factory for building tree nodes for pages
 *
 * Created by SBT-Razuvaev-SV on 06.01.2017.
 */
@Component
public class PageTreeItemFactory {

    public TreeItem<PageTreeItemView> create(Class pageClass) {
        TreeItem<PageTreeItemView> pageNode = new TreeItem<>();
        PageInfoHolder infoHolder = new PageInfoHolder(pageClass);
        TreeItemType treeItemType = TreeItemType.PAGE;
        String name = ReflectionUtils.getPageEntry(pageClass);
        String info = pageClass.getSimpleName() + " (" + pageClass.getPackage() + ")";
        if ( ReflectionUtils.isAbstract(pageClass) ) {
            treeItemType = TreeItemType.ABSTRACT_PAGE;
            info = info + " [abstract]";
        }
        PageTreeItemView nodeView = new PageTreeItemView(treeItemType, name, info, infoHolder);
        pageNode.setValue(nodeView);
        return pageNode;
    }

}
