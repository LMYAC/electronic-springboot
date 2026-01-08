package com.qf.electronic.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.electronic.condition.LineCondition;
import com.qf.electronic.mapper.LineMapper;
import com.qf.electronic.mapper.TowerMapper;
import com.qf.electronic.pojo.Line;
import com.qf.electronic.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LineServiceImpl implements LineService {

    @Autowired
    private LineMapper lineMapper;

    @Autowired
    private TowerMapper towerMapper;

    @Transactional(readOnly = true)
    @Override
    public PageInfo<Line> searchLines(LineCondition condition) {
        PageHelper.startPage(condition.getCurrentPage(), condition.getDisplayCount());
        List<Line> lines = lineMapper.searchLines(condition);
        return new PageInfo<>(lines);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addLine(Map<String, Object> params) {
        int result = lineMapper.addLine(params);
        if(result == 1){ //线路已经保存成功
            String startNumber = (String) params.get("startNumber");
            String endNumber = (String) params.get("endNumber");
            String lineId = (String) params.get("id");
            //线路添加成功后，需要将线路中的杆塔标识为该线路所有
            towerMapper.updateTowerLineId(startNumber, endNumber, lineId);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteLine(String lineId) {
        int result = lineMapper.deleteLine(lineId);
        //删除线路后，线路中使用的杆塔信息也需要删除相关的线路编号
        if(result == 1){//删除成功
            towerMapper.removeLineId(lineId);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer updateLines(Map<String, Object> params) {
        return lineMapper.updateLines(params);
    }
}
