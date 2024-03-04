package com.dls.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.constants.SystemConstants;
import com.dls.domain.ResponseResult;
import com.dls.domain.dto.AddArticleDto;
import com.dls.domain.dto.ArticleDto;
import com.dls.domain.entity.Article;
import com.dls.domain.entity.ArticleTag;
import com.dls.domain.entity.Category;
import com.dls.domain.vo.*;
import com.dls.dao.ArticleDao;
import com.dls.service.ArticleService;
import com.dls.service.ArticleTagService;
import com.dls.service.ArticleVoService;
import com.dls.service.CategoryService;
import com.dls.utils.BeanCopyUtils;
import com.dls.utils.RedisCache;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult hotArticleList() {

        //每调用这个方法就从redis查询文章的浏览量，展示在热门文章列表

        //获取redis中的浏览量，注意得到的viewCountMap是HashMap双列集合
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //让双列集合调用entrySet方法即可转为单列集合，然后才能调用stream方法
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                //把最终数据转为List集合
                .collect(Collectors.toList());
        //把获取到的浏览量更新到mysql数据库中。updateBatchById是mybatisplus提供的批量操作数据的接口
        articleService.updateBatchById(articles);

        // 查询热门文章，然后封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件：正式文章，按照浏览量进行排序，分页（一页最多显示10条）
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page(SystemConstants.ARTICLE_STATUS_CURRENT,SystemConstants.ARTICLE_STATUS_SIZE);
        page(page, queryWrapper);
        articles = page.getRecords();

        // 将articles中的某些变量拷贝到HotArticleVo中
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    // 分页查询文章的列表
    @Override
    public ResponseResult ArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果有categoryId，查询时要与传入的相同
        //判空。如果前端传了categoryId这个参数，那么查询时要和传入的相同。第二个参数是数据表的文章id，第三个字段是前端传来的文章id
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);

        //只查询状态是正式发布的文章。Article实体类的status字段跟0作比较，一样就表示是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);

        //对isTop字段进行升序排序，实现置顶的文章(isTop值为0)在最前面
        lambdaQueryWrapper.orderByAsc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

//        //用categoryId来查询categoryName(category表的name字段)，也就是查询'分类名称'
//        List<Article> articles = page.getRecords();
//        articles.stream()
//                .map(new Function<Article, Article>() {
//                    @Override
//                    public Article apply(Article article) {
//                        //'article.getCategoryId()'表示从article表获取category_id字段，然后作为查询category表的name字段
//                        Category category = categoryService.getById(article.getCategoryId());
//                        String name = category.getName();
//                        //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
//                        article.setCategoryName(name);
//                        return article;
//                    }
//                })
//                .collect(Collectors.toList());

        //把最后的查询结果封装成ArticleListVo。BeanCopyUtils是我们写的工具类
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        //把上面那行的查询结果和文章总数封装在PageVo
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        // 根据id查询文章
        Article article = getById(id);
        // 从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成Vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category categoryServiceById = categoryService.getById(categoryId);
        if (categoryServiceById != null) {
            articleDetailVo.setCategoryName(categoryServiceById.getName());
            // 封装响应返回
            return ResponseResult.okResult(articleDetailVo);
        } else {
            return null;
        }

    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中的浏览量，对应文章id的viewCount浏览量。article:viewCount是ViewCountRunner类里面写的
        //用户每从mysql根据文章id查询一次浏览量，那么redis的浏览量就增加1
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    // 增加博客文章

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleVoService articleVoService;

    @Override
    // 事务操作
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        // 1.添加博客
        ArticleVo articlevo = BeanCopyUtils.copyBean(articleDto, ArticleVo.class);
        articleVoService.save(articlevo);


//        // 把标签Id转换成ArticleTag对象
//        List<ArticleTag> articleTags = new ArrayList<>();
//        for (Long tagId : articleDto.getTags()) {
//            ArticleTag articleTag = new ArticleTag(articlevo.getId(), tagId);
//            articleTags.add(articleTag);
//        }
//
//        // 2.添加博客和标签的关联
//        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    // 管理后台(文章管理)-分页查询文章
    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        // 根据标题输入框和摘要输入框进行模糊查找
        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Article> articles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(articles);
        return pageVo;
    }

    // 修改文章
    // 1.先查询根据文章id查询对应的文章
    @Override
    public ArticleByIdVo getInfo(Long id) {
        Article article = getById(id);
//        //获取关联标签
//        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
//        List<ArticleTag> articleTags = articleTagService.list(articleTagLambdaQueryWrapper);
//        List<Long> tags = new ArrayList<>();
//        for (ArticleTag articleTag : articleTags) {
//            Long tagId = articleTag.getTagId();
//            tags.add(tagId);
//        }

        ArticleByIdVo articleVo = BeanCopyUtils.copyBean(article,ArticleByIdVo.class);
//        articleVo.setTags(tags);
        return articleVo;
    }

    // 2.然后才是修改文章
    @Override
    public void edit(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);
//        //删除原有的 标签和博客的关联
//        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
//        articleTagService.remove(articleTagLambdaQueryWrapper);
//        //添加新的博客和标签的关联信息
//        List<ArticleTag> articleTags = new ArrayList<>();
//        for (Long tagId : articleDto.getTags()) {
//            ArticleTag articleTag = new ArticleTag(articleDto.getId(), tagId);
//            articleTags.add(articleTag);
//        }
//        articleTagService.saveBatch(articleTags);
    }

}
