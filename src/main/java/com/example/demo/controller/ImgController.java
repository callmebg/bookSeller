package com.example.demo.controller;


import com.example.demo.Util.ResponseGenerator;
import com.example.demo.dao.BookMapper;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.exception.CustomException;
import com.example.demo.service.ImgUploadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
public class ImgController {

    @Autowired
    ImgUploadService imgUploadService;
    @Autowired
    BookMapper bookMapper;

    @PostMapping("/bookImgUpload")
    public SuccessResponse bookImgUpload(HttpServletRequest request) throws IOException {
        String imageUrl=imgUploadService.uploadBookImg(request);
        return ResponseGenerator.getSuccessResponse(imageUrl);
    }

    @PostMapping("/userImgUpload")
    public SuccessResponse userImgUpload(HttpServletRequest request) throws  IOException{
        String imageUrl=imgUploadService.uploadUserImg(request);
        return ResponseGenerator.getSuccessResponse(imageUrl);
    }

    @ApiOperation("书绑定图片")
    @PostMapping("/bookBindImg")
    public SuccessResponse bookBindImg(@RequestParam("bookId") String bookId,@RequestParam("url") String url)
    {
        bookMapper.updateUrlById(bookId,url);
        return ResponseGenerator.getSuccessResponse();
    }


 //   @PostMapping("/getBookImg")
 //   public SuccessResponse getBookImg(@RequestParam("bookId")String bookId) throws CustomException {
  //      File file=imgUploadService.downloadBookImg(bookId);
 //       return ResponseGenerator.getSuccessResponse(file);
 //   }
}
