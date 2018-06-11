package com.postman.net.downUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.postman.net.CallServer;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;


public class Download {
    private static Download mDownload = null;

    private Map<String, DownloadRequest> mRequestMap = null;
    private List<String> mCancelList = null;

    private Download(Context context) {
        mRequestMap = new HashMap<String, DownloadRequest>();
        mCancelList = new ArrayList<String>();
    }

    /**
     * 获取Download唯一实例
     *
     * @param context 当前应用程序的Context
     * @return Download唯一实例
     */
    public static Download getInstance(Context context) {
        if (mDownload == null) {
            synchronized (Download.class) {
                if (mDownload == null) {
                    mDownload = new Download(context);
                }
            }
        }

        return mDownload;
    }

    /**
     * 启动下载
     *
     * @param url      要下载的url地址
     * @param fileName 下载的文件保存到本地的文件名称
     * @param listener 下载事件监听
     * @return -1-启动下载失败;0-从头开始下载;100-已下载完成;其它数值-断点续传
     */
    public void start(String url, String fileName, DownloadListener listener) {
        String downloadDir = FileUtils.getDownloadDir();

        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(UrlUtils.downloadUrlEncode(url), downloadDir, fileName, true, false);

        try {
            mRequestMap.put(url, downloadRequest);

        } catch (UnsupportedOperationException e) {
        } catch (ClassCastException e) {
        } catch (IllegalArgumentException e) {
        } catch (NullPointerException e) {
        }

        CallServer.getInstance().download(0, downloadRequest, listener);
    }

    /**
     * 取消下载
     *
     * @param url 要取消的url
     */
    public void cancle(String url) {
        DownloadRequest downloadRequest = getExistDownloadRequest(url);

        if (downloadRequest != null) {
            downloadRequest.cancel();
            mCancelList.add(url);
        } else {
        }
    }

    /**
     * 获取已存在的下载请求对象
     *
     * @param url 要获取的url
     * @return null, 如果url不存在, 否则返回已存在的下载请求对象
     */
    private DownloadRequest getExistDownloadRequest(String url) {
        return mRequestMap.get(url);
    }

    /**
     * 取消全部下载请求
     */
    public void cancelAll() {
        for (String url : mRequestMap.keySet()) {
            mRequestMap.get(url).cancel();
        }

        mRequestMap.clear();
    }

    /**
     * 移除指定url的下载请求
     *
     * @param url 要移除的url
     */
    public void remove(String url) {
        mRequestMap.remove(url);
    }

    /**
     * 判断指定url下载请求是否已被取消
     *
     * @param url 要判断的url
     * @return true, 如果指定url下载请求是否已被取消;否则返回false
     */
    public boolean isCancle(String url) {
        int size = mCancelList.size();
        for (int i = 0; i < size; i++) {
            if (mCancelList.get(i).equals(url)) {
                return true;
            }
        }

        return false;
    }

    public Map<String, DownloadRequest> getmRequestMap() {
        return mRequestMap;
    }

    public void setmRequestMap(Map<String, DownloadRequest> mRequestMap) {
        this.mRequestMap = mRequestMap;
    }
}
