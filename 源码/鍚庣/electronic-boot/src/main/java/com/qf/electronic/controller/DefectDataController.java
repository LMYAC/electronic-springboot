package com.qf.electronic.controller;

import com.qf.electronic.pojo.DefectData;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.DefectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/defectData")
public class DefectDataController {

    @Autowired
    private DefectDataService defectDataService;

    @GetMapping("/{dataType}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<List<DefectData>> getDefectData(@PathVariable("dataType")String dataType){
        return Result.ok(defectDataService.getDefectData(dataType));
    }

    @PostMapping("/{dataType}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> addDefectData(@PathVariable("dataType")String dataType, @RequestBody Map<String,Object> params){
        return Result.ok(defectDataService.addDefectData(dataType, params));
    }

    @PutMapping("/{dataType}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> updateDefectData(@PathVariable("dataType")String dataType, @RequestBody Map<String,Object> params){
        return Result.ok(defectDataService.updateDefectData(dataType, params));
    }

    @DeleteMapping("/{dataType}/{id}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> deleteDefectData(@PathVariable("dataType")String dataType, @PathVariable("id")int id){
        return Result.ok(defectDataService.deleteDefectData(dataType, id));
    }
}
