package com.qf.electronic.mapper;

import com.qf.electronic.condition.RoleCondition;
import com.qf.electronic.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleMapper {

    List<Role> getRoles(@Param("condition") RoleCondition condition);

    Integer updateRole(@Param("params") Map<String, Object> params);

}
