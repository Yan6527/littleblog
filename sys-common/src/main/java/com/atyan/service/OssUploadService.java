package com.atyan.service;

import com.atyan.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;


public interface OssUploadService {
    //图片上传到aliyun oss
    ResponseResult uploadImg(MultipartFile img);
}