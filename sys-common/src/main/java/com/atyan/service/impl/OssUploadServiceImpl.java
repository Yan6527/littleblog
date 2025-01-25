package com.atyan.service.impl;

import com.atyan.domain.ResponseResult;
import com.atyan.enums.AppHttpCodeEnum;
import com.atyan.service.OssUploadService;
import com.atyan.utils.AliOssUtil;
import com.atyan.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class OssUploadServiceImpl implements OssUploadService {
    @Autowired
    private AliOssUtil aliOssUtil;
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        log.info("文件上传：{}",img);

        try {
            //原始文件名
            String originalFilename = img.getOriginalFilename();
            //截取原始文件名的后缀.png .jpg
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(img.getBytes(), objectName);
            return ResponseResult.okResult(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
        }

       return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"上传失败");
    }

}

