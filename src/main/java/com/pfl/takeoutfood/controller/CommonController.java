package com.pfl.takeoutfood.controller;


import com.pfl.takeoutfood.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;


/**
 * 文件上传 / 下载
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    /**
     * 使用 @Value 注解从配置文件注入要转存的目录
     */
    @Value("${take-out-food.img}")
    private String basePath;


    /**
     * 文件上传
     * @param file 上传的文件
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file) {

        log.info(file.toString());

        File dir = new File(basePath);

        // 如果目标目录不存在则创建
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 获取原始文件名的后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));


        // 使用 uuid 生成新的文件名，防止上传的文件名重复，导致之前的文件被覆盖
        String fileName = UUID.randomUUID().toString();


        // 转存文件 在配置文件中设置转存目录
        try {
            file.transferTo(new File(basePath + fileName + suffix));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return BaseResponse.success(fileName + suffix);
    }


    /**
     * 文件下载
     * @param name 要下载文件名称
     * @param response 将文件数据流写入 response
     */

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            // 将要下载文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 将读入到内存的文件流写入输出流
            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = fileInputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
