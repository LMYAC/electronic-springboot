package com.qf.electronic.mapper;

import com.qf.electronic.condition.TowerCondition;
import com.qf.electronic.pojo.Tower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TowerMapper {

    List<Tower> searchTowers(@Param("condition") TowerCondition condition);

    Integer updateTowers(@Param("params") Map<String, Object> params);

    String getTowerId(@Param("orders") String orders);

    String getStartOrders(@Param("towerId") String towerId);

    Integer addTower(@Param("tower") Tower tower);

    Integer updateTowerLineId(@Param("startNumber")String staterNumber, @Param("endNumber")String endNumber, @Param("lineId")String lineId);

    Integer deleteTowers(@Param("towerIds") List<String> towerIdS);

    Integer removeLineId(@Param("lineId") String lineId);
}
