package com.qf.electronic.condition;

import lombok.Data;

@Data
public class UserItemCondition extends Condition {

    private String queryString;

    private Integer state;

    private Integer roleId;
}
