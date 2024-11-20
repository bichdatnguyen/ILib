package org.example.ilib.Controller;


import org.example.ilib.Processor.Book;

import java.util.ArrayList;
import java.util.List;

public class Book_list {
    public static final int TOP_BOOK = 1;
    public static final int CATEGORIES_BOOK = 2;
    public static final int RECECNTLYADDED_BOOK = 3;


    protected List<Book> bookList = new ArrayList<Book>();

    protected Book_list(int Phan_loai) {
        if (Phan_loai == TOP_BOOK) {
            bookList = addTopBookList();

        }
        else if (Phan_loai == CATEGORIES_BOOK) {
            bookList = CategoriesBookList();
        }
        else if (Phan_loai == RECECNTLYADDED_BOOK) {
            bookList = RecentlyAddedBookList();
        }
    }
    private List<Book> addTopBookList(){
        List<Book>TopbookList = new ArrayList<>();
        Book book = new Book("C++_For_Dummie","/org/assets/C++_For_Dummies.jpg","Stephen R. Davist");
        TopbookList.add(book);
        Book book1 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Titu Andreescu,Kathy Cordeiro,Alina Andreescu");
        TopbookList.add(book1);
        Book book2 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Daniel J. Duffy");
        TopbookList.add(book2);
        Book book3 = new Book("C++_how_to_progam","/org/assets/C++_how_to_progam.jpg","Harvey M. Deitel,Paul J. Deitel");
        TopbookList.add(book3);
        Book book4 = new Book("Java_pro","/org/assets/Java_pro.jpg","NARAYAN CHANGDER");
        TopbookList.add(book4);
        Book book5 = new Book("Key_Java","/org/assets/Key_Java.jpg","John Hunt,Alexander G. McManus");
        TopbookList.add(book5);
        Book book6 = new Book("Math_Fact_Fluency","/org/assets/Math_Fact_Fluency.jpg","Jennifer Bay-Williams,Gina Kling");
        TopbookList.add(book6);
        Book book7 = new Book("Math_Tools","/org/assets/Math_Tools.jpg","Harvey F. Silver,John R. Brunsting,Terry Walsh");
        TopbookList.add(book7);
        Book book8 = new Book("TCP_IP_Sockets_in_Java","/org/assets/TCP_IP_Sockets_in_Java.jpg","Kenneth L. Calvert,Michael J. Donahoo");
        TopbookList.add(book8);
        Book book9 = new Book("Learn_C++_in_24_Hours","/org/assets/Learn_C++_in_24_Hours.jpg","Rogers Cadenhead,Jesse Liberty");
        TopbookList.add(book9);

        return TopbookList;
    }

    private List<Book> CategoriesBookList(){
        List<Book>CatebookList = new ArrayList<>();
        Book book = new Book("C++_For_Dummie","/org/assets/C++_For_Dummies.jpg","Stephen R. Davist");
        CatebookList.add(book);
        Book book1 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Titu Andreescu,Kathy Cordeiro,Alina Andreescu");
        CatebookList.add(book1);
        Book book2 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Daniel J. Duffy");
        CatebookList.add(book2);
        Book book3 = new Book("C++_how_to_progam","/org/assets/C++_how_to_progam.jpg","Harvey M. Deitel,Paul J. Deitel");
        CatebookList.add(book3);
        Book book4 = new Book("Java_pro","/org/assets/Java_pro.jpg","NARAYAN CHANGDER");
        CatebookList.add(book4);
        Book book5 = new Book("Key_Java","/org/assets/Key_Java.jpg","John Hunt,Alexander G. McManus");
        CatebookList.add(book5);
        Book book6 = new Book("Math_Fact_Fluency","/org/assets/Math_Fact_Fluency.jpg","Jennifer Bay-Williams,Gina Kling");
        CatebookList.add(book6);
        Book book7 = new Book("Math_Tools","/org/assets/Math_Tools.jpg","Harvey F. Silver,John R. Brunsting,Terry Walsh");
        CatebookList.add(book7);
        Book book8 = new Book("TCP_IP_Sockets_in_Java","/org/assets/TCP_IP_Sockets_in_Java.jpg","Kenneth L. Calvert,Michael J. Donahoo");
        CatebookList.add(book8);
        Book book9 = new Book("Learn_C++_in_24_Hours","/org/assets/Learn_C++_in_24_Hours.jpg","Rogers Cadenhead,Jesse Liberty");
        CatebookList.add(book9);

        return CatebookList;
    }

    protected List<Book> RecentlyAddedBookList(){
        List<Book>RecentlyBookList = new ArrayList<>();
        Book book = new Book("C++_For_Dummie","/org/assets/C++_For_Dummies.jpg","Stephen R. Davist");
        RecentlyBookList.add(book);
        Book book1 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Titu Andreescu,Kathy Cordeiro,Alina Andreescu");
        RecentlyBookList.add(book1);
        Book book2 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Daniel J. Duffy");
        RecentlyBookList.add(book2);
        Book book3 = new Book("C++_how_to_progam","/org/assets/C++_how_to_progam.jpg","Harvey M. Deitel,Paul J. Deitel");
        RecentlyBookList.add(book3);
        Book book4 = new Book("Java_pro","/org/assets/Java_pro.jpg","NARAYAN CHANGDER");
        RecentlyBookList.add(book4);
        Book book5 = new Book("Key_Java","/org/assets/Key_Java.jpg","John Hunt,Alexander G. McManus");
        RecentlyBookList.add(book5);
        Book book6 = new Book("Math_Fact_Fluency","/org/assets/Math_Fact_Fluency.jpg","Jennifer Bay-Williams,Gina Kling");
        RecentlyBookList.add(book6);
        Book book7 = new Book("Math_Tools","/org/assets/Math_Tools.jpg","Harvey F. Silver,John R. Brunsting,Terry Walsh");
        RecentlyBookList.add(book7);
        Book book8 = new Book("TCP_IP_Sockets_in_Java","/org/assets/TCP_IP_Sockets_in_Java.jpg","Kenneth L. Calvert,Michael J. Donahoo");
        RecentlyBookList.add(book8);
        Book book9 = new Book("Learn_C++_in_24_Hours","/org/assets/Learn_C++_in_24_Hours.jpg","Rogers Cadenhead,Jesse Liberty");
        RecentlyBookList.add(book9);

        return RecentlyBookList;
    }




}