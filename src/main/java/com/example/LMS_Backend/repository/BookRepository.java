package com.example.LMS_Backend.repository;

import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find a book by its unique tag
    Book findByTag(String tag);

    // Find all books belonging to a specific category
    List<Book> findByCategory(Category category);

    // Find books by category and status (e.g., available/issued)
    List<Book> findByCategoryAndStatus(Category category, Integer status);

    // Count books by status
    long countByStatus(Integer status);
}
