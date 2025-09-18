package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

   @RequestMapping(value={"/", "/list"}, method = RequestMethod.GET)
   public String showCategoriesPage(Model model){
       model.addAttribute("categories", categoryService.getAll());
       return "/category/list";
   }

   @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addCategoriesPage(Model model){
       model.addAttribute("category", new Category());
       return "/category/form";
   }

   @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editCategoryPage(@PathVariable(name="id")Long id, Model model){
       Category category = categoryService.get(id);
       if(category != null){
           model.addAttribute("category", category);
           return "/category/form";
       }
       else{
           return "redirect:/category/add";
       }
   }


   @RequestMapping(value="/save", method = RequestMethod.GET)
    public String saveCategory(@Valid Category category, BindingResult bindingResult, final RedirectAttributes redirectAttributes){
       if( bindingResult.hasErrors()) {
           return "/category/form";
       }
           if( category.getId() == null){
               categoryService.addNew(category);
               redirectAttributes.addFlashAttribute("successMsg", "'" + category.getName() + "' category has been added.");
               return "redirect:/category/add";
           }
           else{
               Category updateCategory = categoryService.save(category);
               redirectAttributes.addFlashAttribute("successMsg", "Changes for'" + category.getName() + "' has been done successfully.");
               return "redirect:/category/add" + updateCategory.getId();
           }
   }

   @RequestMapping(value="/remove/{id}", method = RequestMethod.GET)
    public String removeCategory(@PathVariable(name="id")Long id, Model model){
       Category category = categoryService.get(id);
       if( category != null){
           if(categoryService.hasUsage(category)){
               model.addAttribute("categoryIssue", true);
               return showCategoriesPage(model);
           }
           else{
               categoryService.delete(id);
           }
       }
       return "redirect:/categories/list";
   }
}
