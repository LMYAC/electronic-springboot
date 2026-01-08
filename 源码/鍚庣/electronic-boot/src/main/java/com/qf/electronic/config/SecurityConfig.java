package com.qf.electronic.config;

import com.qf.electronic.handler.LoginFailureHandler;
import com.qf.electronic.handler.LoginSuccessHandler;
import com.qf.electronic.handler.RequestAccessDeniedHandler;
import com.qf.electronic.service.UserService;
import com.qf.electronic.service.impl.MobileServiceImpl;
import com.qf.electronic.sms.SmsAuthenticationFilter;
import com.qf.electronic.sms.SmsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

//WebSecurityConfigurerAdapter 表示WebSecurity配置适配器，为什么叫适配，因为该类
//中已经提供了一些默认的配置，如果用户需求修改，那么只需要重写要修改的配置即可
@EnableWebSecurity //启用WebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private UserService userService;

    @Resource(name = "mobileService") //指定名称注入
    private UserDetailsService userDetailsService;

    @Bean
    public SmsAuthenticationFilter smsAuthenticationFilter() throws Exception {
        SmsAuthenticationFilter filter = new SmsAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        return filter;
    }

    //HTTP请求的认证配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用跨站请求模拟
        http.csrf().disable();
        //要授权的请求: 对于"/login", "/sms", "/user/code/**"这三种形式的请求授予的权限是放行，其余任何请求都需要经过认证
        http.authorizeRequests().antMatchers("/login", "/sms", "/user/code/**").permitAll().anyRequest().authenticated();
        http.formLogin().usernameParameter("username").passwordParameter("password")
                //配置登录成功和登录失败的处理器
                .successHandler(loginSuccessHandler).failureHandler(loginFailureHandler).permitAll();
        //配置HTTP请求访问的异常处理：访问被拒绝的处理器配置
        http.exceptionHandling().accessDeniedHandler(new RequestAccessDeniedHandler());
        //登录的时候使session失效
        http.logout().invalidateHttpSession(true);
        //添加用户自定义的认证方式
        http.addFilterBefore(smsAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider1 = new DaoAuthenticationProvider();
        provider1.setUserDetailsService(userService);
        provider1.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(provider1); //加入一个认证提供器

        SmsAuthenticationProvider provider2 = new SmsAuthenticationProvider();
        provider2.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(provider2); //加入一个认证提供器
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
