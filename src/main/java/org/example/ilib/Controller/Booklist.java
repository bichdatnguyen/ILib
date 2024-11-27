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
    protected List<Book> CategoriesBookList = new ArrayList<>();
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
        } catch (SQLException e) {
           e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
   }


    private List<Book> addTopBookList() throws SQLException, IOException {
        DBConnection db = DBConnection.getInstance();
        List<Book> recentlyBooks = db.getRecentlyBooks(9);
        if (recentlyBooks == null || recentlyBooks.isEmpty()) {
            System.out.println("Không có sách gần đây nào.");
        }
        return db.getRecentlyBooks(5);
    }

    protected List<Book> addRecentlyBookList() throws SQLException, IOException {
        DBConnection db = DBConnection.getInstance();
        return db.getRecentlyBooks(9);
    }
}