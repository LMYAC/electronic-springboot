package com.qf.electronic.mapper;

import com.qf.electronic.condition.LogCondition;
import com.qf.electronic.pojo.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {

    int addLog(@Param("log") Log log);

    List<Log> searchLog(@Param("condition") LogCondition condition);
}
