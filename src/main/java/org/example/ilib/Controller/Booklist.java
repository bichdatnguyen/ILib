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

    protected List<Book> RecentlyAddedBookList() throws SQLException, IOException {
        DBConnection db = DBConnection.getInstance();

        return db.getRecentlyBooks(9);
    }
}