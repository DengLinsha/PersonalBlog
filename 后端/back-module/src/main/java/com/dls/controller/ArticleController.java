package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.domain.dto.AddArticleDto;
import com.dls.domain.dto.ArticleDto;
import com.dls.domain.entity.Article;
import com.dls.domain.vo.ArticleByIdVo;
import com.dls.domain.vo.PageVo;
import com.dls.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    //新增博客文章
    @Autowired
    private ArticleService articleService;

    @PostMapping
//    @PreAuthorize("@ps.hasPermission('content:article:writer')")//权限控制
    public ResponseResult add(@RequestBody AddArticleDto article) {

        return articleService.add(article);
    }

    // 分页查询博客文章
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize){
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    //根据文章id来修改文章
    @GetMapping(value = "/{id}")
    // 1.先查询根据文章id查询对应的文章
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        ArticleByIdVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    @PutMapping
    // 2.然后才是修改文章
    public ResponseResult edit(@RequestBody ArticleDto article){
        articleService.edit(article);
        return ResponseResult.okResult();
    }

    // 通过文章id删除文章
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        //直接使用mybatisplus提供的removeById方法
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}