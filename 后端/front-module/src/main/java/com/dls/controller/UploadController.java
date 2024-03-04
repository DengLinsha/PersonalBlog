package com.dls.controller;

import com.dls.domain.ResponseResult;
import com.dls.service.OssUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    //MultipartFile是spring提供的接口，ResponseResult是写的实体类
    public ResponseResult uploadImg(MultipartFile img){
        //图片上传到七牛云
        return ossUploadService.uploadImg(img);
    }
}