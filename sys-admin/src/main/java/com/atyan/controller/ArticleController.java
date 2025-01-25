package com.atyan.controller;

import com.atyan.domain.Article;
import com.atyan.domain.ResponseResult;
import com.atyan.dto.AddArticleDto;
import com.atyan.dto.ArticleDto;
import com.atyan.service.ArticleService;
import com.atyan.vo.ArticleByIdVo;
import com.atyan.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content/article")
public class ArticleController {

    //------------------------------新增博客文章-----------------------------
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.addArticle(article);
    }

    //-----------------------------分页查询博客文章---------------------------

    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize){
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }
    @GetMapping(value = "/{id}")
    //①先查询根据文章id查询对应的文章
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleByIdVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    //②然后才是修改文章
    public ResponseResult edit(@RequestBody ArticleDto articleDto){
        articleService.edit(articleDto);
        return ResponseResult.okResult();
    }
    //---------------------------根据文章id来删除文章-------------------------

    @DeleteMapping()
    public ResponseResult delete(@RequestBody List<Long> ids){
        //批量删除
        ids.forEach(id -> articleService.removeById(id));
        return ResponseResult.okResult();
    }
}