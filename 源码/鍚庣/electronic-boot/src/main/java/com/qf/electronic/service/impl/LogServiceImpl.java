package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LogCondition;
import com.qf.electronic.mapper.LogMapper;
import com.qf.electronic.pojo.Log;
import com.qf.electronic.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addLog(Log log) {
        return logMapper.addLog(log);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Log> searchLog(LogCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<Log> logs = logMapper.searchLog(condition);
        return new PageInfo<>(logs);
    }
}
