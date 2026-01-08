package com.qf.electronic.service.impl;

import com.qf.electronic.condition.RoleCondition;
import com.qf.electronic.mapper.RoleMapper;
import com.qf.electronic.pojo.Role;
import com.qf.electronic.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Transactional(readOnly = true)
    @Override
    public List<Role> getRoles(RoleCondition condition) {
        return roleMapper.getRoles(condition);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateRole(Map<String, Object> params) {
        return roleMapper.updateRole(params);
    }
}
