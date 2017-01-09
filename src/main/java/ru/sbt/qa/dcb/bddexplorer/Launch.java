package ru.sbt.qa.dcb.bddexplorer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.sbt.qa.dcb.bddexplorer.managers.ImageManager;
import ru.sbt.qa.dcb.bddexplorer.panes.RootPane;
import ru.sbt.qa.dcb.bddexplorer.panes.StatusBar;
import ru.sbt.qa.dcb.bddexplorer.scanner.ProjectScanner;

/**
 * Created by SBT-Razuvaev-SV on 27.12.2016.
 */
public class Launch extends Application {

    private static Stage primaryStage = null;
    private RootPane rootPane = null;

    @Override
    public void init() throws Exception {
        Ctx.init();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        rootPane = Ctx.get().getBean(RootPane.class);
        Scene scene = new Scene(rootPane, rootPane.getWidth(), rootPane.getHeight());
        scene.getStylesheets().add("styles.css");
        primaryStage.getIcons().add(Ctx.get().getBean(ImageManager.class).getImage("logo.png"));
        primaryStage.setTitle("BDD Explorer [1.0.0 Beta]");
        primaryStage.setScene(scene);
        primaryStage.show();

        Ctx.get().getBean(ProjectScanner.class).loadSourceFiles();
        Ctx.get().getBean(StatusBar.class).setStatusMessage("Application started");
    }

    @Override
    public void stop() throws Exception {
        Ctx.close();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
