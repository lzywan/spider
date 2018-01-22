package com.ziroom.minsu.spider.core.result;

<<<<<<< HEAD
import com.alibaba.fastjson.JSON;
=======
>>>>>>> test
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

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

    private int status;
    private String message;
    private Object data;

    public Result() {
        status = ResultCode.SUCCESS.code;
    }

    public Result setStatus(ResultCode resultCode) {
        this.status = resultCode.code;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Result setStatus(int code) {
        this.status = code;
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
        return JSONObject.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}
