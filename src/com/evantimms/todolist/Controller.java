package com.evantimms.todolist;
import com.evantimms.todolist.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private List<TodoItem> todoItems;
    @FXML
    private ListView todolistView;
    @FXML
    private TextArea desTextArea;

    @FXML
    public void initialize(){
        TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John"
        , LocalDate.of(2018, Month.JUNE, 25));
        TodoItem item2 = new TodoItem("Sell Car", "Sell the VW for something less greasy"
                , LocalDate.of(2018, Month.AUGUST, 7));
        TodoItem item3 = new TodoItem("Fathers Day", "Celebrate Fathers Day"
                , LocalDate.of(2018, Month.JUNE, 15));
        TodoItem item4 = new TodoItem("Doctors Appointment", "See doctor smith at 123 Main St."
                , LocalDate.of(2018, Month.SEPTEMBER, 11));

        todoItems = new ArrayList<TodoItem>();
        todoItems.add(item1);
        todoItems.add(item2);
        todoItems.add(item3);
        todoItems.add(item4);

        todolistView.getItems().setAll(todoItems);
        todolistView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    @FXML
    public void handleClickListView(){
        TodoItem item = (TodoItem) todolistView.getSelectionModel().getSelectedItem();
        StringBuilder sb = new StringBuilder(item.getExtendedDes());
        sb.append("\n\n\n\n");
        sb.append("Due: ");
        sb.append(item.getDeadline().toString());
        desTextArea.setText(sb.toString());
    }

}
