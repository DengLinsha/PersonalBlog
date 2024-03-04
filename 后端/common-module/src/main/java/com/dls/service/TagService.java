package com.dls.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dls.domain.ResponseResult;
import com.dls.domain.dto.TagListDto;
import com.dls.domain.entity.Tag;
import com.dls.domain.vo.PageVo;
import com.dls.domain.vo.TagVo;

import java.util.List;

public interface TagService extends IService<Tag> {
    //查询标签列表
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getLableById(Long id);

    ResponseResult myUpdateById(TagVo tagVo);

    //写博文-查询文章标签的接口
    List<TagVo> listAllTag();

}