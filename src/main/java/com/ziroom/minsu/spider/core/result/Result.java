package com.ziroom.minsu.spider.core.result;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 统一结果统计响应
 * @author jixd
 * @created 2017年08月16日 11:31:17
 * @param
 * @return
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -494849412079027242L;

    private int code;
    private String message;
    private Object data;

    public Result() {
        code = ResultCode.SUCCESS.code;
    }

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
