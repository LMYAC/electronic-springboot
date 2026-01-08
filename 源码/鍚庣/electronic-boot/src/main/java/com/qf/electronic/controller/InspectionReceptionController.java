package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.pojo.InspectionReception;
import com.qf.electronic.pojo.User;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.InspectionReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/inspectionReception")
public class InspectionReceptionController {

    @Autowired
    private InspectionReceptionService inspectionReceptionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_INSEPECTION')")
    public Result<Integer> addInspectionReception(@RequestBody Map<String,Object> params, HttpSession session){
        params.put("user", session.getAttribute("user"));
        return Result.ok(inspectionReceptionService.addInspectionReception(params));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_INSEPECTION', 'ROLE_LINE')")
    public Result<PageInfo<InspectionReception>> searchInspectionReception(InspectionTaskCondition condition, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        if(user.getRoleId() == 3){
            condition.setUser(user.getUsername());
        } else {
            condition.setStates(new int[]{1, 2, 3});
        }
        return Result.ok(inspectionReceptionService.searchInspectionReception(condition));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_INSEPECTION', 'ROLE_LINE')")
    public Result<Integer> updateInspectionReception(@RequestBody Map<String,Object> params){
        return Result.ok(inspectionReceptionService.updateInspectionReception(params));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_INSEPECTION')")
    public Result<Integer> deleteInspectionReception(@PathVariable("id") Long id){
        return Result.ok(inspectionReceptionService.deleteInspectionReception(id));
    }
}
