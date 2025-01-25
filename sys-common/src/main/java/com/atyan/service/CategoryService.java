package com.atyan.service;

import com.atyan.domain.Category;
import com.atyan.domain.ResponseResult;
import com.atyan.vo.CategoryVo;
import com.atyan.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author sunset
* @description 针对表【sg_category(分类表)】的数据库操作Service
* @createDate 2025-01-17 14:54:15
*/
public interface CategoryService extends IService<Category> {
    //查询文章分类的接口
    ResponseResult getCategoryList();
    //写博客-查询所有分类
    List<CategoryVo> listAllCategory();
    //分页查询所有分类
    PageVo selectCategoryPage(Integer pageNum, Integer pageSize, Category category);
}
