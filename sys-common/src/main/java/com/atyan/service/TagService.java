package com.atyan.service;

import com.atyan.domain.ResponseResult;
import com.atyan.dto.TagListDto;
import com.atyan.vo.PageVo;
import com.atyan.vo.TagVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atyan.domain.Tag;

import java.util.List;


public interface TagService extends IService<Tag> {


    //查询标签列表
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult deleteTag(Long id);

    List<TagVo> listAllTag();
}