package com.qf.electronic.handler;

import com.alibaba.fastjson.JSONObject;
import com.qf.electronic.result.ResponseState;
import com.qf.electronic.result.Result;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 认证成功的处理器
 */

@Component //标识为一个组件，SpringBootApplication注解可以扫描到该注解，然后就纳入Spring IOC容器
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        JSONObject json = (JSONObject) JSONObject.toJSON(Result.ok(1));
        writer.print(json);
        writer.flush();
        writer.close();
    }
}
