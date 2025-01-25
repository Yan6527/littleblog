package com.atyan.service;

import com.atyan.domain.Link;
import com.atyan.domain.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sunset
* @description 针对表【sg_link(友链)】的数据库操作Service
* @createDate 2025-01-18 19:38:03
*/
public interface LinkService extends IService<Link> {
    // 友链列表查询
     ResponseResult getAllLink();
}
