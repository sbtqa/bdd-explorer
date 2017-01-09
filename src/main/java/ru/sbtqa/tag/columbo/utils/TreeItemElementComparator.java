package ru.sbtqa.tag.columbo.utils;

import javafx.scene.control.TreeItem;
import ru.sbtqa.tag.columbo.panes.treecomponents.ElementTreeItemView;

import java.util.Comparator;

/**
 * Default comparator for elements
 *
 * Created by SBT-Razuvaev-SV on 03.01.2017.
 */
public class TreeItemElementComparator implements Comparator<TreeItem<ElementTreeItemView>> {

    @Override
    public int compare(TreeItem<ElementTreeItemView> o1, TreeItem<ElementTreeItemView> o2) {
        int o1Count = o1.getChildren().size();
        int o2Count = o2.getChildren().size();
        if ( o1Count > 0 && o2Count == 0 ) {
            return -1;
        }
        if ( o1Count == 0 && o2Count > 0 ) {
            return 1;
        }
        return 0;
    }

}
