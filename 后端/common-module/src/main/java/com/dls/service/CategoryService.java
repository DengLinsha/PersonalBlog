package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Category;
import com.dls.domain.vo.CategoryVo;
import com.dls.domain.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author denglinsha
 * @since 2023-12-11 17:03:24
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    // 写博客-查询文章分类的接口
    List<CategoryVo> listAllCategory();

    // 分页查询分类列表
    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);
}
