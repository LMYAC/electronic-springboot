package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.TowerCondition;
import com.qf.electronic.mapper.TowerMapper;
import com.qf.electronic.pojo.Tower;
import com.qf.electronic.service.TowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TowerServiceImpl implements TowerService {

    @Autowired
    private TowerMapper towerMapper;

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Tower> searchTowers(TowerCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<Tower> towers = towerMapper.searchTowers(condition);
        return new PageInfo<>(towers);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateTowers(Map<String, Object> params) {
        return towerMapper.updateTowers(params);
    }

    @Transactional(readOnly = true)
    @Override
    public String getTowerId(String orders) {
        return towerMapper.getTowerId(orders);
    }

    @Transactional(readOnly = true)
    @Override
    public String getTowerId(String startNumber, int towNum) {
        String startOrders = towerMapper.getStartOrders(startNumber);
        //CN0001
        String prefix = startOrders.substring(0, startOrders.length() -4);
        int order = Integer.parseInt(startOrders.substring(startOrders.length() - 4));
        //101
        int endOrder = order + towNum;
        String str = Integer.toString(endOrder);
        while (str.length() < 4){
            str = "0" + str;
        }
        String result = prefix + str;
        return towerMapper.getTowerId(result);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addTower(Tower tower) {
        return towerMapper.addTower(tower);
    }

    @Transactional
    @Override
    public Integer deleteTowers(List<String> towerIds) {
        return towerMapper.deleteTowers(towerIds);
    }
}
