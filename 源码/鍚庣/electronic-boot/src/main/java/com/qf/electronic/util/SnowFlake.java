package com.qf.electronic.util;

public class SnowFlake {

    private long roomId;//机房ID，占5位

    private long machineId;//机器ID，占5位

    private long sequence; //序列号，占12位，默认值0

    private long startTime = 1641268963530L; //ID生成的起始时间

    private long lastTime = -1L; //记录最后一次生成ID的时间，方便判断是否在一毫秒内需要生成多个ID

    public SnowFlake(long roomId, long machineId){
        if(roomId < 0 || roomId >= 32){
            throw new IllegalArgumentException("机房ID最小值为0，最大值为31，当前值：" + roomId);
        }
        if(machineId < 0 || machineId >= 32){
            throw new IllegalArgumentException("机器ID最小值为0，最大值为31，当前值：" + machineId);
        }
        this.roomId = roomId;
        this.machineId = machineId;
    }

    private static SnowFlake instance;

    //思考一个问题：当第一个线程拿到实例之后，instance就已经有值了，后续操作只需要取到这个实例即可
//    public static synchronized SnowFlake getInstance(){
//        if(instance == null)
//            instance = new SnowFlake(1, 1);
//        return instance;
//    }
//    public static SnowFlake getInstance(){
//        if(instance == null){
            //思考同时有多个线程进来
//            synchronized (SnowFlake.class){//第一个线程进来
//                if(instance == null)
//                    instance = new SnowFlake(1, 1);
//            }
//        }
//        return instance;
//    }

    public long generateId(){
        long time = System.currentTimeMillis();
        if(time < lastTime) throw new RuntimeException("时钟回拨");
        else if(time == lastTime){//同一毫秒内生成多个ID
            synchronized (SnowFlake.class){
                sequence = (sequence + 1) & 4095;
                if(sequence == 0){
                    //CPU空转
                    while ((time = System.currentTimeMillis()) == lastTime);
                }
            }
        } else {
            sequence = 0;
        }
        lastTime = time;
        return (time - startTime) << 22 | roomId << 17 | machineId << 12 | sequence;
    }
}
