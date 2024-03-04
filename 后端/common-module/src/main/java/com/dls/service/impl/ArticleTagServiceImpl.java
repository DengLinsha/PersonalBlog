package com.dls.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.dao.ArticleTagDao;
import com.dls.domain.entity.ArticleTag;
import com.dls.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author denglinsha
 * @since 2023-12-18 10:13:36
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagDao, ArticleTag> implements ArticleTagService {

}
