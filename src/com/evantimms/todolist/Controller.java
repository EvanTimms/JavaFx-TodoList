package com.evantimms.todolist;
import com.evantimms.todolist.datamodel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {

    private List<TodoItem> todoItems;
    @FXML
    private ListView todolistView;
    @FXML
    private TextArea desTextArea;
    @FXML
    private Label deadlineLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;

    @FXML
    public void initialize(){
        // Initializing context menu
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = (TodoItem) todolistView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showEditItemDialog();
            }
        });

        //Adding to context menu
        listContextMenu.getItems().add(deleteMenuItem);
        listContextMenu.getItems().add(editMenuItem);

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

        todolistView.setItems(TodoData.getInstance().getTodoItems());
        todolistView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //selecting first item to display
        todolistView.getSelectionModel().selectFirst();

        todolistView.setCellFactory(new Callback<ListView, ListCell>() {
            //here I am implementing an anonymous class using the ListCell interface
            //this is part of the JavaFx api
            @Override
            public ListCell call(ListView param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                    //this will run whenever the listview wishes to paint a cell
                    @Override
                    protected void updateItem(TodoItem item, boolean empty){
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }else{
                            setText(item.getShortDes());
                            //Will set the cell text to red if deadline is todays date
                            if(item.getDeadline().equals(LocalDate.now())){
                                setTextFill(Color.RED);
                            } else if(item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.BROWN);
                            } else if(item.getDeadline().isBefore(LocalDate.now())){
                                setTextFill(Color.RED);
                            }
                        }

                    }
                };
                //this uses a anonymous function to set the context menu
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) ->{
                            if(isNowEmpty){
                                cell.setContextMenu(null);
                            }else{
                                cell.setContextMenu(listContextMenu);
                            }
                        });

                return cell;
            }
        });
    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        //this is so the user cannot use another part of the ui without first interacting with the dialog
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("New Todo Item");
        dialog.setHeaderText("Use this dialog to create a new todo item.");

        //instantiating fxmloader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try {
            //getting fxml from dialog fxml and setting it
            dialog.getDialogPane().setContent(loader.load());

        } catch (IOException e){
            System.out.println("Couldnt Load Dialog");
            e.printStackTrace();
            return;
        }

        // creating cancel and ok buttons for dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        //showing dialog, generally want to use show and wait for dialogs
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            DialogController controller = loader.getController();
            TodoItem newItem = controller.processResults();
            todolistView.getSelectionModel().select(newItem);
        }
    }

    //TODO: Add showEditDialog
    public void showEditItemDialog(){
        //similar setup to above method
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit Todo Item");

        //getting item
        TodoItem item = (TodoItem) todolistView.getSelectionModel().getSelectedItem();
        dialog.setHeaderText("Use this dialog to edit " + item.getShortDes());

        //instantiating fxmloader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try {
            //getting fxml from dialog fxml and setting it
            dialog.getDialogPane().setContent(loader.load());

        } catch (IOException e){
            System.out.println("Couldnt Load Dialog");
            e.printStackTrace();
            return;
        }

        // creating cancel and ok buttons for dialog
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        //Adding item data
        DialogController controller = loader.getController();
        controller.loadData(item);

        //showing dialog, generally want to use show and wait for dialogs
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            TodoData.getInstance().deleteTodoItem(item);
            item = controller.processResults();
            todolistView.getSelectionModel().select(item);
        }

    }

    public void handleKeyPressed(KeyEvent event){
        TodoItem selectedItem = (TodoItem) todolistView.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            if(event.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            } else if(event.getCode().equals(KeyCode.E)){
                showEditItemDialog();
            }
        }
    }



    public void deleteItem(TodoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Delete item: " + item.getShortDes());
        alert.setContentText("Are you sure? Press OK to confirm.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }

}
