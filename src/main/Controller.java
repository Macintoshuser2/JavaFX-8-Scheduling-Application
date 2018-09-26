package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import main.prompt.AddKeyPrompt;
import main.util.ScheduleItem;
import main.util.file.FileUtil;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML public TableView<ScheduleItem> scheduleTable;

    @FXML public TableColumn<ScheduleItem, String> taskColumn;
    @FXML public TableColumn<ScheduleItem, String> fromColumn;
    @FXML public TableColumn<ScheduleItem, String> toColumn;
    @FXML public TableColumn<ScheduleItem, String> completeColumn;

    @FXML public TextField taskTitleField;
    @FXML public TextField fromTimeField;
    @FXML public TextField toTimeField;

    @FXML public ComboBox<String> completedComboBox;

    @FXML public ListView<String> keyListView;

    @FXML public VBox container;
    @FXML public VBox todoContainer;

    @FXML public TabPane tabPane;

    @FXML public MenuItem saveAppData;
    @FXML public MenuItem openAppData;
    @FXML public MenuItem saveAllAppData;

    @FXML public MenuBar menuBar;

    private ObservableList<ScheduleItem> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        container.getStyleClass().add("todo-container");

        if (!(System.getProperty("os.name").startsWith("Mac"))) {
            saveAppData.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
            openAppData.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
            saveAllAppData.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN,
                                          KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        } else {
            saveAppData.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,
                                       KeyCombination.SHIFT_DOWN));

            openAppData.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN,
                                       KeyCombination.SHIFT_DOWN));

            saveAllAppData.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN,
                                       KeyCombination.ALT_DOWN, KeyCombination.SHIFT_DOWN));
        }

        if (System.getProperty("os.name").startsWith("Mac")) {
            menuBar.setUseSystemMenuBar(true);
        }

        completedComboBox.getItems().addAll("Yes", "No");

        taskColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("task"));
        fromColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("fromTime"));
        toColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("toTime"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("taskCompleted"));

        taskColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fromColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        toColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        completeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        scheduleTable.setEditable(true);

        taskColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ScheduleItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ScheduleItem, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setTask(event.getNewValue());
            }
        });

        fromColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ScheduleItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ScheduleItem, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setFromTime(event.getNewValue());
            }
        });

        toColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ScheduleItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ScheduleItem, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setToTime(event.getNewValue());
            }
        });

        completeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ScheduleItem, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ScheduleItem, String> event) {
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setTaskCompleted(event.getNewValue());
            }
        });
    }

    public void onAddTaskButtonClicked() {
        String errorMessage = "";

        if (taskTitleField.getText().isEmpty()) {
            errorMessage += "You Must Enter a Title for Your Task!\n";
        }

        if (fromTimeField.getText().isEmpty()) {
            errorMessage += "You Must Enter a From Time for Your Task!\n";
        }

        if (toTimeField.getText().isEmpty()) {
            errorMessage += "You Must Enter a To Time for Your Task!\n";
        }

        if (completedComboBox.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "You Must Select Whether or not Your Task Has Been Completed!";
        }

        if (!errorMessage.isEmpty() || !errorMessage.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An Error Has Occurred!");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        } else {
            data.add(new ScheduleItem(taskTitleField.getText(), fromTimeField.getText(), toTimeField.getText(),
                     completedComboBox.getSelectionModel().getSelectedItem()));

            scheduleTable.setItems(data);

            taskTitleField.clear();
            fromTimeField.clear();
            toTimeField.clear();
            completedComboBox.getSelectionModel().select(null);
        }
    }

    public void onClearButtonClicked() {
        taskTitleField.clear();
        fromTimeField.clear();
        toTimeField.clear();
        completedComboBox.getSelectionModel().select(null);
    }

    public void onAddPageButtonClicked() {
        VBox pageInputs = new VBox();
        pageInputs.setAlignment(Pos.CENTER);
        pageInputs.setPrefHeight(200);
        pageInputs.setPrefWidth(642);
        pageInputs.setSpacing(5);

        TextField pageNumberField = new TextField();
        pageNumberField.setAlignment(Pos.CENTER);
        pageNumberField.setPrefSize(250, 27);
        pageNumberField.setMinSize(250, 27);
        pageNumberField.setMaxSize(250, 27);
        pageNumberField.setPromptText("Page Number:");
        VBox.setVgrow(pageNumberField, Priority.ALWAYS);
        VBox.setMargin(pageNumberField, new Insets(10, 10, 5, 10));
        pageNumberField.setFont(new Font(14));

        TextArea contentArea = new TextArea();
        contentArea.setPrefSize(450, 235);
        contentArea.setMinSize(450, 235);
        contentArea.setMaxSize(450, 235);
        contentArea.setPromptText("Page Content");
        contentArea.setWrapText(true);
        contentArea.setFont(new Font(18));

        pageInputs.getChildren().addAll(pageNumberField, contentArea);
        container.getChildren().add(pageInputs);
    }

    public void onAddKeyButtonPressed() {
        AddKeyPrompt.displayPrompt(keyListView);
    }

    public void onSaveDataClicked() {
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Save " + tabPane.getSelectionModel().getSelectedItem().getText() + " Data");
        saveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt"));

        File fileToSave = saveChooser.showSaveDialog(Main.getStage());

        if (fileToSave != null) {
            switch (tabPane.getSelectionModel().getSelectedItem().getText()) {
                case "Schedule":
                    FileUtil.createScheduleFile(scheduleTable, new StringBuilder(), fileToSave);

                    break;
                case "Bullet Journal":
                    FileUtil.createBulletJournalFile(container, keyListView, new StringBuilder(), fileToSave);

                    break;
                case "TODO List":
                    FileUtil.createTodoFile(todoContainer, new StringBuilder(), fileToSave);

                    break;
            }
        }
    }

    public void onOpenDataClicked() {
        FileUtil.openApplicationData(new FileChooser(), new FileChooser(), new FileChooser(), tabPane, scheduleTable,
                                     container, keyListView,todoContainer);
    }

    public void onSaveAllDataClicked() {
        FileUtil.saveApplicationData(scheduleTable, todoContainer, keyListView, container);
    }

    public void onCloseClicked() {
        Main.close();
    }
}
