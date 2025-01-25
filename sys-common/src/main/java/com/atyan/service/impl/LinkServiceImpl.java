package com.atyan.service.impl;

import com.atyan.constants.SystemConstants;
import com.atyan.domain.ResponseResult;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.vo.LinkVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atyan.domain.Link;
import com.atyan.service.LinkService;
import com.atyan.mapper.LinkMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author sunset
* @description 针对表【sg_link(友链)】的数据库操作Service实现
* @createDate 2025-01-18 19:38:03
*/
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
    implements LinkService{
    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的
        LambdaQueryWrapper <Link> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        // 查询
        List<Link> links = list(queryWrapper);
        // 封装成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }
}




