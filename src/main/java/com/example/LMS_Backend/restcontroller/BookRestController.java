package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.service.BookService;
import com.example.LMS_Backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
