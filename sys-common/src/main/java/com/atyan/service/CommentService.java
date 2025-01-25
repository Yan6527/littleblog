package com.atyan.service;

import com.atyan.domain.Comment;
import com.atyan.domain.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sunset
* @description 针对表【sg_comment(评论表)】的数据库操作Service
* @createDate 2025-01-19 14:26:26
*/
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize);
    ResponseResult addComment(Comment comment);
}
