package com.postman.net.downUp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;

import com.postman.net.CallServer;
import com.postman.net.HttpListener;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OnUploadListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;


public class Upload {
    private static Upload sUpload = null;

    private Context mContext = null;

    private Map<String, Request<String>> mUploadRequestMap = null;

    private Upload(Context context) {
        mContext = context;
        mUploadRequestMap = new HashMap<String, Request<String>>();
    }

    /**
     * 获取Upload唯一实例
     *
     * @param context
     * @return Upload唯一实例
     */
    public static Upload getInstance(Context context) {
        if (sUpload == null) {
            synchronized (Upload.class) {
                if (sUpload == null) {
                    sUpload = new Upload(context);
                }
            }
        }

        return sUpload;
    }

    /**
     * 用NoHttp默认实现上传文件
     *
     * @param url            上传的服务器地址
     * @param filePath       要上传文件的本地路径
     * @param httpListener   http监听对象
     * @param uploadListener 上传监听对象
     */
    public void start(String url, String filePath, HttpListener<String> httpListener, OnUploadListener uploadListener) {
        String fileName = FileUtils.getFileName(filePath);
        Request<String> uploadRequest = NoHttp.createStringRequest(url, RequestMethod.POST);

        try {
            String encodeName = URLEncoder.encode(fileName, "utf-8");
            FileBinary fileBinary = new FileBinary(new File(filePath), encodeName, URLConnection.guessContentTypeFromName(encodeName));

            uploadRequest.add("file", fileBinary);

            fileBinary.setUploadListener(0, uploadListener);

            mUploadRequestMap.put(filePath, uploadRequest);

            CallServer.getInstance().request(mContext, 0, uploadRequest, httpListener, true, false);
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 用NoHttp默认实现上传文件(收发文件)
     *
     * @param url            上传的服务器地址
     * @param filePath       要上传文件的本地路径
     * @param httpListener   http监听对象
     * @param uploadListener 上传监听对象
     * @param fileName       服务器存储的文件名
     *                       <p>
     *                       add by miss at 2016-11-16
     */
    public void start(String url, String filePath, HttpListener<String> httpListener, OnUploadListener uploadListener, String fileName) {
        Request<String> uploadRequest = NoHttp.createStringRequest(url, RequestMethod.POST);

        try {
            String encodeName = URLEncoder.encode(fileName, "utf-8");
            FileBinary fileBinary = new FileBinary(new File(filePath), encodeName, URLConnection.guessContentTypeFromName(encodeName));

            uploadRequest.add("file", fileBinary);

            fileBinary.setUploadListener(0, uploadListener);

            mUploadRequestMap.put(filePath, uploadRequest);

            CallServer.getInstance().request(mContext, 0, uploadRequest, httpListener, true, false);
        } catch (UnsupportedEncodingException e) {
        }
    }

    /**
     * 取消指定路径文件的上传
     *
     * @param filePath 要取消上传的本地文件路径
     */
    public void cancle(String filePath) {
        Request<String> uploadRequest = getUploadRequest(filePath);

        if (uploadRequest != null) {
            uploadRequest.cancel();
            remove(filePath);
        }
    }

    /**
     * 获取指定路径的上传请求
     *
     * @param filePath 要获取上传请求的本地文件路径
     * @return null, 如果指定的路径未上传过;否则返回指定路径的上传请求
     */
    private Request<String> getUploadRequest(String filePath) {
        if (mUploadRequestMap.containsKey(filePath)) {
            return mUploadRequestMap.get(filePath);
        }

        return null;
    }

    /**
     * 取消所有上传请求
     */
    public void cancelAll() {
        for (String url : mUploadRequestMap.keySet()) {
            mUploadRequestMap.get(url).cancel();
        }

        mUploadRequestMap.clear();
    }

    /**
     * 移除指定路径的上传请求
     *
     * @param filePath 要移除的本地文件路径
     */
    public void remove(String filePath) {
        mUploadRequestMap.remove(filePath);
    }

    public Map<String, Request<String>> getmUploadRequestMap() {
        return mUploadRequestMap;
    }

    public void setmUploadRequestMap(Map<String, Request<String>> mUploadRequestMap) {
        this.mUploadRequestMap = mUploadRequestMap;
    }
}
