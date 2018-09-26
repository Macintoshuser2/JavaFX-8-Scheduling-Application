package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("schedule_app.fxml"));
        primaryStage.setTitle("Schedule");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();

        primaryStage.getScene().getStylesheets().add("main/dark.css");
    }

    public static void close() {
        stage.close();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
