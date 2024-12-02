package org.example.ilib.Controller;


import org.example.ilib.Processor.Book;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Booklist {
    private static Booklist instance;
    protected List<Book> TopBookList = new ArrayList<>();
    protected List<Book> RecentlyBookList = new ArrayList<>();
    protected List<Book> RecommendBookList = new ArrayList<>();

    /**  getInstance method in singleton design pattern.
     * @return Booklist instance
     */
    public static Booklist getInstance() {
        if(instance == null){
            instance = new Booklist();
        }
        return instance;
    }

    /**
     * private constructor.
     */
    private Booklist(){
        try{
            if (TopBookList.isEmpty()) {
                TopBookList = addTopBookList();
            }
            if (RecentlyBookList.isEmpty()) {
               RecentlyBookList = addRecentlyBookList();
            }
            if (RecommendBookList.isEmpty()) {
               RecommendBookList = addRecommendForYou();
            }
        } catch (SQLException | IOException e) {
           e.printStackTrace();
        }
   }

    /** add top averateRating books in database to list.
     * @return list of highest rating books
     * @throws SQLException throw sql exception
     * @throws IOException throw IO exception
     */
    private List<Book> addTopBookList() throws SQLException, IOException {
        return DBConnection.getInstance().getTopBooks(9);
    }

    /** add recently add books in database to list.
     * @return list of recently add books.
     * @throws SQLException throw sql exception
     * @throws IOException throw IO exception
     */
    private List<Book> addRecentlyBookList() throws SQLException, IOException {
        return DBConnection.getInstance().getRecentlyBooks(9);
    }

    /** add books recommend for user to list.
     * @return list of recommend books
     * @throws SQLException throw sql exception
     * @throws IOException throw IO exception
     */
    private List<Book> addRecommendForYou() throws SQLException, IOException {
        return DBConnection.getInstance().getRecommendedBooks(9);
    }
}