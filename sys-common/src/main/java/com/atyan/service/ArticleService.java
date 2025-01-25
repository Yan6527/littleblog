package com.atyan.service;

import com.atyan.domain.Article;
import com.atyan.domain.ResponseResult;
import com.atyan.dto.AddArticleDto;
import com.atyan.dto.ArticleDto;
import com.atyan.vo.ArticleByIdVo;
import com.atyan.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ArticleService extends IService<Article> {
        // 查询热门文章
        ResponseResult hotArticleList();
        // 分页查询文章列表
        ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
        // 查询文章详情
        ResponseResult getArticleDetail(Long id);
        //跟据文章id更新文章浏览量
        ResponseResult updateViewCount(Long id);
        //新增博文
        ResponseResult addArticle(AddArticleDto articleDto);
        //管理端-分页查询博文
        PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);
        //修改文章-①根据文章id查询对应的文章
        ArticleByIdVo getInfo(Long id);

        //修改文章-②然后才是修改文章
        void edit(ArticleDto articleDto);

}
