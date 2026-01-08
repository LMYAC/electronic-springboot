package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LineCondition;
import com.qf.electronic.pojo.Line;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.LineService;
import com.qf.electronic.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/line")
public class LineController {

    @Autowired
    private LineService lineService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<PageInfo<Line>> searchLines(LineCondition condition){
        return Result.ok(lineService.searchLines(condition));
    }

    //由于line表的主键ID使用的是字符串，那么需要考虑ID的生成方式
    //字符串类型的ID生成方式常用的有两种：
    //第一种就是使用UUID
    //第二种就是使用雪花算法实现
    @PostMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> addLine(@RequestBody Map<String,Object> params){
        params.put("id", IdGenerator.generateId("EL"));
        if("".equals(params.get("operationTime"))){
            params.remove("operationTime");
        }
        return Result.ok(lineService.addLine(params));
    }

    @DeleteMapping("/{lineId}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> deleteLine(@PathVariable("lineId")String lineId){
        return Result.ok(lineService.deleteLine(lineId));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> updateLines(@RequestBody Map<String,Object> params){
        return Result.ok(lineService.updateLines(params));
    }
}
