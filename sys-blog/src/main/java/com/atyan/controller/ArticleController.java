package com.atyan.controller;

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.Article;
import com.atyan.domain.Category;
import com.atyan.domain.ResponseResult;
import com.atyan.mapper.CategoryMapper;
import com.atyan.service.ArticleService;
import com.atyan.vo.ArticleListVo;
import com.atyan.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping("/list")
    @mySystemlog(businessName = "查询文章列表")
    public List<Article> list() {
        return articleService.list();
    }

    @GetMapping("/hotArticleList")
    @mySystemlog(businessName = "查询热门文章")
    public ResponseResult hotArticleList(){
        //查询热门文章，封装成ResponseResult返回
        return articleService.hotArticleList();
    }
    @GetMapping("/articleList")
    @mySystemlog(businessName = "分页查询文章列表")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId,@RequestParam(required = false) String search){
        if (search!=""){
            List<ArticleListVo> articleListVo = categoryMapper.serchList(search);
            if (articleListVo.size()!=0) {
                PageVo pageVo = new PageVo(articleListVo,articleListVo.get(0).getTotal());
                List<ArticleListVo> list = pageVo.getRows();
                for (ArticleListVo one : list) {
                    Category category = categoryMapper.selectById(one.getId());
                    one.setCategoryName(category.getName());
                    one.setCategoryId(category.getId());
                }

                return ResponseResult.okResult(pageVo);
            }

        }
        ResponseResult responseResult = articleService.articleList(pageNum, pageSize, categoryId);
        return responseResult;
    }
    @GetMapping("/{id}")
    @mySystemlog(businessName = "根据id查询文章详情")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    @mySystemlog(businessName = "根据文章id从mysql查询文章")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
