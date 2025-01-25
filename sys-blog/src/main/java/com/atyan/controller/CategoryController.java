package com.atyan.controller;

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.Category;
import com.atyan.domain.ResponseResult;
import com.atyan.dto.CategoryDto;
import com.atyan.service.CategoryService;
import com.atyan.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryList")
    @mySystemlog(businessName = "获取分类列表")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }


}

