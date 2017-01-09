package ru.sbtqa.tag.columbo.panes;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbtqa.tag.columbo.Launch;
import ru.sbtqa.tag.columbo.managers.ConfigurationManager;
import ru.sbtqa.tag.columbo.managers.ImageManager;
import ru.sbtqa.tag.columbo.scanner.ProjectScanner;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalTime;

/**
 * Toolbar panel
 *
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@Component
public class MainToolBar extends HBox {
    private static final Logger log = LoggerFactory.getLogger(MainToolBar.class);

    private static final Double TEXT_FIELD_WIDTH = 200d;
    private static final Double EXPANDER_WIDTH = 2d;

    @Autowired private ProjectScanner projectScanner;
    @Autowired private ConfigurationManager configurationManager;
    @Autowired private ImageManager imageManager;
    @Autowired private RootPane rootPane;
    @Autowired private PageTreePane pageTreePane;
    @Autowired private PageDetailsPane pageDetailsPane;
    @Autowired private StatusBar statusBar;

    private Button openProjectSrc;
    private Button refreshProjectSrc;
    private Label pageSearchLeftExpander;
    private TextField pageSearchField;
    private Label expander;
    private ToggleButton filterActionMethods;
    private ToggleButton filterHtmlElements;
    private ToggleButton filterTypifiedElements;
    private ToggleButton filterWebElement;
    private ToggleButton filterListOfElements;
    private Label elementSearchLeftExpander;
    private TextField elementSearchField;
    private Label elementSearchRightExpander;
    private ToggleButton help;

    @PostConstruct
    private void init() {
        openProjectSrc = new Button("", new ImageView(imageManager.getImage("plus.png")));
        openProjectSrc.setTooltip(new Tooltip("Set project source folder"));
        openProjectSrc.setOnAction(event -> {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            final File selectedDirectory = directoryChooser.showDialog(Launch.getPrimaryStage());
            if (selectedDirectory != null) {
                configurationManager.setProperty("src_dir_path", selectedDirectory.getAbsolutePath());
                projectScanner.loadSourceFiles();
                statusBar.setStatusMessage("Source files from " + selectedDirectory.getAbsolutePath() + " loaded successfully at " + LocalTime.now());
            }
        });

        refreshProjectSrc = new Button("", new ImageView(imageManager.getImage("arrow-circle-double.png")));
        refreshProjectSrc.setTooltip(new Tooltip("Refresh sources"));
        refreshProjectSrc.setOnAction(event -> {
            pageSearchField.clear();
            projectScanner.loadSourceFiles();
            statusBar.setStatusMessage("Source files reloaded successfully at " + LocalTime.now());
        });

        pageSearchLeftExpander = new Label();
        pageSearchLeftExpander.setPrefWidth(EXPANDER_WIDTH);

        pageSearchField = new TextField();
        pageSearchField.setId("pageSearch-field");
        pageSearchField.setPrefWidth(TEXT_FIELD_WIDTH);
        pageSearchField.setMaxHeight(Double.MAX_VALUE);
        pageSearchField.setPromptText("Page search");
        pageSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pageTreePane.pageSearch(newValue.toLowerCase());
        });

        expander = new Label();
        expander.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(expander, Priority.ALWAYS);

        filterActionMethods = new ToggleButton("", new ImageView(imageManager.getImage("action-method.png")));
        filterActionMethods.setTooltip(new Tooltip("Show/Hide action methods"));
        filterActionMethods.setSelected(getFilterProperty("filter-action-method-selected"));
        filterActionMethods.setOnAction(event -> {
            configurationManager.setProperty("filter-action-method-selected", Boolean.toString(filterActionMethods.isSelected()));
            pageDetailsPane.filterRootTreeItem();
        });

        filterHtmlElements = new ToggleButton("", new ImageView(imageManager.getImage("html-element.png")));
        filterHtmlElements.setTooltip(new Tooltip("Show/Hide html elements"));
        filterHtmlElements.setSelected(getFilterProperty("filter-html-element-selected"));
        filterHtmlElements.setOnAction(event -> {
            configurationManager.setProperty("filter-html-element-selected", Boolean.toString(filterHtmlElements.isSelected()));
            pageDetailsPane.filterRootTreeItem();
        });

        filterListOfElements = new ToggleButton("", new ImageView(imageManager.getImage("list-elements.png")));
        filterListOfElements.setTooltip(new Tooltip("Show/Hide lists of elements"));
        filterListOfElements.setSelected(getFilterProperty("filter-list-element-selected"));
        filterListOfElements.setOnAction(event -> {
            configurationManager.setProperty("filter-list-element-selected", Boolean.toString(filterListOfElements.isSelected()));
            pageDetailsPane.filterRootTreeItem();
        });

        filterTypifiedElements = new ToggleButton("", new ImageView(imageManager.getImage("typified-element.png")));
        filterTypifiedElements.setTooltip(new Tooltip("Show/Hide typified elements"));
        filterTypifiedElements.setSelected(getFilterProperty("filter-typified-element-selected"));
        filterTypifiedElements.setOnAction(event -> {
            configurationManager.setProperty("filter-typified-element-selected", Boolean.toString(filterTypifiedElements.isSelected()));
            pageDetailsPane.filterRootTreeItem();
        });

        filterWebElement = new ToggleButton("", new ImageView(imageManager.getImage("web-element.png")));
        filterWebElement.setTooltip(new Tooltip("Show/Hide web elements"));
        filterWebElement.setSelected(getFilterProperty("filter-web-element-selected"));
        filterWebElement.setOnAction(event -> {
            configurationManager.setProperty("filter-web-element-selected", Boolean.toString(filterWebElement.isSelected()));
            pageDetailsPane.filterRootTreeItem();
        });

        elementSearchLeftExpander = new Label();
        elementSearchLeftExpander.setPrefWidth(EXPANDER_WIDTH);

        elementSearchField = new TextField();
        elementSearchField.setId("pageSearch-field");
        elementSearchField.setPrefWidth(TEXT_FIELD_WIDTH);
        elementSearchField.setMaxHeight(Double.MAX_VALUE);
        elementSearchField.setPromptText("Element search");
        elementSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            pageDetailsPane.filterRootTreeItem();
        });

        elementSearchRightExpander = new Label();
        elementSearchRightExpander.setPrefWidth(EXPANDER_WIDTH);

        help = new ToggleButton("", new ImageView(imageManager.getImage("question.png")));
        help.setTooltip(new Tooltip("Show/Hide help panel"));
        help.setOnAction(event -> {
            if (help.isSelected()) {
                rootPane.showHelpPanel();
            } else {
                rootPane.hideHelpPanel();
            }
        });

        this.getChildren().addAll(openProjectSrc, refreshProjectSrc, pageSearchLeftExpander, pageSearchField, expander,
                filterActionMethods, filterHtmlElements, filterListOfElements, filterTypifiedElements, filterWebElement,
                elementSearchLeftExpander, elementSearchField, elementSearchRightExpander, help);
        this.setId("tool-bar");
    }

    public boolean isActiomMethodSelected() {
        return filterActionMethods.isSelected();
    }

    public boolean isHtmlElementSelected() {
        return filterHtmlElements.isSelected();
    }

    public boolean isTypifiedElementSelected() {
        return filterTypifiedElements.isSelected();
    }

    public boolean isWebElementSelected() {
        return filterWebElement.isSelected();
    }

    public boolean isListElementSelected() {
        return filterListOfElements.isSelected();
    }

    public String getElementSearchText() {
        return elementSearchField.getText();
    }

    private boolean getFilterProperty(String key) {
        String filterProperty = configurationManager.getProperty(key);
        if (filterProperty != null) {
            try {
                return Boolean.valueOf(filterProperty);
            } catch (Exception e) {
                log.warn("Error when converting filter property from prop: " + configurationManager.getProperty(key), e);
                return true;
            }
        } else {
            return true;
        }
    }

}
