package com.example.LMS_Backend.services;
//package com.lms.service;

import java.util.Date;
import java.util.List;

import com.example.LMS_Backend.model.Category;
import com.example.LMS_Backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Long getTotalCount(){
        return categoryRepository.count();
    }

    public List<Category> getAllBySort(){
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public List<Category> getAll(){
        return categoryRepository.findAll();
    }

    public Category get(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    public Category addNew(Category category){
        category.setCreateDate(new Date());
        return categoryRepository.save(category);
    }

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public void delete(Category category){
        categoryRepository.delete(category);
    }

    public void delete(Long id){
        categoryRepository.deleteById(id);
    }

    public boolean hasUsage(Category category){
        return !category.getBooks().isEmpty();
    }

}