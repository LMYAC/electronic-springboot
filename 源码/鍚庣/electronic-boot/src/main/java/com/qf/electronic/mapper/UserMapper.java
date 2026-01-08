package com.qf.electronic.mapper;

import com.qf.electronic.condition.UserCondition;
import com.qf.electronic.condition.UserItemCondition;
import com.qf.electronic.pojo.Menu;
import com.qf.electronic.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    User getUserByUsername(@Param("username") String username);

    User getUserByPhone(@Param("mobile") String mobile);

    List<Menu> getUserMenus(@Param("username") String username);

    List<User> getUsers(@Param("condition") UserCondition userCondition);

    int addUser(@Param("user") Map<String,Object> params);

    int deleteUser(@Param("usernames") List<String> usernames);

    int changeUserState(@Param("params") Map<String, Object> params);

    int updateUser(@Param("params") Map<String, Object> params);

    int batchSave(@Param("users") List<User> users, @Param("password")String defaultPassword);

    int updateUserRole(@Param("username") String username, @Param("roleId") Integer roleId);

    List<User> getUserItems(@Param("condition") UserItemCondition condition);
}
