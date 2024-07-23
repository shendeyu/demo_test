package com.cn.demo.utils;

public enum  ReturnCodeEnum {

    RETURN_SUCCESS("success", 0),
    RETURN_OTHER_ERROR("other error", 16000),
    RETURN_ERROR_TYPE("please upload a CSV file", 16001),
    RETURN_ERROR_UPLOAD("could not upload the file", 16002),
    RETURN_INVALID_PARAM("Invalid parameters. Please provide a time period or game_no", 16003),
    ;



    private int code;
    private String msg;

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

    ReturnCodeEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }


}
