package com.qf.electronic.controller;

import com.qf.electronic.condition.RoleCondition;
import com.qf.electronic.pojo.Role;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<List<Role>> getRoles(RoleCondition condition){
        return Result.ok(roleService.getRoles(condition));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Integer> updateRole(@RequestBody Map<String,Object> params){
        Object value = params.get("enableStatus");
        if("".equals(value))
            params.remove("enableStatus");
        return Result.ok(roleService.updateRole(params));
    }
}
