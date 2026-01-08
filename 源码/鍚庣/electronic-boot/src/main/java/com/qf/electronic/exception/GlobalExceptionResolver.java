package com.qf.electronic.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//public class GlobalExceptionResolver implements HandlerExceptionResolver {
//
//    @Override
//    public ModelAndView resolveException(HttpServletRequest request,
//                                         HttpServletResponse response,
//                                         Object handler, Exception ex) {
//
//        //AJAX
//        PrintWriter writer = response.getWriter();
//
//        return new ModelAndView("50x");
//    }
//}
