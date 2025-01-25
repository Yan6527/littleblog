package com.atyan.service.impl;

import com.atyan.constants.SystemConstants;
import com.atyan.domain.Article;
import com.atyan.domain.ArticleTag;
import com.atyan.domain.Category;
import com.atyan.domain.ResponseResult;
import com.atyan.dto.AddArticleDto;
import com.atyan.dto.ArticleDto;
import com.atyan.mapper.ArticleMapper;
import com.atyan.service.ArticleService;
import com.atyan.service.ArticleTagService;
import com.atyan.service.ArticleVoService;
import com.atyan.service.CategoryService;
import com.atyan.utils.BeanCopyUtils;
import com.atyan.utils.RedisCache;
import com.atyan.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult hotArticleList() {

        //从redis查询文章的浏览量，展示在热门文章列表-
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //让双列集合调用entrySet方法即可转为单列集合，然后才能调用stream方法
        List<Article> redisArticles = viewCountMap.entrySet()
                .stream()
                //过滤浏览量为0
                .filter(entry -> entry.getValue() != 0)
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                //把最终数据转为List集合
                .collect(Collectors.toList());
        articleService.updateBatchById(redisArticles);

        //查询热门文章，封装成ResponseResult返回。把所有查询条件写在queryWrapper里面
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //查询的不能是草稿。也就是Status字段不能是0
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序。也就是根据ViewCount字段降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只能查询出来10条消息。当前显示第一页的数据，每页显示10条数据
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_CURRENT,SystemConstants.ARTICLE_STATUS_SIZE);
        page(page,queryWrapper);
        //获取最终的查询结果
        List<Article> articles = page.getRecords();

        //封装到HotArticleVo
        //优化
        List<HotArticleVO> hotArticleVOS = BeanCopyUtils.copyBeanList(articles, HotArticleVO.class);

        return ResponseResult.okResult(hotArticleVOS);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 判空。如果前端传了categoryId这个参数，那么查询时要和传入的相同
        if (categoryId != null && categoryId > 0) {
            lambdaQueryWrapper.eq(Article::getCategoryId, categoryId);
        }
        //只查询状态是正式发布的文章。Article实体类的status字段跟0作比较，一样就表示是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);

        //对isTop字段进行降序排序，实现置顶的文章(isTop值为1)在最前面
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Article> articles = page.getRecords();
        // 添加文章的categoryName字段
        articles.stream()
                .filter(article -> article.getCategoryId() != null)
                .forEach(article -> {
                    String categoryName = categoryService.getById(article.getCategoryId()).getName();
                    article.setCategoryName(categoryName);
                });

        //把最后的查询结果封装成ArticleListVo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        //把上面那行的查询结果和文章总数封装在PageVo
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章详情
        Article article = getById(id);

        //获取redis中的文章浏览量
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());

        //封装到ArticleDetailVo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyProperties(article, ArticleDetailVo.class);
        if (articleDetailVo != null) {
            //根据分类id查询分类名
            Long categoryId = articleDetailVo.getCategoryId();
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                if (category != null) {
                    articleDetailVo.setCategoryName(category.getName());
                }
            }
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Autowired
    private RedisCache redisCache;
    // 更新文章浏览量到Redis
    @Override
    public ResponseResult updateViewCount(Long id) {
        /*
          key 操作的是哪个hash结构 article:viewCount
          hKey 对hash结构里面的哪个key进行操作 id
          hkey对应的value值会递增多少 1
         */
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleVoService articleVoService;
    @Override
    public ResponseResult addArticle(AddArticleDto articleDto) {
        //属性拷贝
        ArticleVo articleVo = BeanCopyUtils.copyProperties(articleDto, ArticleVo.class);
        articleVoService.save(articleVo);
        //创建多个 ArticleTag 对象
        List<ArticleTag> tags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleVo.getId(), tagId))
                .collect(Collectors.toList());
        //保存文章Tags
        articleTagService.saveBatch(tags);
        return ResponseResult.okResult();
    }

    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        return new PageVo(page.getRecords(), page.getTotal());
    }

    // 根据id查询文章信息
    @Override
    public ArticleByIdVo getInfo(Long id) {
        Article article = getById(id);
        // 根据文章id查询文章标签
        List<ArticleTag> articleTags = articleTagService.list(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, article.getId()));
        List<Long> tags = articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toList());
        // 封装到ArticleByIdVo
        ArticleByIdVo articleByIdVo = new ArticleByIdVo(article.getId(), article.getTitle(), article.getContent(), article.getSummary(), article.getCategoryId(), article.getThumbnail(), article.getIsTop(), article.getStatus(), article.getViewCount(), article.getIsComment(), tags);
        articleByIdVo.setTags(tags);
        return articleByIdVo;
    }
    // 修改文章
    @Override
    public void edit(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyProperties(articleDto, Article.class);
        updateById(article);
        // 删除文章标签
        articleTagService.remove(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, article.getId()));
        // 创建多个 ArticleTag 对象
        List<ArticleTag> tags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(tags);
    }
}
