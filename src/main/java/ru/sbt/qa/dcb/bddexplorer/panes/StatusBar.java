package ru.sbt.qa.dcb.bddexplorer.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@Component
public class StatusBar extends HBox {

    private Label status;

    @PostConstruct
    private void init() {
        this.setId("status-bar");
        status = new Label();
        this.getChildren().add(status);
    }

    public void setStatusMessage(String message) {
        status.setText(message);
    }

}
