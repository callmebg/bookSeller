package com.example.demo.service;

import com.example.demo.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public interface ImgUploadService {

    void uploadBookImg(HttpServletRequest request) throws IOException;

    File downloadBookImg(String bookId) throws CustomException;
}
