package com.evantimms.todolist.datamodel;

import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class TodoData {
    private static TodoData instance = new TodoData();
    //File name where data is stored
    private static String filename = "TodoListItems.txt";

    private List<TodoItem> todoItems;
    private DateTimeFormatter formatter;

    public static TodoData getInstance(){
        return instance;
    }

    // setting constructor to private to prevent anyone from instantiating this class
    // this is referred to as a singleton class
    private TodoData(){
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public void loadTodoItems() throws IOException{
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        //generating file reader with specified file path
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try{
            while((input = br.readLine()) != null){
                //splitting to get different pieces of data from class
                String[] itemPieces = input.split("\t");

                String shortDes= itemPieces[0];
                String extendedDes = itemPieces[1];
                String dateString = itemPieces[2];

                //converting date
                LocalDate date = LocalDate.parse(dateString, formatter);
                TodoItem todoItem = new TodoItem(shortDes,extendedDes,date);

                //adding to todoitems list
                todoItems.add(todoItem);
            }
        }finally {
            if(br != null){
                br.close();
            }
        }
    }


    public void storeTodoItems() throws IOException{

        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try{
            //using an iterator class for looping
            Iterator<TodoItem> iter = todoItems.iterator();
            while(iter.hasNext()){
                TodoItem item = iter.next();
                bw.write(String.format("%s\t%s\t,%s",
                        item.getShortDes(),
                        item.getExtendedDes(),
                        item.getDeadline().format(formatter)
                        ));
                bw.newLine();//adds a newline to the text file
            }

        } finally {
            if(bw != null){
                bw.close();
            }
        }
    }
}
