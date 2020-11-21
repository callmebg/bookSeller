package com.example.demo.controller;


import com.example.demo.Util.ResponseGenerator;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.exception.CustomException;
import com.example.demo.service.ImgUploadService;
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

    @PostMapping("/bookImgUpload")
    public SuccessResponse bookImgUpload(HttpServletRequest request) throws IOException {
        imgUploadService.uploadBookImg(request);
        return ResponseGenerator.getSuccessResponse();
    }

 //   @PostMapping("/getBookImg")
 //   public SuccessResponse getBookImg(@RequestParam("bookId")String bookId) throws CustomException {
  //      File file=imgUploadService.downloadBookImg(bookId);
 //       return ResponseGenerator.getSuccessResponse(file);
 //   }
}
