package org.example.ilib.Controller;


import org.example.ilib.Processor.Book;

import java.util.ArrayList;
import java.util.List;

public class Book_list {
    public static final int TOP_BOOK = 1;
    public static final int CATEGORIES_BOOK = 2;


    protected List<Book> bookList = new ArrayList<Book>();

    protected Book_list(int Phan_loai) {
        if (Phan_loai == TOP_BOOK) {
            bookList = addTopBookList();
            System.out.println(bookList.size());
        }
        else if (Phan_loai == CATEGORIES_BOOK) {
            bookList = CategoriesBookList();
        }
    }
    private List<Book> addTopBookList(){
        List<Book>TopbookList = new ArrayList<>();
        Book book = new Book("C++_For_Dummie","/org/assets/C++_For_Dummies.jpg","Vũ Tiến Đạt");
        TopbookList.add(book);
        Book book1 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Vũ Tiến Đạt");
        TopbookList.add(book1);
        Book book2 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Vũ Tiến Đạt");
        TopbookList.add(book2);
        Book book3 = new Book("C++_how_to_progam","/org/assets/C++_how_to_progam.jpg","Vũ Tiến Đạt");
        TopbookList.add(book3);
        Book book4 = new Book("Java_pro","/org/assets/Java_pro.jpg","Vũ Tiến Đạt");
        TopbookList.add(book4);
        Book book5 = new Book("Key_Java","/org/assets/Key_Java.jpg","Vũ Tiến Đạt");
        TopbookList.add(book5);
        Book book6 = new Book("Math_Fact_Fluency","/org/assets/Math_Fact_Fluency.jpg","Vũ Tiến Đạt");
        TopbookList.add(book6);
        Book book7 = new Book("Math_Tools","/org/assets/Math_Tools.jpg","Vũ Tiến Đạt");
        TopbookList.add(book7);
        Book book8 = new Book("TCP_IP_Sockets_in_Java","/org/assets/TCP_IP_Sockets_in_Java.jpg","Vũ Tiến Đạt");
        TopbookList.add(book8);
        Book book9 = new Book("Learn_C++_in_24_Hours","/org/assets/Learn_C++_in_24_Hours.jpg","Vũ Tiến Đạt");
        TopbookList.add(book9);
        Book book10 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Vũ Tiến Đạt");
        TopbookList.add(book10);
        Book book11 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Vũ Tiến Đạt");
        TopbookList.add(book11);
        Book book12 = new Book("C++_For_Dummies","/org/assets/C++_For_Dummies.jpg","Vũ Tiến Đạt");
        TopbookList.add(book12);
        Book book13 = new Book("Java_pro","/org/assets/Java_pro.jpg","Vũ Tiến Đạt");
        TopbookList.add(book13);
        return TopbookList;
    }

    private List<Book> CategoriesBookList(){
        List<Book>CatebookList = new ArrayList<>();
        Book book = new Book("C++_For_Dummie","/org/assets/C++_For_Dummies.jpg","Vũ Tiến Đạt");
        CatebookList.add(book);
        Book book1 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Vũ Tiến Đạt");
        CatebookList.add(book1);
        Book book2 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Vũ Tiến Đạt");
        CatebookList.add(book2);
        Book book3 = new Book("C++_how_to_progam","/org/assets/C++_how_to_progam.jpg","Vũ Tiến Đạt");
        CatebookList.add(book3);
        Book book4 = new Book("Java_pro","/org/assets/Java_pro.jpg","Vũ Tiến Đạt");
        CatebookList.add(book4);
        Book book5 = new Book("Key_Java","/org/assets/Key_Java.jpg","Vũ Tiến Đạt");
        CatebookList.add(book5);
        Book book6 = new Book("Math_Fact_Fluency","/org/assets/Math_Fact_Fluency.jpg","Vũ Tiến Đạt");
        CatebookList.add(book6);
        Book book7 = new Book("Math_Tools","/org/assets/Math_Tools.jpg","Vũ Tiến Đạt");
        CatebookList.add(book7);
        Book book8 = new Book("TCP_IP_Sockets_in_Java","/org/assets/TCP_IP_Sockets_in_Java.jpg","Vũ Tiến Đạt");
        CatebookList.add(book8);
        Book book9 = new Book("Learn_C++_in_24_Hours","/org/assets/Learn_C++_in_24_Hours.jpg","Vũ Tiến Đạt");
        CatebookList.add(book9);
        Book book10 = new Book("Awesome_Math","/org/assets/Awesome_Math.jpg","Vũ Tiến Đạt");
        CatebookList.add(book10);
        Book book11 = new Book("C++_for_Finance","/org/assets/C++_for_Finance.jpg","Vũ Tiến Đạt");
        CatebookList.add(book11);
        Book book12 = new Book("C++_For_Dummies","/org/assets/C++_For_Dummies.jpg","Vũ Tiến Đạt");
        CatebookList.add(book12);
        Book book13 = new Book("Java_pro","/org/assets/Java_pro.jpg","Vũ Tiến Đạt");
        CatebookList.add(book13);
        return CatebookList;
    }




}