package main.util.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import main.Main;
import main.util.BulletJournalEntry;
import main.util.ScheduleItem;
import main.util.TODOListData;

import java.io.*;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static void createScheduleFile(TableView<ScheduleItem> table, StringBuilder fileContent, File fileToSave) {
        for (ScheduleItem item : table.getItems()) {
            fileContent.append("Task/Event: " + item.getTask() + "\n");
            fileContent.append("      From: " + item.getFromTime() + "\n");
            fileContent.append("        To: " + item.getToTime() + "\n");
            fileContent.append(" Complete?: " + item.getTaskCompleted() + "\n\n\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));

            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createBulletJournalFile(VBox container, ListView<String> keyListView, StringBuilder fileContent, File fileToSave) {
        ArrayList<TextField> fields = new ArrayList<TextField>();
        ArrayList<TextArea> textAreas = new ArrayList<TextArea>();

        fileContent.append("Keys:\n");

        for (String entry : keyListView.getItems()) {
            fileContent.append(entry + "\n");
        }

        for (Node node : container.getChildren()) {
            Pane pane = ((Pane) node);

            if (pane instanceof VBox) {
                for (Node n : pane.getChildren()) {
                    if (n instanceof TextField) {
                        TextField field = ((TextField) n);

                        if (!field.getText().isEmpty()) {
                            fields.add(field);
                        }
                    } else if (n instanceof TextArea) {
                        TextArea area = ((TextArea) n);

                        if (!area.getText().isEmpty()) {
                            textAreas.add(area);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < fields.size(); i++) {
            fileContent.append("Page: " + fields.get(i).getText() + "\n");
            fileContent.append("Content: " + textAreas.get(i).getText() + "\n\n\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));

            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTodoFile(VBox todoContainer, StringBuilder fileContent, File fileToSave) {
        ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
        ArrayList<TextField> textFields = new ArrayList<TextField>();

        for (Node node : todoContainer.getChildren()) {
            Pane pane = ((Pane) node);

            if (pane instanceof HBox) {
                for (Node n : pane.getChildren()) {
                    if (n instanceof CheckBox) {
                        checkboxes.add(((CheckBox) n));
                    } else if (n instanceof TextField) {
                        textFields.add(((TextField) n));
                    }
                }
            }
        }

        int size = checkboxes.size();

        for (int i = 0; i < checkboxes.size(); i++) {
            if (i < size) {
                if (!textFields.get(i).getText().isEmpty()) {
                    fileContent.append("TODO #" + (i + 1) + "\n");
                    fileContent.append("Task: " + textFields.get(i).getText() + "\n");
                    fileContent.append("Done: " + checkboxes.get(i).isSelected() + "\n\n\n");
                } else {
                    textFields.remove(i);
                    checkboxes.remove(i);
                    size--;
                }
            } else {
                break;
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));

            writer.write(fileContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openApplicationData(FileChooser scheduleChooser, FileChooser bulletJournalChooser,
                                           FileChooser todoChooser, TabPane tabPane,
                                           TableView<ScheduleItem> scheduleTable, VBox container,
                                           ListView<String> keyListView, VBox todoContainer) {
        FileChooser[] choosers = { scheduleChooser, bulletJournalChooser, todoChooser };

        for (int i = 0; i < choosers.length; i++) {
            choosers[i].setTitle("Open " + tabPane.getTabs().get(i).getText() + " Data");
            choosers[i].getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON File", "*.json"));
        }

        File scheduleFile = scheduleChooser.showOpenDialog(Main.getStage());
        File bulletJournalFile = bulletJournalChooser.showOpenDialog(Main.getStage());
        File todoFile = todoChooser.showOpenDialog(Main.getStage());

        if (scheduleFile != null && bulletJournalFile != null && todoFile != null) {
            StringBuilder scheduleContent = new StringBuilder();
            StringBuilder bulletJournalContent = new StringBuilder();
            StringBuilder todoContent = new StringBuilder();

            try {
                BufferedReader scheduleReader = new BufferedReader(new FileReader(scheduleFile));
                BufferedReader bulletJournalReader = new BufferedReader(new FileReader(bulletJournalFile));
                BufferedReader todoReader = new BufferedReader(new FileReader(todoFile));

                String scheduleLine;
                String bulletJournalLine;
                String todoLine;

                while ((scheduleLine = scheduleReader.readLine()) != null) {
                    scheduleContent.append(scheduleLine);
                }

                while ((bulletJournalLine = bulletJournalReader.readLine()) != null) {
                    bulletJournalContent.append(bulletJournalLine);
                }

                while ((todoLine = todoReader.readLine()) != null) {
                    todoContent.append(todoLine);
                }

                scheduleReader.close();
                bulletJournalReader.close();
                todoReader.close();
            } catch (IOException ex) {
                Alert readAlert = new Alert(Alert.AlertType.ERROR);
                readAlert.setTitle("An Error Has Occurred!");
                readAlert.setContentText("Sorry, an error has occurred whilst reading file contents. Please Try Again!");
                readAlert.showAndWait();
                ex.printStackTrace();
            }

            String scheduleJSON = scheduleContent.toString();
            String bulletJournalJSON = bulletJournalContent.toString();
            String todoJSON = todoContent.toString();

            Type scheduleType = new TypeToken<ArrayList<ScheduleItem>>() {
            }.getType();

            Type bulletJournalType = new TypeToken<BulletJournalEntry>() {
            }.getType();

            Type todoType = new TypeToken<TODOListData>() {
            }.getType();

            List<ScheduleItem> items = new Gson().fromJson(scheduleJSON, scheduleType);
            BulletJournalEntry entry = new Gson().fromJson(bulletJournalJSON, bulletJournalType);
            TODOListData data = new Gson().fromJson(todoJSON, todoType);

            // Schedule
            scheduleTable.getItems().addAll(items);

            // Bullet Journal
            ArrayList<String> pageNumbers = entry.getPage();
            ArrayList<String> keys = entry.getKeys();
            ArrayList<String> pageContent = entry.getPageContent();
            ArrayList<TextField> fields = new ArrayList<TextField>();
            ArrayList<TextArea> textAreas = new ArrayList<TextArea>();

            keyListView.getItems().addAll(keys);

            for (Node node : container.getChildren()) {
                Pane pane = ((Pane) node);

                if (pane instanceof VBox) {
                    for (Node n : pane.getChildren()) {
                        if (n instanceof TextField) {
                            TextField field = ((TextField) n);

                            fields.add(field);
                        } else if (n instanceof TextArea) {
                            TextArea area = ((TextArea) n);

                            textAreas.add(area);
                        }
                    }
                }
            }

            for (int i = 0; i < pageNumbers.size(); i++) {
                fields.get(i).setText(pageNumbers.get(i));
                textAreas.get(i).setText(pageContent.get(i));
            }

            // to do  List
            ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
            ArrayList<TextField> textFields = new ArrayList<TextField>();
            ArrayList<String> checked = data.getDoneBox();
            ArrayList<String> todoEntryContent = data.getTodo();

            for (Node node : todoContainer.getChildren()) {
                Pane pane = ((Pane) node);

                if (pane instanceof HBox) {
                    for (Node n : pane.getChildren()) {
                        if (n instanceof CheckBox) {
                            checkboxes.add(((CheckBox) n));
                        } else if (n instanceof TextField) {
                            textFields.add(((TextField) n));
                        }
                    }
                }
            }

            for (int i = 0; i < todoEntryContent.size(); i++) {
                if(checked.get(i).equals("Yes")) {
                    checkboxes.get(i).setSelected(true);
                } else {
                    checkboxes.get(i).setSelected(false);
                }

                textFields.get(i).setText(todoEntryContent.get(i));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An Error has Occurred!");
            alert.setContentText("Sorry, an error occurred whilst opening the files! Please try again later!");
            alert.showAndWait();
        }
    }

    public static void saveApplicationData(TableView<ScheduleItem> scheduleTable, VBox todoContainer,
                                           ListView<String> keyListView, VBox container) {
        ArrayList<ScheduleItem> item = new ArrayList<ScheduleItem>();

        item.addAll(scheduleTable.getItems());

        TODOListData data = new TODOListData();

        ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>();
        ArrayList<TextField> textFields = new ArrayList<TextField>();
        ArrayList<String> checked = new ArrayList<String>();

        for (Node node : todoContainer.getChildren()) {
            Pane pane = ((Pane) node);

            if (pane instanceof HBox) {
                for (Node n : pane.getChildren()) {
                    if (n instanceof CheckBox) {
                        checkboxes.add(((CheckBox) n));
                    } else if (n instanceof TextField) {
                        textFields.add(((TextField) n));
                    }
                }
            }
        }

        int size = checkboxes.size();

        for (int i = 0; i < checkboxes.size(); i++) {
            if (i < size) {
                if (textFields.get(i).getText().isEmpty()) {
                    textFields.remove(i);
                    checkboxes.remove(i);
                    size--;
                }
            } else {
                break;
            }
        }

        for (CheckBox box : checkboxes) {
            if (box.isSelected()) {
                checked.add("Yes");
            } else {
                checked.add("No");
            }
        }

        data.setDoneBox(checked);

        ArrayList<String> entries = new ArrayList<String>();

        for (TextField field : textFields) {
            entries.add(field.getText());
        }

        data.setTodo(entries);

        ArrayList<TextField> fields = new ArrayList<TextField>();
        ArrayList<TextArea> textAreas = new ArrayList<TextArea>();
        ArrayList<String> fieldText = new ArrayList<String>();
        ArrayList<String> areaText = new ArrayList<String>();
        ArrayList<String> keys = new ArrayList<String>();

        for (String s : keyListView.getItems()) {
            keys.add(s);
        }

        for (Node node : container.getChildren()) {
            Pane pane = ((Pane) node);

            if (pane instanceof VBox) {
                for (Node n : pane.getChildren()) {
                    if (n instanceof TextField) {
                        TextField field = ((TextField) n);

                        if (!field.getText().isEmpty()) {
                            fields.add(field);
                        }
                    } else if (n instanceof TextArea) {
                        TextArea area = ((TextArea) n);

                        if (!area.getText().isEmpty()) {
                            textAreas.add(area);
                        }
                    }
                }
            }
        }

        for (TextField tf : fields) {
            fieldText.add(tf.getText());
        }

        for (TextArea ta : textAreas) {
            areaText.add(ta.getText());
        }

        BulletJournalEntry entry = new BulletJournalEntry();
        entry.setPage(fieldText);
        entry.setPageContent(areaText);
        entry.setKeys(keys);

        File scheduleFile = new File("schedule.json");
        File bulletJournalFile = new File("bullet_journal.json");
        File todoFile = new File("todo.json");

        String scheduleJSON = new Gson().toJson(item);
        String bulletJournalJSON = new Gson().toJson(entry);
        String todoJSON = new Gson().toJson(data);

        try {
            BufferedWriter scheduleWriter = new BufferedWriter(new FileWriter(scheduleFile));
            BufferedWriter journalWriter = new BufferedWriter(new FileWriter(bulletJournalFile));
            BufferedWriter todoWriter = new BufferedWriter(new FileWriter(todoFile));

            scheduleWriter.write(scheduleJSON);
            journalWriter.write(bulletJournalJSON);
            todoWriter.write(todoJSON);

            scheduleWriter.close();
            journalWriter.close();
            todoWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
