package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LoginInfo;
import com.qf.electronic.condition.UserCondition;
import com.qf.electronic.condition.UserItemCondition;
import com.qf.electronic.excel.ExcelProcessor;
import com.qf.electronic.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends ExcelProcessor<User>, UserDetailsService {

    Map<String,Object> getInitInfo(User user);

    PageInfo<User> searchUsers(UserCondition userCondition);

    int addUser(Map<String,Object> params);

    int deleteUser(List<String> usernames);

    int changeUserState(Map<String, Object> params);

    int updateUser(Map<String, Object> params);

    List<User> getExportUsers(UserCondition userCondition);

    int updateUserRole(String username, Integer roleId);

    PageInfo<User> getUserItems(UserItemCondition userItemCondition);
}
