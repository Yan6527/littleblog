package com.atyan.runner;

import com.atyan.domain.Article;
import com.atyan.mapper.ArticleMapper;
import com.atyan.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
//当项目启动时，就把博客的浏览量(id和viewCount字段)存储到redis中。也叫启动预处理。CommandLineRunner是spring提供的接口
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    //用于操作redis
    private RedisCache redisCache;

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public void run(String... args) throws Exception {
        //查询数据库中的博客信息，注意只需要查询id、viewCount字段的博客信息
        List<Article> articles = articleMapper.selectList(null);//为null即无条件，表示查询所有
        //stream流获取id、viewCount字段
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));

        //把查询到的id作为key，viewCount作为value，一起存入Redis
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}