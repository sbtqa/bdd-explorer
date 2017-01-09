package ru.sbt.qa.dcb.bddexplorer.panes.treecomponents.factories;

import javafx.scene.control.TreeItem;
import org.springframework.stereotype.Component;
import ru.sbt.qa.dcb.bddexplorer.panes.treecomponents.PageTreeItemView;
import ru.sbt.qa.dcb.bddexplorer.panes.treecomponents.TreeItemType;
import ru.sbt.qa.dcb.bddexplorer.units.PageInfoHolder;
import ru.sbt.qa.dcb.bddexplorer.utils.ReflectionUtils;

/**
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
