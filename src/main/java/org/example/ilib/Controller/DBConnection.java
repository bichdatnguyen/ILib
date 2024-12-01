package org.example.ilib.Controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.collections.ObservableList;
import org.example.ilib.Processor.Account;
import org.example.ilib.Processor.Book;
import org.example.ilib.Processor.CartItem;
import org.example.ilib.Processor.Comment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static java.lang.System.getenv;

// Singleton design pattern
public class DBConnection {
    private static DBConnection instance;
    private HikariDataSource dataSource;
    private final String url
            = "jdbc:mysql://localhost:3306/ilib?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
    private final String userName = System.getenv("userName");
    private final String userPassword = System.getenv("userPassword");

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private DBConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(userPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(1000);
        config.setMinimumIdle(50);
        config.setIdleTimeout(10000);
        config.setMaxLifetime(240000);
        config.setConnectionTimeout(5000);
        config.setLeakDetectionThreshold(5000);

        dataSource = new HikariDataSource(config);
    }
    public static DBConnection getInstance() throws SQLException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public static PreparedStatement createStatement(String sql) throws SQLException {
        return DBConnection.getInstance().getConnection().prepareStatement(sql);
    }

    public void createAccount(String email, String phoneNumber, String fullName,
                              String password, String avatarPath, String role) throws SQLException {
        String sql = "INSERT INTO user (email, phoneNumber, fullName, password, avatarPath, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
       try(PreparedStatement stmt = createStatement(sql)){
           stmt.setString(1, email);
           stmt.setString(2, phoneNumber);
           stmt.setString(3, fullName);
           stmt.setString(4, password);
           stmt.setString(5, avatarPath);
           stmt.setString(6, role);
           stmt.executeUpdate();
       } catch (SQLException e){
           e.printStackTrace();
       }
    }

    public void createVoucher(String email, int discountPercentage) throws SQLException {
        String sql = "INSERT INTO Voucher (Email, discountPercentage) VALUES (?, ?)";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setString(1, email);
            stmt.setInt(2, discountPercentage);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean checkDataExit(String email_)  {
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";

        try (Connection connection1 = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection1.prepareStatement(query)) {

            preparedStatement.setString(1, email_); // Gán giá trị cho dấu ? trong câu lệnh SQL
            try (ResultSet resultSet = preparedStatement.executeQuery()) {  // Thực hiện câu lệnh sau khi đã gán giá trị
                if (resultSet.next()) {
                    int count = resultSet.getInt(1); // Lấy giá trị COUNT từ kết quả
                    return count > 0;
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
        }
        return false;
    }

    public boolean checkDataExit(String email_, String password_) {
        String query = "SELECT COUNT(*) FROM User WHERE email = ? AND password = ?";

        try (Connection connection1 = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection1.prepareStatement(query)) {

            preparedStatement.setString(1, email_);  // Gán giá trị cho dấu ? trong câu lệnh SQL
            preparedStatement.setString(2, password_);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {  // Thực hiện câu lệnh SQL sau khi đã gán giá trị
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);  // Lấy giá trị COUNT từ kết quả
                    return count > 0;
                }
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
        }
        return false;
    }

    public boolean bookExist(String bookID) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM books WHERE bookID = ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public int getQuantity(String bookID) throws SQLException {
        String sql = "SELECT quantityInStock FROM books WHERE bookID = ?";
        PreparedStatement stmt = createStatement(sql);
        stmt.setString(1, bookID);
        try(ResultSet rs = stmt.executeQuery();){
            if (rs.next()) {
                int quantity = rs.getInt(1);
                return quantity;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Book> getTopBooks(int number) throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM books ORDER BY averageRating DESC LIMIT ?";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setInt(1, number);
            List<Book> books = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    String bookID = rs.getString(1);
                    String thumbnail = rs.getString(2);
                    String description = rs.getString(3);
                    String title = rs.getString(4);
                    String authors = getAuthor(bookID);
                    int quantityInStock = rs.getInt(5);

                    Book book = new Book(thumbnail, title, authors, description, bookID, quantityInStock);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }
    }

    public List<Book> getTopCategories(String category) throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM books NATURAL JOIN categories WHERE category = ? " +
                "ORDER BY averageRating DESC";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setString(1, category);
            List<Book> books = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    String bookID = rs.getString(1);
                    String thumbnail = rs.getString(2);
                    String description = rs.getString(3);
                    String title = rs.getString(4);
                    String authors = getAuthor(bookID);
                    int quantityInStock = rs.getInt(5);

                    Book book = new Book(thumbnail, title, authors, description, bookID, quantityInStock);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }
    }

    public String getAuthor(String bookID) throws SQLException {
        String sql = "SELECT authorName FROM author WHERE bookID = ?";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setString(1, bookID);
            try(ResultSet rs = stmt.executeQuery()){
                StringBuilder authors = new StringBuilder();
                while (rs.next()) {
                    authors.append(rs.getString(1));
                }
                return authors.toString();
            }catch (SQLException e){
                e.printStackTrace();
            }

        }
        return null;
    }

