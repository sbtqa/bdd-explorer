package ru.sbt.qa.dcb.bddexplorer.panes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by SBT-Razuvaev-SV on 02.01.2017.
 */
@Component
public class ElementPropertiesPane extends GridPane {

    public void setProperties(Map<String, String> properties) {
        this.getChildren().clear();
        if (properties != null) {
            int currentRowIndex = 0;
            for (String key : properties.keySet()) {
                Label keyLabel = new Label(key);
                keyLabel.setId("element-property-key-label");
                this.add(keyLabel, 1, currentRowIndex);
                TextField textField = new TextField(properties.get(key));
                textField.setEditable(false);
                textField.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(textField, Priority.ALWAYS);
                textField.setId("element-property-value-text-field");
                this.add(textField, 2, currentRowIndex++);
            }
        }
    }

}
