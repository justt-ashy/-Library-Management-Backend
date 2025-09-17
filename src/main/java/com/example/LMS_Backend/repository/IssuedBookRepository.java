package com.example.LMS_Backend.repository;
//package com.lms.repository;


import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.IssuedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook,Long> {
    int countByBookAndReturned(Book book, Integer bookNotReturned);
//    public Long countByBookAndReturned(Book book, Integer returned);
}