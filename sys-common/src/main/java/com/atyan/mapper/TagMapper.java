package com.atyan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.atyan.domain.Tag;
import org.apache.ibatis.annotations.Param;

public interface TagMapper extends BaseMapper<Tag> {

    //逻辑删除标签
    int myUpdateById(@Param("id") Long id, @Param("flag") int flag);
}