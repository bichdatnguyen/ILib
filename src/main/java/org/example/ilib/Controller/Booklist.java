package org.example.ilib.Controller;

import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Booklist {
    private static Booklist instance;
    private final List<Book> TopBookList = new ArrayList<>();
    private final List<Book> RecentlyBookList = new ArrayList<>();
    private final List<Book> RecommendBookList = new ArrayList<>();
    private final Map<String, Book> BookMap = new HashMap<>(); // Để tìm kiếm và quản lý sách dễ dàng hơn

    /**
     * getInstance method in singleton design pattern.
     * @return Booklist instance
     */
    public static Booklist getInstance() {
        if (instance == null) {
            instance = new Booklist();
        }
        return instance;
    }

    /**
     * private constructor.
     */
    private Booklist() {
        try {
            addBooksToLists();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load books into lists and map.
     */
    private void addBooksToLists() throws SQLException, IOException {
        addBooksToList(addTopBookList(), TopBookList);
        addBooksToList(addRecentlyBookList(), RecentlyBookList);
        addBooksToList(addRecommendForYou(), RecommendBookList);
    }

    private void addBooksToList(List<Book> source, List<Book> target) {
        for (Book book : source) {
            target.add(book);
            BookMap.put(book.getId(), book); // Map để truy xuất nhanh
        }
    }

    public List<Book> getTopBookList() {
        return TopBookList;
    }

    public List<Book> getRecentlyBookList() {
        return RecentlyBookList;
    }

    public List<Book> getRecommendBookList() {
        return RecommendBookList;
    }

    public Book getBookById(String id) {
        return BookMap.get(id); // Tìm kiếm nhanh
    }

    public void updateBook(String id, Book updatedBook) {
        if (BookMap.containsKey(id)) {
            Book oldBook = BookMap.get(id);
            updateBookInList(TopBookList, oldBook, updatedBook);
            updateBookInList(RecentlyBookList, oldBook, updatedBook);
            updateBookInList(RecommendBookList, oldBook, updatedBook);
            BookMap.put(id, updatedBook);
        } else {
            throw new IllegalArgumentException("Book with ID " + id + " not found.");
        }
    }

    private void updateBookInList(List<Book> list, Book oldBook, Book updatedBook) {
        int index = list.indexOf(oldBook);
        if (index >= 0) {
            list.set(index, updatedBook);
        }
    }

    private List<Book> addTopBookList() throws SQLException, IOException {
        return DBConnection.getInstance().getTopBooks(8);
    }

    private List<Book> addRecentlyBookList() throws SQLException, IOException {
        return DBConnection.getInstance().getRecentlyBooks(9);
    }

    private List<Book> addRecommendForYou() throws SQLException, IOException {
        return DBConnection.getInstance().getRecommendedBooks(9);
    }

    public void updateBookQuantity(String bookId, int newQuantity) {
        updateBookQuantityInList(TopBookList, bookId, newQuantity);
        updateBookQuantityInList(RecentlyBookList, bookId, newQuantity);
        updateBookQuantityInList(RecommendBookList, bookId, newQuantity);
    }

    private void updateBookQuantityInList(List<Book> bookList, String bookId, int newQuantity) {
        for (Book book : bookList) {
            if (book.getId().equals(bookId)) {
                book.setQuantity(newQuantity);
                break;
            }
        }
    }

}
