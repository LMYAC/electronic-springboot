package com.qf.electronic.service;

import com.qf.electronic.condition.RoleCondition;
import com.qf.electronic.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {

    List<Role> getRoles(RoleCondition condition);

    Integer updateRole(Map<String, Object> params);
}
