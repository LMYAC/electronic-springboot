package com.qf.electronic.controller;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LogCondition;
import com.qf.electronic.pojo.Log;
import com.qf.electronic.result.Result;
import com.qf.electronic.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<PageInfo<Log>> search(LogCondition logCondition){
        return Result.ok(logService.searchLog(logCondition));
    }
}
