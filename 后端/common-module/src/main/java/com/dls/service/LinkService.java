package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Link;
import com.dls.domain.vo.PageVo;

/**
 * 友链(Link)表服务接口
 *
 * @author denglinsha
 * @since 2023-12-13 10:31:14
 */
public interface LinkService extends IService<Link> {

    //查询友链
    ResponseResult getAllLink();

    //分页查询友链
    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}
