package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    // ✅ Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAll();
    }

    // ✅ Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.get(id);
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Add a new book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addNew(book);
    }

    // ✅ Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Optional<Book> optionalBook = bookService.get(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetails.getTitle());
            book.setTag(bookDetails.getTag());
            book.setAuthors(bookDetails.getAuthors());
            book.setPublisher(bookDetails.getPublisher());
            book.setIsbn(bookDetails.getIsbn());
            book.setStatus(bookDetails.getStatus());
            book.setCategory(bookDetails.getCategory());

            return ResponseEntity.ok(bookService.save(book));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Delete a book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> book = bookService.get(id);
        if (book.isPresent()) {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
