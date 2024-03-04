package com.dls.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.dao.ArticleVoDao;
import com.dls.domain.vo.ArticleVo;
import com.dls.service.ArticleVoService;
import org.springframework.stereotype.Service;

@Service
public class ArticleVoServiceImpl extends ServiceImpl<ArticleVoDao, ArticleVo> implements ArticleVoService {

}