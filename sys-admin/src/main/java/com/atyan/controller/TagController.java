package com.atyan.controller;

import com.atyan.domain.ResponseResult;
import com.atyan.domain.Tag;
import com.atyan.dto.AddTagDto;
import com.atyan.dto.EditTagDto;
import com.atyan.dto.TagListDto;
import com.atyan.service.TagService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.vo.PageVo;
import com.atyan.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;
    //查询标签列表
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    //-------------------------------新增标签------------------------------------
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto tagDto){
        Tag tag = BeanCopyUtils.copyProperties(tagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }
    //-------------------------------删除标签------------------------------------
    @DeleteMapping
    public ResponseResult deleteTags(@RequestBody List<Long> ids) {
        ids.forEach(id -> tagService.deleteTag(id)); // 遍历逐个删除
        return ResponseResult.okResult();
    }

    //-------------------------------修改标签------------------------------------

    @GetMapping("/{id}")
    //①根据标签的id来查询标签
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Tag tag = tagService.getById(id);
        return ResponseResult.okResult(tag);
    }

    @PutMapping
    //②根据标签的id来修改标签
    public ResponseResult edit(@RequestBody EditTagDto tagDto){
        Tag tag = BeanCopyUtils.copyProperties(tagDto,Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }
    //-----------------------------写博文--查询所有标签------------------------------------
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

}