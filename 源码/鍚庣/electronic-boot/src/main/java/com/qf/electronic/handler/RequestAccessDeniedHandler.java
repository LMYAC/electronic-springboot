package com.qf.electronic.handler;

import com.alibaba.fastjson.JSONObject;
import com.qf.electronic.result.ResponseState;
import com.qf.electronic.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//访问被拒绝的处理器
public class RequestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject json = (JSONObject) JSONObject.toJSON(Result.error(ResponseState.NO_PERMISSION, "无权访问，请联系管理员"));
        writer.print(json);
        writer.flush();
        writer.close();
    }
}
