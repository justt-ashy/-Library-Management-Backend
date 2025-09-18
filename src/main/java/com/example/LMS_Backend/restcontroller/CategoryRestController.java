package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/rest/category")
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value= {"/", "/list"})
    public List<Category> all(){
        return categoryService.getAll();
    }
}
