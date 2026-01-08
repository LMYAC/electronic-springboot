package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LogCondition;
import com.qf.electronic.pojo.Log;


public interface LogService {

    int addLog(Log log);

    PageInfo<Log> searchLog(LogCondition condition);
}
