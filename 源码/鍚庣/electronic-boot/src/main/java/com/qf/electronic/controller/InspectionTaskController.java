package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.pojo.InspectionTask;
import com.qf.electronic.pojo.User;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.InspectionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/inspectionTask")
public class InspectionTaskController {

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> addInspectionTask(@RequestBody Map<String,Object> params){
        String inspectionUser = (String) params.get("inspectionUser");
        if(StringUtils.hasLength(inspectionUser)){
            params.put("state", 1); //有用户，表示该任务已分配
        } else {
            params.put("state", 0); //没有用户，表示该任务待分配
        }
        return Result.ok(inspectionTaskService.addInspectionTask(params));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_LINE', 'ROLE_INSEPECTION')")
    public Result<PageInfo<InspectionTask>> searchInspectionTask(InspectionTaskCondition condition, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        if(user.getRoleId() == 3){//如果是巡检员，只能查自己的数据
            condition.setUser(user.getUsername());
        }
        return Result.ok(inspectionTaskService.searchInspectionTask(condition));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> updateInspectionTask(@RequestBody Map<String,Object> params){
        String inspectionUser = (String) params.get("inspectionUser");
        if(StringUtils.hasLength(inspectionUser)){
            params.put("state", 1); //有用户，表示该任务已分配
        }
        return Result.ok(inspectionTaskService.updateInspectionTask(params));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> deleteInspectionTask(@PathVariable("id")Long id){
        return Result.ok(inspectionTaskService.deleteInspectionTask(id));
    }

}
