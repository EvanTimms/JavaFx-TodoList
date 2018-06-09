package com.evantimms.todolist;
import com.evantimms.todolist.datamodel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private List<TodoItem> todoItems;
    @FXML
    private ListView todolistView;
    @FXML
    private TextArea desTextArea;
    @FXML
    private Label deadlineLabel;

    @FXML
    public void initialize(){
        TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John"
        , LocalDate.of(2018, Month.JUNE, 25));
        TodoItem item2 = new TodoItem("Sell Car", "Sell the VW"
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

        // adding listener for selecting item detailed text. Can also use lambda expressions
        todolistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if(newValue != null){
                    TodoItem item = (TodoItem) todolistView.getSelectionModel().getSelectedItem();
                    desTextArea.setText(item.getExtendedDes());
                    //Using DateTimeFormatter class for better date displaying
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyy");
                    deadlineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });

        todolistView.getItems().setAll(todoItems);
        todolistView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //selecting first item to display
        todolistView.getSelectionModel().selectFirst();


    }

    @FXML
    public void handleClickListView(){
        TodoItem item = (TodoItem) todolistView.getSelectionModel().getSelectedItem();
        desTextArea.setText(item.getExtendedDes());
        deadlineLabel.setText(item.getDeadline().toString());
    }

}
