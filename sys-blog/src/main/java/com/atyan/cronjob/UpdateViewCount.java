package com.atyan.cronjob;

import com.atyan.domain.Article;
import com.atyan.service.ArticleService;
import com.atyan.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
//通过定时任务实现每隔3分钟把redis中的浏览量更新到mysql数据库中
public class UpdateViewCount {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    //每隔30秒，把redis的浏览量数据更新到mysql数据库
    @Scheduled(cron = "${ViewCount.scheduled}")
    public void updateViewCount(){
        //获取redis中的浏览量，注意得到的viewCountMap是HashMap双列集合
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //让双列集合调用entrySet方法即可转为单列集合，然后才能调用stream方法
        //调用article的构造方法
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                //过滤浏览量为0
                .filter(entry -> entry.getValue() != 0)
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                //把最终数据转为List集合
                .collect(Collectors.toList());
        //调用service层方法
        articleService.updateBatchById(articles);
        //方便在控制台看打印信息
        System.out.println("redis的文章浏览量数据已更新到数据库,现在的时间是: "+LocalTime.now());

    }
}