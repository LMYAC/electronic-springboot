package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LineCondition;
import com.qf.electronic.pojo.Line;

import java.util.Map;

public interface LineService {

    PageInfo<Line> searchLines(LineCondition condition);

    Integer addLine(Map<String, Object> params);

    Integer deleteLine(String lineId);

    Integer updateLines(Map<String, Object> params);

}
