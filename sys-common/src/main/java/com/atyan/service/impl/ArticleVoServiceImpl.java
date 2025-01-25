package com.atyan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.mapper.ArticleVoMapper;
import com.atyan.service.ArticleVoService;
import com.atyan.vo.ArticleVo;
import org.springframework.stereotype.Service;

@Service
public class ArticleVoServiceImpl extends ServiceImpl<ArticleVoMapper, ArticleVo> implements ArticleVoService {
}