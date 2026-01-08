package com.qf.electronic.mapper;

import com.qf.electronic.condition.LineCondition;
import com.qf.electronic.pojo.Line;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LineMapper {

    List<Line> searchLines(@Param("condition") LineCondition condition);

    Integer addLine(@Param("lineInfo") Map<String, Object> params);

    int deleteLine(@Param("lineId") String lineId);

    Integer updateLines(@Param("params") Map<String, Object> params);
}
