package com.atyan.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {

    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类名
    private String categoryName;
    //所属分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    @TableField(exist = false)
    private List<String> tagNameList;

    private Date createTime;

    //返回的数据条数
    private Long total;

}