package com.dls.controller;

import com.dls.annotation.mySystemlog;
import com.dls.domain.ResponseResult;
import com.dls.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
// 设置请求路径
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        // 查询热门文章
       ResponseResult result =  articleService.hotArticleList();
       return result;
    }

    @GetMapping("/articleList")
    public ResponseResult ArticleList(Integer pageNum, Integer pageSize, Long categoryId) {
        return articleService.ArticleList(pageNum, pageSize, categoryId);

    }

    @GetMapping("/{id}")
    // 获取路径参数
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {

        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    @mySystemlog(businessName = "根据文章id从mysql查询文章")//接口描述，用于'日志记录'功能
    public ResponseResult updateViewCount(@PathVariable("id") Long id){

        return articleService.updateViewCount(id);
    }
}
