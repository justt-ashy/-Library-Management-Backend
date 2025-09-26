package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.model.IssuedBook;
import com.example.LMS_Backend.services.IssuedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/issued-books")
public class IssuedBookController {

    @Autowired
    private IssuedBookService issuedBookService;

    // Get all issued books
    @GetMapping
    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookService.getAll();
    }

    // Get a single issued book by ID
    @GetMapping("/{id}")
    public ResponseEntity<IssuedBook> getIssuedBook(@PathVariable Long id) {
        IssuedBook found = issuedBookService.getById(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }

    // Create a new issued book (issue a book)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<IssuedBook> issueBook(@Valid @RequestBody IssuedBook issuedBook) {
        return ResponseEntity.ok(issuedBookService.save(issuedBook));
    }

    // Update an issued book (e.g., mark as returned) - only update 'returned'
    @PutMapping("/{id}")
    public ResponseEntity<IssuedBook> updateIssuedBook(@PathVariable Long id, @RequestBody IssuedBook issuedBook) {
        IssuedBook existing = issuedBookService.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        // Only update status; preserve linked book to avoid @NotNull violation
        existing.setReturned(issuedBook.getReturned());
        return ResponseEntity.ok(issuedBookService.save(existing));
    }

    // Delete an issued book record
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssuedBook(@PathVariable Long id) {
        IssuedBook existing = issuedBookService.getById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        issuedBookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}