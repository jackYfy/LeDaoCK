package com.zk.taxi.http;

/**
 *
 */
public class HttpResult<T> {

    private String Status;//返回调用状态码
    private String Message;//返回调用错误信息
    private String ErrorCode;//返回错误代码
    private T Data;//返回数据

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
