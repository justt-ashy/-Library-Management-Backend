package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.model.IssuedBook;
import com.example.LMS_Backend.service.IssuedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issued-books")
@CrossOrigin("*")
public class IssueController {

    @Autowired
    private IssuedBookService issuedBookService;

    // Get all issued books
    @GetMapping
    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookService.getAll();
    }

    // Get a single issued book by ID
    @GetMapping("/{id}")
    public IssuedBook getIssuedBook(@PathVariable Long id) {
        return issuedBookService.get(id);
    }

    // Create a new issued book (issue a book)
    @PostMapping
    public IssuedBook issueBook(@RequestBody IssuedBook issuedBook) {
        return issuedBookService.save(issuedBook);
    }

    // Update an issued book (e.g., mark as returned)
    @PutMapping("/{id}")
    public IssuedBook updateIssuedBook(@PathVariable Long id, @RequestBody IssuedBook issuedBook) {
        issuedBook.setId(id);
        return issuedBookService.save(issuedBook);
    }

//    // Delete an issued book record
//    @DeleteMapping("/{id}")
//    public void deleteIssuedBook(@PathVariable Long id) {
//        issuedBookService.delete(id);
//    }
}
