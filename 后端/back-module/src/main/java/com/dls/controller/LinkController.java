package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.domain.entity.Link;
import com.dls.domain.vo.PageVo;
import com.dls.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    //分页查询友链
    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize) {
        PageVo pageVo = linkService.selectLinkPage(link,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    // 增加友链
    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    // 修改友链
    @GetMapping(value = "/{id}")
    //1.根据id查询友链
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    @PutMapping("/changeLinkStatus")
    //2.修改友链状态
    public ResponseResult changeLinkStatus(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @PutMapping
    //3.修改友链
    public ResponseResult edit(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    // 删除友链
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}