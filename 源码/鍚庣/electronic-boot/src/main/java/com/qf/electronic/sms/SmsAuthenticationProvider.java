package com.qf.electronic.sms;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

public class SmsAuthenticationProvider implements AuthenticationProvider {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SmsAuthenticationToken.class, authentication,
                () -> messages.getMessage("SmsAuthenticationProvider.onlySupports",
                        "Only SmsAuthenticationToken is supported"));
        String mobile = authentication.getPrincipal().toString();
        //根据手机号码去数据库将用户信息查询出来
        UserDetails user = userDetailsService.loadUserByUsername(mobile);
        //认证前的检测
        preAuthenticationCheck(user);
        //认证后的检测
        postAuthenticationCheck(user);
        return createSuccessAuthentication(user, authentication, user);
    }

    private Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return new SmsAuthenticationToken(principal,authentication.getCredentials(), user.getAuthorities());
    }

    private void postAuthenticationCheck(UserDetails user) {
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(messages.getMessage(
                    "SmsAuthenticationProvider.credentialsExpired",
                    "User credentials have expired"));
        }
    }

    /**
     * 认证前的数据检测
     *
     * @param user
     */
    private void preAuthenticationCheck(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException(messages.getMessage(
                    "SmsAuthenticationProvider.locked",
                    "User account is locked"));
        }

        if (!user.isEnabled()) {
            throw new DisabledException(messages.getMessage(
                    "SmsAuthenticationProvider.disabled",
                    "User is disabled"));
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException(messages.getMessage(
                    "SmsAuthenticationProvider.expired",
                    "User account has expired"));
        }
    }


    /**
     * 表示支持验证的票据类型，这里只能支持手机号和验证码的票据
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == SmsAuthenticationToken.class;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
