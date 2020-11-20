package com.example.demo.service.impl;

import com.example.demo.service.ImgUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImgUploadServiceImpl  implements ImgUploadService {
    @Override
    public void uploadBookImg(HttpServletRequest request) throws IOException {
        MultipartRequest  multipartRequest=(MultipartRequest)request;
        List<MultipartFile> list=multipartRequest.getFiles("img");
        MultipartFile multipartFile=list.get(0);
        multipartFile.transferTo(new File("D:\\"+multipartFile.getOriginalFilename()));
    }
}
