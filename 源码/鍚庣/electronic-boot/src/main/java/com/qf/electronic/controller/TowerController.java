package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.TowerCondition;
import com.qf.electronic.pojo.Tower;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.TowerService;
import com.qf.electronic.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tower")
public class TowerController {

    @Autowired
    private TowerService towerService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<PageInfo<Tower>> searchTowers(TowerCondition condition){
        return Result.ok(towerService.searchTowers(condition));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> addTower(@RequestBody Tower tower){
        tower.setId(IdGenerator.generateId("ET"));
        return Result.ok(towerService.addTower(tower));
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> updateTowers(@RequestBody Map<String,Object> params){
        return Result.ok(towerService.updateTowers(params));
    }

    @GetMapping("/{orders}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<String> getTowerId(@PathVariable("orders")String orders){
        return Result.ok(towerService.getTowerId(orders));
    }

    @GetMapping("/{startNumber}/{towNum}")
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<String> getTowerId(@PathVariable("startNumber")String startNumber, @PathVariable("towNum") int towNum){
        return Result.ok(towerService.getTowerId(startNumber, towNum));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_LINE')")
    public Result<Integer> deleteTowers(@RequestBody Map<String,Object> params){
        List<String> towerIds = (List<String>) params.get("towerIds");
        return Result.ok(towerService.deleteTowers(towerIds));
    }
}
