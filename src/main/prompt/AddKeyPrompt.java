package main.prompt;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddKeyPrompt {
    public static void displayPrompt(ListView<String> keyListView) {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        HBox buttons = new HBox(10);
        TextField keyIconField = new TextField();
        TextField meaningField = new TextField();
        Button addButton = new Button("Add Key");
        Button doneButton = new Button("Done");
        Button cancelButton = new Button("Cancel");

        keyIconField.setPromptText("Key Icon");
        keyIconField.setPrefSize(75, 27);
        keyIconField.setMinSize(75, 27);
        keyIconField.setMaxSize(75, 27);

        meaningField.setPromptText("Key Meaning");
        meaningField.setPrefSize(175, 27);
        meaningField.setMinSize(175, 27);
        meaningField.setMaxSize(175, 27);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 10, 10, 10));

        buttons.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(addButton, doneButton, cancelButton);
        root.getChildren().addAll(keyIconField, meaningField, buttons);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String entry = "";

                if ((keyIconField.getText().isEmpty() || meaningField.getText().isEmpty()) || (keyIconField.getText().isEmpty() && meaningField.getText().isEmpty())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("An Error has Occurred!");
                    alert.setContentText("Both Fields Must be Filled in!");
                    alert.showAndWait();
                } else {
                    entry = keyIconField.getText() + "\t" + meaningField.getText();
                    keyListView.getItems().add(entry);
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!keyListView.getItems().isEmpty()) {
                    keyListView.getItems().clear();
                    stage.close();
                } else {
                    stage.close();
                }
            }
        });

        doneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        stage.setTitle("Add Key");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
