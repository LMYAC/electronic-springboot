package com.qf.electronic.util;

public class IdGenerator {

    private static final SnowFlake flake = new SnowFlake(1, 1);

    public static String generateId(String prefix){
        return String.format("%s%d", prefix, flake.generateId());
    }

}
