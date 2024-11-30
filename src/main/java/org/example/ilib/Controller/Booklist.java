package org.example.ilib.Controller;


import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Booklist {

    private static Booklist instance;
    protected List<Book> TopBookList = new ArrayList<>();
    protected List<Book> RecentlyBookList = new ArrayList<>();

    public static Booklist getInstance() {
        if(instance == null){
            instance = new Booklist();
        }
        return instance;
    }
   private Booklist(){
        try{
            if(TopBookList.isEmpty()){
                TopBookList = addTopBookList();
            }
            if(RecentlyBookList.isEmpty()){
               RecentlyBookList = addRecentlyBookList();
            }
        } catch (SQLException | IOException e) {
           e.printStackTrace();
        }
   }


    private List<Book> addTopBookList() throws SQLException, IOException {
        return DBConnection.getInstance().getTopBooks(9);
    }

    protected List<Book> addRecentlyBookList() throws SQLException, IOException {
        return DBConnection.getInstance().getRecentlyBooks(9);
    }
}