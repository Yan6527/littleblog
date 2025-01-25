package com.atyan.service.impl;

import com.atyan.constants.SystemConstants;
import com.atyan.domain.Article;
import com.atyan.domain.ResponseResult;
import com.atyan.service.ArticleService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.vo.CategoryVo;
import com.atyan.vo.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.Category;
import com.atyan.service.CategoryService;
import com.atyan.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author sunset
* @description 针对表【sg_category(分类表)】的数据库操作Service实现
* @createDate 2025-01-17 14:54:15
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {

        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        //要求查的是getStatus字段的值为1，注意SystemConstants是我们写的一个常量类，用来解决字面值的书写问题
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //查询文章列表，条件就是上一行的articleWrapper
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重。使用stream流来处理上一行得到的文章表集合
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(queryWrapper);
        return BeanCopyUtils.copyBeanList(list, CategoryVo.class);
    }

    @Override
    public PageVo selectCategoryPage(Integer pageNum, Integer pageSize, Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if (category != null) {
            queryWrapper.like(category.getName() != null, Category::getName, category.getName());
            queryWrapper.like(category.getStatus() != null, Category::getStatus, category.getStatus());
        }
        //查询分页数据
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        //封装成PageVo
        return new PageVo(page.getRecords(), page.getTotal());

    }
}




