package com.dls.job;

import com.dls.domain.entity.Article;
import com.dls.service.ArticleService;
import com.dls.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
//通过定时任务实现每隔3分钟把redis中的浏览量更新到mysql数据库中
public class UpdateViewCount {

    @Autowired
    //操作redis。
    private RedisCache redisCache;

    @Autowired
    //操作数据库。
    private ArticleService articleService;

    //每隔10分钟，把redis的浏览量数据更新到mysql数据库
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void updateViewCount(){
        // 1.获取redis中的浏览量，注意得到的viewCountMap是HashMap双列集合
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //让双列集合调用entrySet方法即可转为单列集合，然后才能调用stream方法
        //把最终数据转为List集合
        List<Article> articles = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : viewCountMap.entrySet()) {
            Article article = new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue());
            articles.add(article);
        }
        // 2.把获取到的浏览量更新到mysql数据库中。updateBatchById是mybatisplus提供的批量操作数据的接口
        articleService.updateBatchById(articles);
        //方便在控制台看打印信息
//        System.out.println("redis的文章浏览量数据已更新到数据库，现在的时间是: "+ LocalTime.now());
    }
}