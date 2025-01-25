package com.atyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.ArticleTag;
import com.atyan.mapper.ArticleTagMapper;
import com.atyan.service.ArticleTagService;
import org.springframework.stereotype.Service;


@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}