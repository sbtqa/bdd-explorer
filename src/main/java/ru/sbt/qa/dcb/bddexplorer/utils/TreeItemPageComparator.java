package ru.sbt.qa.dcb.bddexplorer.utils;

import javafx.scene.control.TreeItem;
import ru.sbt.qa.dcb.bddexplorer.panes.treecomponents.PageTreeItemView;

import java.util.Comparator;

/**
 * Created by SBT-Razuvaev-SV on 02.01.2017.
 */
public class TreeItemPageComparator implements Comparator<TreeItem<PageTreeItemView>> {

    @Override
    public int compare(TreeItem<PageTreeItemView> o1, TreeItem<PageTreeItemView> o2) {
        int o1Count = o1.getChildren().size();
        int o2Count = o2.getChildren().size();

        String o1Name = o1.getValue().getName() == null ? o1.getValue().getInfo().getText() : o1.getValue().getName().getText();
        String o2Name = o2.getValue().getName() == null ? o2.getValue().getInfo().getText() : o2.getValue().getName().getText();

        if ( (o1Count == 0 && o2Count == 0) || (o1Count > 0 && o2Count > 0) ) {
            return o1Name.compareTo(o2Name);
        }
        if ( o1Count > 0 && o2Count == 0 ) {
            return -1;
        }
        if ( o1Count == 0 && o2Count > 0 ) {
            return 1;
        }
        return 0;
    }

}
