package com.qf.electronic.condition;

import lombok.Data;

@Data
public class LogCondition extends Condition {

    private String startTime;

    private String endTime;

    private String user;
}
