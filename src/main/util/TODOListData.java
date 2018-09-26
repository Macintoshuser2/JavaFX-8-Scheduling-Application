package main.util;

import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class TODOListData {
    public ArrayList<String> doneBox;
    public ArrayList<String> todo;

    public TODOListData() {
        this.doneBox = new ArrayList<String>();
        this.todo = new ArrayList<String>();
    }

    public TODOListData(ArrayList<String> doneBox, ArrayList<String> todo) {
        this.doneBox = doneBox;
        this.todo = todo;
    }

    public ArrayList<String> getDoneBox() {
        return doneBox;
    }

    public void setDoneBox(ArrayList<String> doneBox) {
        this.doneBox = doneBox;
    }

    public ArrayList<String> getTodo() {
        return todo;
    }

    public void setTodo(ArrayList<String> todo) {
        this.todo = todo;
    }
}
