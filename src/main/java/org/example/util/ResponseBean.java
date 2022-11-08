package org.example.util;

import org.springframework.http.HttpStatus;

/**
 * 统一响应消息返回
 */
public class ResponseBean {
    private int code;//返回状态码
    private String msg;//返回数据说明
    private Object data;//返回数据的对象引用

    public ResponseBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseBean() {
    }

    //返回响应消息成功的方法
    public static ResponseBean success(Object data) {
        ResponseBean rb = new ResponseBean();
        if(data == null){
            rb.code = HttpStatus.EXPECTATION_FAILED.value();//417
            rb.msg = "请求失败";
        }else {
            rb.code = HttpStatus.OK.value();//200
            rb.msg = "请求成功";
        }
        rb.data = data;
        return rb;
    }
    //返回响应消息失败的方法
    public static ResponseBean failure() {
        ResponseBean rb = new ResponseBean();
        rb.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        rb.msg = "请求失败";
        rb.data = null;
        return rb;
    }
    public static ResponseBean failure(Object data) {
        ResponseBean rb = new ResponseBean();
        rb.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        rb.msg = "请求失败";
        rb.data = data;
        return rb;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
