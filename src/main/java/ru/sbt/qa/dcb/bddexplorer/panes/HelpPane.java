package ru.sbt.qa.dcb.bddexplorer.panes;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbt.qa.dcb.bddexplorer.managers.ImageManager;

import javax.annotation.PostConstruct;

/**
 * Created by SBT-Razuvaev-SV on 04.01.2017.
 */
@Component
public class HelpPane extends ScrollPane {

    @Autowired private ImageManager imageManager;

    private static final Double MAX_COMMENT_WIDTH = 350d;

    private GridPane grid;

    @PostConstruct
    private void init() {
        grid = new GridPane();
        grid.setId("help-grid-pane");
        grid.setHgap(5d);
        grid.setVgap(10d);
        this.setId("help-pane");
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        this.setContent(grid);

        ImageView addSource = new ImageView(imageManager.getImage("plus.png"));
        Label addSourceComment = new Label("Выбрать директорию с исходными файлами проекта. В директории должы содержаться скомпилированные классы. Для IntellijIdea это, обычно, папка target/classes");
        addSourceComment.setWrapText(true);
        addSourceComment.setTextAlignment(TextAlignment.JUSTIFY);
        addSourceComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(addSource, 0, 1);
        this.grid.add(addSourceComment, 1, 1);

        ImageView refreshSource = new ImageView(imageManager.getImage("arrow-circle-double.png"));
        Label refreshSourceComment = new Label("Перезачитать файлы исходников. Для того, чтобы изменения кода были видны приложению, проект тестов нужно пересобрать (чтобы обновились файлы в директории target/classes)");
        refreshSourceComment.setWrapText(true);
        refreshSourceComment.setTextAlignment(TextAlignment.JUSTIFY);
        refreshSourceComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(refreshSource, 0, 2);
        this.grid.add(refreshSourceComment, 1, 2);

        ImageView abstractPage = new ImageView(imageManager.getImage("abstract-page.png"));
        Label abstractPageComment = new Label("Иконка обозначает абстрактную страницу в иерархии страниц проекта");
        abstractPageComment.setWrapText(true);
        abstractPageComment.setTextAlignment(TextAlignment.JUSTIFY);
        abstractPageComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(abstractPage, 0, 3);
        this.grid.add(abstractPageComment, 1, 3);

        ImageView page = new ImageView(imageManager.getImage("page.png"));
        Label pageComment = new Label("Иконка обозначает обыкновенную страницу в иерархии страниц проекта");
        pageComment.setWrapText(true);
        pageComment.setTextAlignment(TextAlignment.JUSTIFY);
        pageComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(page, 0, 4);
        this.grid.add(pageComment, 1, 4);

        ImageView actionMethod = new ImageView(imageManager.getImage("action-method.png"));
        Label actionMethodComment = new Label("Обозначает метод, помеченный аннотацией(ями) @ActionTitle");
        actionMethodComment.setWrapText(true);
        actionMethodComment.setTextAlignment(TextAlignment.JUSTIFY);
        actionMethodComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(actionMethod, 0, 5);
        this.grid.add(actionMethodComment, 1, 5);

        ImageView htmlElement = new ImageView(imageManager.getImage("html-element.png"));
        Label htmlElementComment = new Label("Обозначает HtmlElement и его потомков (? extends HtmlElement). Элементы, помеченные этой иконкой - панели (в основном), содержащие другие элементы");
        htmlElementComment.setWrapText(true);
        htmlElementComment.setTextAlignment(TextAlignment.JUSTIFY);
        htmlElementComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(htmlElement, 0, 6);
        this.grid.add(htmlElementComment, 1, 6);

        ImageView listElement = new ImageView(imageManager.getImage("list-elements.png"));
        Label listElementComment = new Label("Обозначает список элементов");
        listElementComment.setWrapText(true);
        listElementComment.setTextAlignment(TextAlignment.JUSTIFY);
        listElementComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(listElement, 0, 7);
        this.grid.add(listElementComment, 1, 7);

        ImageView typifiedElement = new ImageView(imageManager.getImage("typified-element.png"));
        Label typifiedElementComment = new Label("Обозначает типизированный yandex-элемент, например, Button, TextBlock, Link и т.п.");
        typifiedElementComment.setWrapText(true);
        typifiedElementComment.setTextAlignment(TextAlignment.JUSTIFY);
        typifiedElementComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(typifiedElement, 0, 8);
        this.grid.add(typifiedElementComment, 1, 8);

        ImageView webElement = new ImageView(imageManager.getImage("web-element.png"));
        Label webElementComment = new Label("Обозначает обычный web-элемент");
        webElementComment.setWrapText(true);
        webElementComment.setTextAlignment(TextAlignment.JUSTIFY);
        webElementComment.setMaxWidth(MAX_COMMENT_WIDTH);
        this.grid.add(webElement, 0, 9);
        this.grid.add(webElementComment, 1, 9);

    }

}
