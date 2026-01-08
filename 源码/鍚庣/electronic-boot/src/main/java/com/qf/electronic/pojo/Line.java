package com.qf.electronic.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class Line {

    private String id;

    private String name;

    private int towerNum;

    private String startNumber;

    private String endNumber;

    private Double length;

    private Double backLength;

    private String level;

    @JSONField(format = "yyyy-MM-dd")
    private Date operateTime;

    private int enableStatus;

    private String enableStatusText;

    private String remark;
}
