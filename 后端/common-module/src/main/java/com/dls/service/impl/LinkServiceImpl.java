package com.dls.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dls.constants.SystemConstants;
import com.dls.dao.LinkDao;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Link;
import com.dls.domain.vo.LinkVo;
import com.dls.domain.vo.PageVo;
import com.dls.service.LinkService;
import com.dls.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author denglinsha
 * @since 2023-12-13 10:31:15
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkDao, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的链接
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_NORMAL);
        List<Link> linkList = list(linkLambdaQueryWrapper);
        // 转换成Vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        // 封装返回
        return ResponseResult.okResult(linkVos);
    }

    // 分页查询友链
    @Override
    public PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(link.getName()),Link::getName, link.getName());
        queryWrapper.eq(Objects.nonNull(link.getStatus()),Link::getStatus, link.getStatus());

        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Link> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}
