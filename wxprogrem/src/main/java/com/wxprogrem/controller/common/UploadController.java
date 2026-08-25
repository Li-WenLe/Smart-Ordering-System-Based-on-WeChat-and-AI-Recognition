package com.wxprogrem.controller.common;

import com.wxprogrem.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping                                                                                       //ResponseEntity：Spring 提供的响应封装类
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {                  //MultipartFile：Spring 封装的文件上传对象
        try {
            String objectName = "images/" + UUID.randomUUID() + "_" + file.getOriginalFilename();         //于获取上传文件的原始文件名
            String url = aliOssUtil.upload(file.getBytes(), objectName);                                  //生成图片路径url
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");            //返回500状态码
        }
    }
}