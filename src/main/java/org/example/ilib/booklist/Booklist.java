package org.example.ilib.booklist;

import org.example.ilib.book.Book;
import org.example.ilib.controller.DBConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Booklist {
    private static Booklist instance;
    private final List<Book> TopBookList = new ArrayList<>();
    private final List<Book> RecentlyBookList = new ArrayList<>();
    private final List<Book> RecommendBookList = new ArrayList<>();
    private final Map<String, Book> BookMap = new HashMap<>(); // Để tìm kiếm và quản lý sách dễ dàng hơn

    /**
     * getInstance method in singleton design pattern.
     *
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


    public void updateBook(String oldBookId, Book updatedBook) {
        if (!oldBookId.equals(updatedBook.getId())) {
            updatedBook.setImage(BookMap.get(oldBookId).getImage());
            deleteBook(oldBookId);
            addBook(updatedBook);
        } else {
            updateBookInList(TopBookList, oldBookId, updatedBook);
            updateBookInList(RecentlyBookList, oldBookId, updatedBook);
            updateBookInList(RecommendBookList, oldBookId, updatedBook);
            if (BookMap.containsKey(oldBookId)) {
                BookMap.put(oldBookId, updatedBook);
            }
        }
    }

    private void updateBookInList(List<Book> bookList, String oldBookId, Book updatedBook) {
        updatedBook.setImage(BookMap.get(oldBookId).getImage());
        for (int i = 0; i < bookList.size(); i++) {
            Book book = bookList.get(i);
            if (book.getId().equals(oldBookId)) {
                bookList.set(i, updatedBook);
                break;
            }
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

    public void addBook(Book book) {
        addBookToList(TopBookList, book);
        addBookToList(RecentlyBookList, book);
        addBookToList(RecommendBookList, book);
        if (!BookMap.containsKey(book.getId())) {
            BookMap.put(book.getId(), book);
        }
    }

    private void addBookToList(List<Book> bookList, Book book) {
        if (book != null && !bookList.contains(book)) {
            bookList.add(book);
        }
    }

    public void deleteBook(String bookId) {
        deleteBookFromList(TopBookList, bookId);
        deleteBookFromList(RecentlyBookList, bookId);
        deleteBookFromList(RecommendBookList, bookId);
        BookMap.remove(bookId);
    }

    private void deleteBookFromList(List<Book> bookList, String bookId) {
        bookList.removeIf(book -> book.getId().equals(bookId));
    }

}
