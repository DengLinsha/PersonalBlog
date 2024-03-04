package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.domain.dto.AddTagDto;
import com.dls.domain.dto.EditTagDto;
import com.dls.domain.dto.TagListDto;
import com.dls.domain.entity.Tag;
import com.dls.domain.vo.PageVo;
import com.dls.domain.vo.TagVo;
import com.dls.service.TagService;
import com.dls.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    //TagService是工程的接口
    private TagService tagService;

    //查询标签列表
    @GetMapping("/list")
    //ResponseResult是工程的实体类
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        //pageTagList是我们在工程写的方法
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    // 新增标签
    @PostMapping
    public ResponseResult add(@RequestBody AddTagDto tagDto){
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    // 删除标签
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    // 修改标签
    @GetMapping("/{id}")
    // 1.根据标签的id来查询标签
    public ResponseResult getLableById(@PathVariable Long id){
        return tagService.getLableById(id);
    }

    @PutMapping
    // 2.根据标签的id来修改标签
    public ResponseResult updateById(@RequestBody TagVo tagVo){
        return tagService.myUpdateById(tagVo);
    }

    //写博文-查询文章标签的接口
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}