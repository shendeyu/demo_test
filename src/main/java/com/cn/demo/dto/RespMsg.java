package com.cn.demo.dto;


public class RespMsg<T> {

    private int code;
    private String msg;
    private T data;

    public static RespMsg success(String msg){
        RespMsg responseEntity = new RespMsg();
        responseEntity.setCode(200);
        responseEntity.setMsg(msg);
        return responseEntity;
    }

    public RespMsg<T> success(String msg, T data){
        RespMsg<T> responseEntity = new RespMsg<>();
        responseEntity.setCode(200);
        responseEntity.setData(data);
        responseEntity.setMsg(msg);
        return responseEntity;
    }

    public static RespMsg error(int errorCode, String msg){
        RespMsg responseEntity=new RespMsg();
        responseEntity.setCode(errorCode);
        responseEntity.setMsg(msg);
        return responseEntity;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
