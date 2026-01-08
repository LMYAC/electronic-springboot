package com.qf.electronic.sms;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//Short Messaging Service
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/sms", "POST");

    public SmsAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String mobile = request.getParameter("mobile");
        String code = request.getParameter("code");
        //验证码的检测 需要在Filter中完成，因为此时security使用的拦截器在DispatcherServlet之前执行
        //不能通过解耦的方式获取到session
        HttpSession session = request.getSession();
        String sendCode = (String) session.getAttribute("code");
        //验证码不正确，抛出坏的凭据异常
        if (!code.equals(sendCode)) throw new BadCredentialsException("验证码不正确");
        //构建SMS认证票据，这个票据的认证状态为未认证
        SmsAuthenticationToken token = new SmsAuthenticationToken(mobile, code);
        //把IP地址和SessionID取出来存在票据中
        token.setDetails(authenticationDetailsSource.buildDetails(request));
        //调用认证管理器来对这个票据进行认证
        return this.getAuthenticationManager().authenticate(token);
    }
}
