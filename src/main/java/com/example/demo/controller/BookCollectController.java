package com.example.demo.controller;

import com.example.demo.Util.JwtUtil;
import com.example.demo.Util.ResponseGenerator;
import com.example.demo.dto.SuccessResponse;
import com.example.demo.exception.CustomException;
import com.example.demo.service.BookCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BookCollectController {

    @Autowired
    BookCollectService bookCollectService;

    @PostMapping("/bookCollect")
    public SuccessResponse bookCollect(HttpServletRequest request, @RequestParam("bookId") String bookId) throws CustomException {
        String userId= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        bookCollectService.collectBook(userId,bookId);
        return ResponseGenerator.getSuccessResponse();
    }

    @PostMapping("/cancelCollect")
    public SuccessResponse cancelBookCollect(HttpServletRequest request,@RequestParam("bookId") String bookId) throws CustomException {
        String userId= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        bookCollectService.cancelCollectBook(userId,bookId);
        return  ResponseGenerator.getSuccessResponse();
    }


    @GetMapping("/seeAllBookCollect")
    public SuccessResponse selectAllCollect(HttpServletRequest request)
    {
        String userId= JwtUtil.getUid(request.getHeader("Authorization").substring("Bearer ".length()));
        return ResponseGenerator.getSuccessResponse(bookCollectService.selectAllCollectBook(userId));
    }
}
