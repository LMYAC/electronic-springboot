package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.pojo.DefectReception;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.DefectReceptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/defectReception")
public class DefectReceptionController {

    @Autowired
    private DefectReceptionService defectReceptionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_DEFECT')")
    public Result<Integer> addDefectReception(@RequestBody Map<String,Object> params, HttpSession session){
        String user = (String) session.getAttribute("user");
        params.put("operationUser", user);
        return Result.ok(defectReceptionService.addDefectReception(params));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_LINE','ROLE_DEFECT')")
    public Result<PageInfo<DefectReception>> searchDefectReceptions(DefectTaskCondition condition){
        return Result.ok(defectReceptionService.searchDefectReceptions(condition));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_LINE','ROLE_DEFECT')")
    public Result<Integer> updateDefectReception(@RequestBody Map<String,Object> params){
        return Result.ok(defectReceptionService.updateDefectReception(params));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_DEFECT')")
    public Result<Integer> deleteDefectReception(@PathVariable("id") long id){
        return Result.ok(defectReceptionService.deleteDefectReception(id));
    }
}
