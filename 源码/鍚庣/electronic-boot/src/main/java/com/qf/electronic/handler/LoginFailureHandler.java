package com.qf.electronic.handler;

import com.alibaba.fastjson.JSONObject;
import com.qf.electronic.result.Result;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        int flag;
        if(e instanceof UsernameNotFoundException){//账号不存在
            flag = -1;
        } else if(e instanceof BadCredentialsException){//凭证坏了
            flag = 0;
        } else if (e instanceof AccountStatusException){//账号状态异常
            flag = -2;
        } else {
            flag = -3;
        }
        JSONObject json = (JSONObject) JSONObject.toJSON(Result.ok(flag));
        writer.print(json);
        writer.flush();
        writer.close();
    }
}
