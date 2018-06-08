package com.postman.net;

import android.text.TextUtils;

import com.base.utils.LogUtil;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.util.Map;


public class JsonRequest extends StringRequest {
    private final static String TAG = "JsonRequest";
    public JsonRequest(String url) {
        super(url);
    }

    public JsonRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }

    public void setParams(Map<String,String> params){
        if (params != null && params.size() > 0) {
            String json = new Gson().toJson(params);
            if (!TextUtils.isEmpty(json)){
                LogUtil.d(TAG, "NoHttp"+json);
                setDefineRequestBodyForJson(json);
            }else {
                LogUtil.d(TAG, "Params is not normal");
            }
        }else {
            LogUtil.d(TAG, "Params is not null");
        }
    }
}
