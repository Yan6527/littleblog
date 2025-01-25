package com.atyan.service.impl;

import com.atyan.constants.SystemConstants;
import com.atyan.domain.ResponseResult;
import com.atyan.dto.TagListDto;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.vo.PageVo;
import com.atyan.vo.TagVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.Tag;
import com.atyan.mapper.TagMapper;
import com.atyan.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(tagListDto.getName() != null, Tag::getName, tagListDto.getName());
        lambdaQueryWrapper.like(tagListDto.getRemark() != null, Tag::getRemark, tagListDto.getRemark());
        // 分页查询
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, lambdaQueryWrapper);
        // 封装成PageVo
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    // 删除标签
    @Override
    public ResponseResult deleteTag(Long id) {
        TagMapper tagMapper = getBaseMapper();
        // 通过数据id查找数据
        Tag tag  = tagMapper.selectById(id);
        // 把值传入数据库进行更新
        if (tag != null){
            // 把 def_flag=1 为逻辑删除
            int flag = 1;
            tagMapper.myUpdateById(id,flag);
        }
        return ResponseResult.okResult();
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Tag::getDelFlag, SystemConstants.STATUS_NORMAL);
        List<Tag> list = list(lambdaQueryWrapper);
        return BeanCopyUtils.copyBeanList(list, TagVo.class);
    }
}