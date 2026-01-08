package com.qf.electronic.condition;

import lombok.Data;

import java.util.List;

@Data
public class InspectionTaskCondition extends Condition {

    private String lineId;

    private String lineName;

    private String user;

    private Integer state;

    private int[] states;
}
