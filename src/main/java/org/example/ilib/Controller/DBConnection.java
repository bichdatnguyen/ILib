package org.example.ilib.Controller;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.ilib.Account.Account;
import org.example.ilib.Book.Book;
import org.example.ilib.Book.BookDetail.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * constructor.
     */
    private DBConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(userPassword);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(10000000);
        config.setMinimumIdle(20);
        config.setIdleTimeout(4000);  // Giảm thời gian giữ kết nối không dùng
        config.setMaxLifetime(240000); // Tăng thời gian sống tối đa nếu cần
        config.setConnectionTimeout(5000);

        dataSource = new HikariDataSource(config);
    }

    /** get DBConnection's instance.
     * @return DBConnection's instance
     * @throws SQLException prevent SQL exception
     */
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

    /** add account to database.
     * @param email email's info
     * @param phoneNumber phone number's info
     * @param fullName name's info
     * @param password password's info
     * @param avatarPath avatar's info
     * @param role user or admin
     * @throws SQLException prevent SQL exception
     */
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

    /** create voucher.
     * @param email add voucher to this email
     * @param discountPercentage voucher's discount percentage
     * @throws SQLException prevent SQL exception
     */
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

    /** check if book already exist in database.
     * @param bookID bookID which is checked
     * @return true or false
     * @throws SQLException prevent SQL exception
     */
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

    /** get all top books in database.
     * @param number number of books want to take from database
     * @return list of top books
     * @throws SQLException prevent SQL exception
     */
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

    /** get all books by category in database.
     * @param category category want to take
     * @return list of books by category
     * @throws SQLException prevent SQL exception
     */
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

    /** get authors of a book.
     * @param bookID book ID of a book when to find authors.
     * @return authors name.
     * @throws SQLException prevent SQL exception
     */
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

    /** get all recently add books in database.
     * @param number number of books want to take
     * @return list of recently add books.
     * @throws SQLException prevent SQL exception
     */
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

    /** get recommend books from database.
     * @param number number of books want to take
     * @return list of recommend books
     * @throws SQLException prevent SQL exception
     */
    public List<Book> getRecommendedBooks(int number) throws SQLException {
        String sql = "SELECT bookID, thumbnail, description, title, quantityInStock " +
                "FROM books ORDER BY averageRating ASC LIMIT ?";
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

    /** get all categories in database.
     * @return list of categories
     * @throws SQLException prevent SQL exception
     */
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

    /** check if book exist in shelf.
     * @param bookID book ID want to check
     * @return true or false
     * @throws SQLException prevent SQL exception
     */
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

    /** add book to shelf.
     * @param bookID book ID want to add
     * @throws SQLException prevent SQL exception
     */
    public void saveBookToShelf(String bookID) throws SQLException {
        String sql = "INSERT INTO shelf (Email, bookID) VALUES (?, ?)";
        try(PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, Account.getInstance().getEmail());
            stmt.setString(2, bookID);
            stmt.executeUpdate();
        }
    }

    /** delete book from shelf.
     * @param bookID book ID want to delete
     * @throws SQLException prevent SQL exception
     */
    public void deleteBookFromShelf(String bookID) throws SQLException {
        String sql = "DELETE FROM shelf WHERE bookID = ?";
        try(PreparedStatement stmt = createStatement(sql)) {
            stmt.setString(1, bookID);
            stmt.executeUpdate();
        }
    }

    /** take all borrow books of user.
     * @return list of borrow books
     * @throws SQLException
     */
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

    /** take all books saved in shelf of user.
     * @return list of books
     * @throws SQLException prevent SQL exception
     */
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

    /** save book's comment.
     * @param email user who post comment
     * @param bookID book ID which is comment
     * @param cmt comment
     * @param now real time
     */
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

    /** take all comments of a book.
     * @param bookID book ID want to take comment
     * @return list of comments
     * @throws SQLException prevent SQL exception
     */
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

    /** hints of book when searching.
     * @param text key word which user want to find
     * @return list of books suggest to user
     * @throws SQLException prevent SQL exception
     */
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

    /**
     * this method will handle inserting user detail.
     * @param userFullname Fullname of user
     * @param userEmailAddress email of user
     * @param userPhoneNumber phone number of user
     * @param userPassword password of user
     * @param userRole role of user
     */
    public void insertUserDetail(String userFullname, String userEmailAddress,
                                 String userPhoneNumber, String userPassword, String userRole) {
        String sql = "INSERT INTO user (Email, phoneNumber, fullname, password, role) " +
                "VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setString(1, userEmailAddress);
            stmt.setString(2, userPhoneNumber);
            stmt.setString(3, userFullname);
            stmt.setString(4, userPassword);
            System.out.println(userRole);
            stmt.setString(5, userRole);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method will handle deleting user detail.
     * @param userEmail email of user
     * @param userPassword password of user
     */
    public void deleteUserDetail(String userEmail, String userPassword) {
        String sql = "DELETE FROM user WHERE Email = ? and password = ?";
        try(PreparedStatement stmt = createStatement(sql)){
            stmt.setString(1, userEmail);
            stmt.setString(2, userPassword);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}