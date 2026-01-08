package com.qf.electronic.mapper;

import com.qf.electronic.condition.DefectTaskCondition;
import com.qf.electronic.pojo.DefectTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DefectTaskMapper {

    int addDefectTask(@Param("params") Map<String,Object> params);

    List<DefectTask> searchDefectTask(@Param("condition")DefectTaskCondition condition);

    Integer updateDefectTask(@Param("params") Map<String, Object> params);

}
