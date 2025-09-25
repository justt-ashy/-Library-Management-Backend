package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/books", "/book"})
public class BookController {

    @Autowired
    private BookService bookService;

//    @GetMapping("/whoami")
//    public String whoami(Authentication auth){
//        if(auth==null){
//            return "Not Aunthenticated";
//        }
//        return auth.getAuthorities().toString();
//    }

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

    // ✅ Add a new book (maps to /books and /book)
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addNew(book));
    }

    // ✅ Update an existing book (maps to /books/{id} and /book/{id})
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
