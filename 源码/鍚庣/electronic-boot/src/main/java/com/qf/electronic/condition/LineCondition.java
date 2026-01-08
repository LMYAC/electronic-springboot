package com.qf.electronic.condition;

import lombok.Data;

@Data
public class LineCondition extends Condition {

    private String id;

    private String name;

    private Integer state;
}