    public List<Book> getRecentlyBooks(int number) throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM books ORDER BY addDate DESC LIMIT ?";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setInt(1, number);
            List<Book> books = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    String bookID = rs.getString(1);
                    String thumbnail = rs.getString(2);
                    String description = rs.getString(3);
                    String title = rs.getString(4);
                    String authors = getAuthor(bookID);
                    int quantityInStock = rs.getInt(5);

                    Book book = new Book(thumbnail, title, authors, description, bookID, quantityInStock);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }
    }

    public List<String> getAllSubjectFromDB() throws SQLException {
        String sql = "SELECT DISTINCT Category FROM categories ORDER BY Category ASC";
        try(PreparedStatement stmt = createStatement(sql);  ){
            List<String> subjects = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String category = rs.getString(1);
                    subjects.add(category);
                }
                return subjects;
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public boolean existInShelf(String bookID) throws SQLException {
        String sql = "SELECT EXISTS (SELECT 1 FROM shelf WHERE bookID = ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bookID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public void saveBookToShelf(String bookID) throws SQLException {
        String sql = "INSERT INTO shelf (Email, bookID) VALUES (?, ?)";
        try(PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, bookID);
            stmt.executeUpdate();
        }
    }

    public void deleteBookFromShelf(String bookID) throws SQLException {
        String sql = "DELETE FROM shelf WHERE bookID = ?";
        try(PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, bookID);
            stmt.executeUpdate();
        }
    }

    public List<Book> allBorrowBooks() throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM borrow NATURAL JOIN books WHERE Email = ?";
        try(PreparedStatement stmt = createStatement(sql)) {;
            stmt.setString(1, Account.getInstance().getEmail());
            List<Book> books = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    String bookID = rs.getString(1);
                    String thumbnail = rs.getString(2);
                    String description = rs.getString(3);
                    String title = rs.getString(4);
                    String authors = getAuthor(bookID);
                    int quantityInStock = rs.getInt(5);

                    Book book = new Book(thumbnail, title, authors, description, bookID, quantityInStock);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }
    }

    public List<Book> allBookInShelf() throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM shelf NATURAL JOIN books WHERE Email = ?";
        try(PreparedStatement stmt = createStatement(sql)) {;
            stmt.setString(1, Account.getInstance().getEmail());
            List<Book> books = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    String bookID = rs.getString(1);
                    String thumbnail = rs.getString(2);
                    String description = rs.getString(3);
                    String title = rs.getString(4);
                    String authors = getAuthor(bookID);
                    int quantityInStock = rs.getInt(5);

                    Book book = new Book(thumbnail, title, authors, description, bookID, quantityInStock);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }
    }

    public void saveCmt(String email, String bookID, String cmt, Timestamp now) {
        String sql = "INSERT INTO rating (Email, bookID, Comment, Time) " +
                "VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setString(1, email);
            stmt.setString(2, bookID);
            stmt.setString(3, cmt);
            stmt.setTimestamp(4, now);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Comment> allBookCmt(String bookID) throws SQLException {
        String sql = "SELECT Email, Comment, Time FROM rating WHERE bookID = ?";

        try(PreparedStatement stmt = createStatement(sql)) {;
            stmt.setString(1, bookID);
            List<Comment> comments = new ArrayList<>();

            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    String email = rs.getString(1);
                    String cmt = rs.getString(2);
                    Timestamp time = rs.getTimestamp(3);

                    Comment comment = new Comment(email, cmt, time);
                    comments.add(comment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return comments;
        }
    }

    public List<Book> allHints(String text) throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM books WHERE title LIKE ? LIMIT 4";
        try(PreparedStatement stmt = createStatement(sql)) {;
            stmt.setString(1, "%" + text + "%");
            List<Book> books = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()) {
                    String bookID = rs.getString(1);
                    String thumbnail = rs.getString(2);
                    String description = rs.getString(3);
                    String title = rs.getString(4);
                    String authors = getAuthor(bookID);
                    int quantityInStock = rs.getInt(5);

                    Book book = new Book(thumbnail, title, authors, description, bookID, quantityInStock);
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }
    }
}