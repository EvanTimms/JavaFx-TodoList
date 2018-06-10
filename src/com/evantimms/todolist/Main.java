package com.evantimms.todolist;

import com.evantimms.todolist.datamodel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("todo list");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }

    //Used on application startup for loading data from text file
    @Override
    public void init() throws Exception {
        try {
            TodoData.getInstance().loadTodoItems();
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception{
        try{
            //create singleton class to store items in file
            TodoData.getInstance().storeTodoItems();

        } catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
