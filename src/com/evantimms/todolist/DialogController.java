package com.evantimms.todolist;

import com.evantimms.todolist.datamodel.TodoData;
import com.evantimms.todolist.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;


import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDes;
    @FXML
    private TextArea extendedDes;
    @FXML
    private DatePicker deadlinePicker;

    @FXML
    public TodoItem processResults(){
        String title = shortDes.getText().trim();
        String details= extendedDes.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();

        TodoItem item = new TodoItem(title,details,deadline);
        TodoData.getInstance().addTodoItem(item);
        return item;
    }
    @FXML
    public void loadData(TodoItem item){
        shortDes.setText(item.getShortDes());
        extendedDes.setText(item.getExtendedDes());
        deadlinePicker.setValue(item.getDeadline());
    }

}
