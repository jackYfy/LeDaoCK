package com.zk.taxi.http;

import com.loopj.android.http.AsyncHttpResponseHandler;


public abstract class HttpResponse extends AsyncHttpResponseHandler {


    @Override
    public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
        if(bytes!=null) {
            result(i, new String(bytes));
        }
    }

    @Override
    public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
        if(bytes!=null) {
            result(i, new String(bytes));
        }
    }

    public abstract void result(int i, String content);
}
