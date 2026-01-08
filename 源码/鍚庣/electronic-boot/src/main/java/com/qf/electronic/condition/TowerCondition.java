package com.qf.electronic.condition;

import lombok.Data;

@Data
public class TowerCondition extends Condition {

    private String id;

    private Integer state;

    private String queryString;
}
