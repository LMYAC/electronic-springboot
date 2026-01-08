package com.qf.electronic.mapper;

import com.qf.electronic.condition.InspectionTaskCondition;
import com.qf.electronic.pojo.InspectionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface InspectionTaskMapper {

    int addInspectionTask(@Param("params") Map<String,Object> params);

    List<InspectionTask> searchInspectionTask(@Param("condition") InspectionTaskCondition condition);

    Integer updateInspectionTask(@Param("params") Map<String, Object> params);

    Integer deleteInspectionTask(@Param("id") Long id);

}
