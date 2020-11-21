package com.example.demo.service.impl;

import com.example.demo.Util.ImgUploadUtil;
import com.example.demo.dao.BookMapper;
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

    @Override
    public void uploadBookImg(HttpServletRequest request) throws IOException {
        MultipartRequest  multipartRequest=(MultipartRequest)request;
        List<MultipartFile> list=multipartRequest.getFiles("img");
        MultipartFile multipartFile=list.get(0);
        String filePath= ImgUploadUtil.filePath +multipartFile.getOriginalFilename();
        byte [] bytes=multipartFile.getBytes();
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(new File(filePath)));
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.close();
        String bookId=request.getParameter("bookId");
        bookMapper.updateUrlById(bookId,ImgUploadUtil.booKUrl+multipartFile.getOriginalFilename());
    }

    @Override
    public File downloadBookImg(String bookId) throws CustomException {
        Book book=bookMapper.selectBookById(bookId);
        String url=book.getUrl();
        File file=new File(url);
        if(!file.exists()) throw new CustomException(ExceptionEnum.VALID_IMG,"downloadBookImg");
        return file;
    }
}
