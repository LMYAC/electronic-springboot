package com.qf.electronic.service;

import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.TowerCondition;
import com.qf.electronic.pojo.Tower;

import java.util.List;
import java.util.Map;

public interface TowerService {

    PageInfo<Tower> searchTowers(TowerCondition condition);

    Integer updateTowers(Map<String, Object> params);

    String getTowerId(String orders);

    String getTowerId(String startNumber, int towNum);

    Integer addTower(Tower tower);

    Integer deleteTowers(List<String> towerIds);
}
