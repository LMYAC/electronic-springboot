package com.qf.electronic.result;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Result<T> {

    private T data;

    private int status;

    private String msg;

    private Result(int status, T data, String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public static<E> Result<E> ok(E data){
        return new Result<>(HttpStatus.OK.value(), data, "OK");
    }

    public static<E> Result<E> error(){
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "服务内部错误");
    }

    public static<E> Result<E> error(int status, String msg){
        return new Result<>(status, null, msg);
    }
}
