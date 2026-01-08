package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.pojo.DefectTask;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.DefectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/defectTask")
public class DefectTaskController {

    @Autowired
    private DefectTaskService defectTaskService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_LINE','ROLE_DEFECT')")
    public Result<PageInfo<DefectTask>> searchDefectTask(DefectTaskCondition condition){
        return Result.ok(defectTaskService.searchDefectTask(condition));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_LINE','ROLE_DEFECT')")
    public Result<Integer> updateDefectTask(@RequestBody Map<String,Object> params){
        String operationUser = (String) params.get("operationUser");
        if(StringUtils.hasLength(operationUser)){
            params.put("state", 1);
        }
        return Result.ok(defectTaskService.updateDefectTask(params));
    }
}
