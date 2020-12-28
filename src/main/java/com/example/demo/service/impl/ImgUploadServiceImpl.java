package com.example.demo.service.impl;

import com.example.demo.Util.ImgUploadUtil;
import com.example.demo.Util.JwtUtil;
import com.example.demo.dao.BookMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.dto.Book;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ExceptionEnum;
import com.example.demo.service.ImgUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@Service
public class ImgUploadServiceImpl  implements ImgUploadService {
    private Logger log= LoggerFactory.getLogger(getClass());
    @Autowired
    BookMapper bookMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public String uploadBookImg(HttpServletRequest request) throws IOException {
        MultipartRequest  multipartRequest=(MultipartRequest)request;
        List<MultipartFile> list=multipartRequest.getFiles("img");
        MultipartFile multipartFile=list.get(0);
        String filePath= ImgUploadUtil.filePath +multipartFile.getOriginalFilename();
        byte [] bytes=multipartFile.getBytes();
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.close();
        return ImgUploadUtil.booKUrl+multipartFile.getOriginalFilename();
    }

    @Override
    public File downloadBookImg(String bookId) throws CustomException {
        Book book=bookMapper.selectBookById(bookId);
        String url=book.getUrl();
        File file=new File(url);
        if(!file.exists()) throw new CustomException(ExceptionEnum.VALID_IMG,"downloadBookImg");
        return file;
    }

    @Override
    public String uploadUserImg(HttpServletRequest request) throws IOException {
        MultipartRequest multipartRequest=(MultipartRequest)request;
        List<MultipartFile> list=multipartRequest.getFiles("img");
        MultipartFile file=list.get(0);
        String filePath=ImgUploadUtil.filePath +file.getOriginalFilename();
        byte [] bytes=file.getBytes();
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.close();
        String uid= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        userMapper.updateUserImg(uid,ImgUploadUtil.booKUrl+file.getOriginalFilename());
        return ImgUploadUtil.booKUrl+file.getOriginalFilename();
    }
}
