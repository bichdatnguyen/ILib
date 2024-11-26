package org.example.ilib.Controller;


import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Booklist {
    public static final int TOP_BOOK = 1;
    public static final int CATEGORIES_BOOK = 2;
    public static final int RECECNTLYADDED_BOOK = 3;

    protected List<Book> bookList = new ArrayList<Book>();

    protected Booklist(int Phan_loai) throws SQLException, IOException {
        if (Phan_loai == TOP_BOOK) {
            bookList = addTopBookList();
        } else if (Phan_loai == CATEGORIES_BOOK) {
            bookList = CategoriesBookList("Philosophy");
        } else if (Phan_loai == RECECNTLYADDED_BOOK) {
            bookList = RecentlyAddedBookList();
        }
    }

    private List<Book> addTopBookList() throws SQLException, IOException {
        List<Book> TopBookList = new ArrayList<>();

        DBConnection db = DBConnection.getInstance();
        GoogleBooksAPI gg = new GoogleBooksAPI();

        List<String> ids = db.getTopBooks(4);

        for (String id : ids) {
            TopBookList.add(gg.getBooksByID(id));
        }

        return TopBookList;
    }

    private List<Book> CategoriesBookList(String category) throws SQLException, IOException {
        List<Book>CatebookList = new ArrayList<>();

        DBConnection db = DBConnection.getInstance();
        GoogleBooksAPI gg = new GoogleBooksAPI();

        List<String> ids = db.getTopCategories(category);

        for (String id : ids) {
            CatebookList.add(gg.getBooksByID(id));
        }

        return CatebookList;
    }

    protected List<Book> RecentlyAddedBookList() throws SQLException, IOException {
        List<Book> RecentlyBookList = new ArrayList<>();

        DBConnection db = DBConnection.getInstance();
        GoogleBooksAPI gg = new GoogleBooksAPI();

        List<String> ids = db.getRecentlyBooks(9);

        for (String id : ids) {
            RecentlyBookList.add(gg.getBooksByID(id));
        }

        return RecentlyBookList;
    }
}