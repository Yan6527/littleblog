package com.atyan.controller;

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.Comment;
import com.atyan.domain.ResponseResult;
import com.atyan.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("commentList")
    @mySystemlog(businessName = "分页查询评论列表")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(articleId,pageNum,pageSize);
    }

    @PostMapping
    @mySystemlog(businessName = "发表评论")
    //在文章的评论区发送评论。
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }
}