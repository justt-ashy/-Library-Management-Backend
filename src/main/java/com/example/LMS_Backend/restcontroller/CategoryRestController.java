package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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

    @GetMapping(value="/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable(name="id") Long id){
        Category category = categoryService.get(id);
        if(category != null){
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/save")
    public ResponseEntity<String> saveCategory(@RequestBody Category category){
        try {
            categoryService.addNew(category);
            return ResponseEntity.ok("Category saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving category: " + e.getMessage());
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable(name="id") Long id, @RequestBody Category category){
        try {
            Category existingCategory = categoryService.get(id);
            if(existingCategory != null){
                category.setId(id);
                categoryService.save(category);
                return ResponseEntity.ok("Category updated successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating category: " + e.getMessage());
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name="id") Long id){
        try {
            Category category = categoryService.get(id);
            if(category != null){
                if(!categoryService.hasUsage(category)){
                    categoryService.delete(id);
                    return ResponseEntity.ok("Category deleted successfully");
                } else {
                    return ResponseEntity.badRequest().body("Cannot delete category: it contains books");
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting category: " + e.getMessage());
        }
    }
}
