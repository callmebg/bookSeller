package com.example.demo.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ImgUploadService {

    void uploadBookImg(HttpServletRequest request) throws IOException;
}
