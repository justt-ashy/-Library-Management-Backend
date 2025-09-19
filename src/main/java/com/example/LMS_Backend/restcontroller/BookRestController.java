package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.service.BookService;
import com.example.LMS_Backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping(value="/rest/book")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/","/list"})
    public List<Book> all(){
        return bookService.getAll();
    }

    @GetMapping(value="/{id}/list")
    public List<Book> get(@PathVariable(name="id") Long id){
        Category category = categoryService.get(id);
        return bookService.getByCategory(category);
    }

    @GetMapping(value="/{id}/available")
    public List<Book> getAvailableBooks(@PathVariable(name="id")Long id){
        Category category = categoryService.get(id);
        return bookService.getAvailableByCategory(category);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Book> getBook(@PathVariable(name="id") Long id){
        Book book = bookService.get(id);
        if(book != null){
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/save")
    public ResponseEntity<String> saveBook(@RequestBody Book book){
        try {
            bookService.addNew(book);
            return ResponseEntity.ok("Book saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving book: " + e.getMessage());
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<String> updateBook(@PathVariable(name="id") Long id, @RequestBody Book book){
        try {
            Book existingBook = bookService.get(id);
            if(existingBook != null){
                book.setId(id);
                bookService.save(book);
                return ResponseEntity.ok("Book updated successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating book: " + e.getMessage());
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable(name="id") Long id){
        try {
            Book book = bookService.get(id);
            if(book != null){
                if(!bookService.hasUsage(book)){
                    bookService.delete(id);
                    return ResponseEntity.ok("Book deleted successfully");
                } else {
                    return ResponseEntity.badRequest().body("Cannot delete book: it has been issued");
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting book: " + e.getMessage());
        }
    }
}
