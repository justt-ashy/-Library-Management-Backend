package com.example.LMS_Backend.service;

import com.example.LMS_Backend.common.Constants;
import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.IssuedBook;
import com.example.LMS_Backend.repository.IssuedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssuedBookService {

    @Autowired
    private IssuedBookRepository issuedBookRepository;

    // Get all issued books
    public List<IssuedBook> getAll() {
        return issuedBookRepository.findAll();
    }

    // Get a single issued book by ID
    public IssuedBook get(Long id) {
//        Optional<IssuedBook> issuedBook = issuedBookRepository.findById(id);
        return issuedBookRepository.findById(id).get();
    }

    // Save or update an issued book
    public IssuedBook save(IssuedBook issuedBook) {
        return issuedBookRepository.save(issuedBook);
    }

//    // Delete an issued book by ID
//    public void delete(Long id) {
//        issuedBookRepository.deleteById(id);
//    }

    public int getCountByBook(Book book) {
        return issuedBookRepository.countByBookAndReturned(book, Constants.BOOK_NOT_RETURNED);
    }

    public IssuedBook addNew(IssuedBook issuedBook){
        issuedBook.setReturned(Contants.BOOK_NOT_RETURNED);
        return issuedBookRepository.save(issuedBook);
    }
}
