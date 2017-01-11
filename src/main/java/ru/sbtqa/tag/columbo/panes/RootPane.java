package ru.sbtqa.tag.columbo.panes;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.managers.ConfigurationManager;

import javax.annotation.PostConstruct;

/**
 * Root panel
 *
 * Created by SBT-Razuvaev-SV on 27.12.2016.
 */
@Component("rootPane")
public class RootPane extends BorderPane {
    private static final Logger log = LoggerFactory.getLogger(BorderPane.class);

    @Autowired private ConfigurationManager configurationManager;
    @Autowired private HotKeyPane hotKeyPane;
    @Autowired private MainToolBar mainToolBar;
    @Autowired private PageTreePane pageTreePane;
    @Autowired private PageDetailsPane pageDetailsPane;
    @Autowired private HelpPane helpPane;
    @Autowired private StatusBar statusBar;

    private SplitPane splitPane;

    @PostConstruct
    void init() {
        splitPane = new SplitPane();
        splitPane.getItems().addAll(pageTreePane, pageDetailsPane);
        splitPane.getDividers().get(0).positionProperty().addListener(
                (observable, oldValue, newValue) -> {
                    configurationManager.setGuiProperty(GUI_PROPERTY.DIVIDER_POSITION.toString(), newValue.toString());
                }
        );
        splitPane.setDividerPosition(0,getGuiProperty(GUI_PROPERTY.DIVIDER_POSITION));
        this.setLeft(hotKeyPane);
        this.setTop(mainToolBar);
        this.setCenter(splitPane);
        this.setBottom(statusBar);
        this.widthProperty().addListener(
                (observable, oldValue, newValue) -> {
                    configurationManager.setGuiProperty(GUI_PROPERTY.WIDTH.toString(), newValue.toString());
                }
        );
        this.heightProperty().addListener(
                (observable, oldValue, newValue) -> {
                    configurationManager.setGuiProperty(GUI_PROPERTY.HEIGHT.toString(), newValue.toString());
                }
        );
        this.setWidth(getGuiProperty(GUI_PROPERTY.WIDTH));
        this.setHeight(getGuiProperty(GUI_PROPERTY.HEIGHT));
    }

    private Double getGuiProperty(GUI_PROPERTY key) {
        String guiProperty = configurationManager.getGuiProperty(key.toString());
        if (guiProperty != null) {
            try {
                return Double.valueOf(guiProperty);
            } catch (Exception e) {
                log.warn("Error when parsing property: " + configurationManager.getGuiProperty(key.toString()), e);
                return getDefaultGuiProperty(key);
            }
        } else {
            return getDefaultGuiProperty(key);
        }
    }

    private Double getDefaultGuiProperty(GUI_PROPERTY key) {
        Double guiProperty = null;
        switch (key) {
            case DIVIDER_POSITION: guiProperty = 0.35d; break;
            case WIDTH: guiProperty = 1200d; break;
            case HEIGHT: guiProperty = 800d; break;
        }
        return guiProperty;
    }

    public void showHelpPanel() {
        this.setRight(helpPane);
    }

    public void hideHelpPanel() {
        this.setRight(null);
    }

    private enum GUI_PROPERTY {
        WIDTH, HEIGHT, DIVIDER_POSITION
    }

}
