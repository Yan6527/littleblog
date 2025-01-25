package com.atyan.controller;
 

import com.atyan.annotation.mySystemlog;
import com.atyan.domain.ResponseResult;
import com.atyan.service.OssUploadService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Slf4j
public class UploadController {
    @Autowired
    private OssUploadService ossUploadService;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    @mySystemlog(businessName = "上传文件")
    public ResponseResult upload(MultipartFile img) {
        return ossUploadService.uploadImg(img);
    }
}