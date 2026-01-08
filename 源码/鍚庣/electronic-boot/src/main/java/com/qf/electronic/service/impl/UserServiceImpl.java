package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.UserCondition;
import com.qf.electronic.condition.UserItemCondition;
import com.qf.electronic.mapper.UserMapper;
import com.qf.electronic.pojo.Menu;
import com.qf.electronic.pojo.User;
import com.qf.electronic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        //从数据库中将用户信息查询出来
        User user = userMapper.getUserByUsername(username);
        if(user == null) throw new UsernameNotFoundException("用户名不存在");
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String,Object> getInitInfo(User user) {
        List<Menu> menus = userMapper.getUserMenus(user.getUsername());
        Map<String,Object> result = new HashMap<>();
        result.put("user", user);
        result.put("menus", menus);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<User> searchUsers(UserCondition userCondition) {
        PageHelper.startPage(userCondition.getCurrentPage(), userCondition.getDisplayCount());
        List<User> users = userMapper.getUsers(userCondition);
        return new PageInfo<>(users);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addUser(Map<String,Object> params) {
        return userMapper.addUser(params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteUser(List<String> usernames) {
        return userMapper.deleteUser(usernames);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int changeUserState(Map<String, Object> params) {
        return userMapper.changeUserState(params);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateUser(Map<String, Object> params) {
        return userMapper.updateUser(params);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getExportUsers(UserCondition userCondition) {
        return userMapper.getUsers(userCondition);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateUserRole(String username, Integer roleId) {
        return userMapper.updateUserRole(username, roleId);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<User> getUserItems(UserItemCondition userItemCondition) {
        PageHelper.startPage(userItemCondition.getCurrentPage(),userItemCondition.getDisplayCount());
        List<User> users = userMapper.getUserItems(userItemCondition);
        return new PageInfo<>(users);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveData(List<User> dataList) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("123456");
        return userMapper.batchSave(dataList, password);
    }
}
