package com.dls.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.constants.SystemConstants;
import com.dls.dao.CategoryDao;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Article;
import com.dls.domain.entity.Category;
import com.dls.domain.vo.CategoryVo;
import com.dls.domain.vo.PageVo;
import com.dls.service.ArticleService;
import com.dls.service.CategoryService;
import com.dls.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author denglinsha
 * @since 2023-12-11 17:03:25
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    // 查询分类列表
    @Override
    public ResponseResult getCategoryList() {
        // 单表查询
        // 查询文章表，状态为已发布的文章
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);
        // 获取文章分类的id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(new Function<Article, Long>() {
                    @Override
                    public Long apply(Article article) {
                        return article.getCategoryId();
                    }
                })
                .collect(Collectors.toSet());
        // 然后查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                // 使用 filter 方法筛选出状态为正常的分类信息
                .filter(category -> SystemConstants.CATEGORY__STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        // 封装vo
        List<CategoryVo> categoryVos =  BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    // 写博客-查询文章分类的接口
    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    // 分页查询分类列表
    @Override
    public PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(category.getName()),Category::getName, category.getName());
        queryWrapper.eq(Objects.nonNull(category.getStatus()),Category::getStatus, category.getStatus());

        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Category> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}
