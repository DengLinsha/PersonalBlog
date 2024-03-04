package com.dls.runner;

import com.dls.dao.ArticleDao;
import com.dls.domain.entity.Article;
import com.dls.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
    //用于操作数据库的article表
    private ArticleDao articleDao;

    @Override
    public void run(String... args) throws Exception {
        // 1.查询数据库中的博客信息，注意只需要查询id、viewCount字段的博客信息
        List<Article> articles = articleDao.selectList(null);//为null即无条件，表示查询所有
        Map<String, Integer> viewCountMap = new HashMap<>();
        for (Article article : articles) {
            // 如果之前已经存在相同的id，则put方法会返回之前的value值，如果不为空，则表示出现了重复的id，抛出IllegalStateException异常。
            if (viewCountMap.put(article.getId().toString(), article.getViewCount().intValue()) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }

        //把查询到的id作为key，viewCount作为value，一起存入Redis
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}