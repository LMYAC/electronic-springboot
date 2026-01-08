package com.qf.electronic.service.impl;


import com.qf.electronic.mapper.UserMapper;
import com.qf.electronic.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mobileService")
public class MobileServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        User user = userMapper.getUserByPhone(mobile);
        if(user == null)
            throw new UsernameNotFoundException("账号不存在");
        return user;
    }
}
