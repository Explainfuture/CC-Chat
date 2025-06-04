package com.explainsf.springai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//todo
public class Result<T> {
    private int code; //0表示成功
    private String msg;
    private T data;

    public static <T> Result<T> success(T data){
        return new Result<T>(0, "success", data);
    }

    public static Result<?> error(String msg){
        return new Result<>(1,msg,null);
    }
}
