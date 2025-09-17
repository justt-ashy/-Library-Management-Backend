package com.example.LMS_Backend.service;

import com.example.LMS_Backend.common.Constants;
import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private IssuedBookService issuedBookService;

    public Long getTotalCount(){
        return bookRepository.count();
    }

    public Long getTotalIssuedBooks(){
        return bookRepository.countByStatus(Constants.BOOK_STATUS_ISSUED);
    }

    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    // ✅ return Optional<Book> for safe handling
    public Optional<Book> get(Long id){
        return bookRepository.findById(id);
    }

    public Book getByTag(String tag){
        return bookRepository.findByTag(tag);
    }

    public List<Book> get(List<Long> ids){
        return bookRepository.findAllById(ids);
    }

    public List<Book> getByCategory(Category category){
        return bookRepository.findByCategory(category);
    }

    public List<Book> getAvailableByCategory(Category category){
        return bookRepository.findByCategoryAndStatus(category, Constants.BOOK_STATUS_AVAILABLE);
    }

    public Book addNew(Book book){
        book.setCreateDate(new Date());
        book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
        return bookRepository.save(book);
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public void delete(Book book){
        bookRepository.delete(book);
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }
}
