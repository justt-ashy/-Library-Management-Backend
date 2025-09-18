package com.example.LMS_Backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.service.BookService;
import com.example.LMS_Backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/book")
@CrossOrigin("*")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute(name="categories")
    public List<Category> categories(){
        return categoryService.getAllBySort();
    }


    // Get all books
    @RequestMapping(value = {"", "/list"}, method = RequestMethod.GET)
    public String showBooksPage(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "/book/list";
    }

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addBookPage(Model model){
        model.addAttribute("book", new Book());
        return "/book/form";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editBookPage(@PathVariable(name="id") Long id, Model model){
        Book book = bookService.get(id);
        if(book != null){
            model.addAttribute("book", book);
            return "/book/form";
        }
        else{
            return "redirect:/book/add";
        }
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveBook(@Valid Book book, BindingResult bindingResult, final RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "/book/form";
        }

        if(book.getId() == null){
            if(bookService.getByTag(book.getTag())!=null){
                bindingResult.rejectValue("tag","tag","Tag Already exists");
                return "/book/form";
            }
            else{
                bookService.addNew(book);
                redirectAttributes.addFlashAttribute("successMsg","'" + book.getTitle() + "' is added as a New book.");
                return "redirect:/book/add";
            }
        }
        else{
            Book updatedBook = bookService.save(book);
            redirectAttributes.addFlashAttribute("successMsg", "Changes for '" + book.getTitle() + "' are saved successfully");
            return "redirect:/book/edit" + updatedBook.getId();
        }
    }


    @RequestMapping(value="/remove/{id}", method = RequestMethod.GET)
    public String removeBook(@PathVariable(name="id") Long id, Model model){
        Book book = bookService.get(id);
        if(book != null){
            if(bookService.hasUsage(book)){
                model.addAttribute("bookInUse", true);
                return showBooksPage(model);
            }
            else{
                bookService.delete(id);
            }
        }
        return "redirect:/book/list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = Optional.ofNullable(bookService.get(id));
        return book.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addNew(book);
    }

    //  Update an existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Optional<Book> optionalBook = Optional.ofNullable(bookService.get(id));

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


}
