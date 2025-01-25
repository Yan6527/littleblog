package com.atyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.atyan.domain.Article;
import com.atyan.vo.ArticleVo;
import org.springframework.stereotype.Service;

@Service
public interface ArticleVoMapper extends BaseMapper<ArticleVo> {

}