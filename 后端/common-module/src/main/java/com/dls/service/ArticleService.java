package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.ResponseResult;
import com.dls.domain.dto.AddArticleDto;
import com.dls.domain.dto.ArticleDto;
import com.dls.domain.entity.Article;
import com.dls.domain.vo.ArticleByIdVo;
import com.dls.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    // 分类查询文章列表
    ResponseResult ArticleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    //新增博客文章
    ResponseResult add(AddArticleDto article);

    // 管理后台(文章管理)-分页查询文章
    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    //修改文章- 1.根据文章id查询对应的文章
    ArticleByIdVo getInfo(Long id);

    //修改文章- 2.然后才是修改文章
    void edit(ArticleDto article);
}
