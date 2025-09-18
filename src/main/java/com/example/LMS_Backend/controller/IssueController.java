package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.common.Constants;
import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.model.IssuedBook;
import com.example.LMS_Backend.service.CategoryService;
import com.example.LMS_Backend.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/issue")
@CrossOrigin("*")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private CategoryService categoryService;

    @ModelAttribute(name = "memberTypes")
    public List<String> memberTypes(){
        return Constants.MEMBER_TYPES;
    }

    @ModelAttribute("categories")
    public List<Category> getCategories(){
        return categoryService.getAllBySort();
    }

    @RequestMapping(value ={"/", "/list"}, method = RequestMethod.GET)
    public  String listIssuePage(Model model){
        model.addAttribute("issues", issueService.getAllUnreturned());
        return "/issue/list";
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public String newIssuePage(Model model){
        return "/issue/form";
    }
}
