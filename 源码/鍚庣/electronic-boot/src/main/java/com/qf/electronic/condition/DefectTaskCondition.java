package com.qf.electronic.condition;

import lombok.Data;

@Data
public class DefectTaskCondition extends Condition {

    private String lineId;

    private String lineName;

    private String user;

    private Integer state;
}
