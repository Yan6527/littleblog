package com.atyan.mapper;

import com.atyan.domain.Category;
import com.atyan.vo.ArticleListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author sunset
* @description 针对表【sg_category(分类表)】的数据库操作Mapper
* @createDate 2025-01-17 14:54:15
* @Entity com.atyan.domain.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {
    List<ArticleListVo> serchList(String search);
}




