package com.postman.net;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.base.utils.LogUtil;
import com.hint.utils.ToastUtils;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;


public class HttpResponseListener<T> implements OnResponseListener<T> {
    private final static String TAG = "HttpResponseListener";

    private Activity mActivity;
    /**
     * Dialog.
     */
    private MaterialDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<T> callback;

    /**
     * @param activity     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */
    public HttpResponseListener(Activity activity, Request<?> request, HttpListener<T> httpCallback, boolean
            canCancel, boolean isLoading) {
        this(activity, request, httpCallback, canCancel, isLoading,null);

    }

    public HttpResponseListener(Activity activity, Request<?> request, HttpListener<T> httpCallback, boolean
            canCancel, boolean isLoading, String dialogTitle){
        this.mActivity = activity;
        this.mRequest = request;
        if (activity != null && isLoading) {
            if (TextUtils.isEmpty(dialogTitle)){
                mWaitDialog = new MaterialDialog.Builder(activity).title("Loading")
                        .content("wait a moment...").progress(true, 0).build();
            }else {
                mWaitDialog = new MaterialDialog.Builder(activity).title(dialogTitle)
                        .content("wait a moment...").progress(true, 0).build();
            }

            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }

    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (mWaitDialog != null && !mActivity.isFinishing() && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        LogUtil.d(TAG, "response:onSucceed "+response.get());
        if (callback != null) {
            // 这里判断一下http响应码，这个响应码问下你们的服务端你们的状态有几种，一般是200成功。
            callback.onSucceed(what, response);
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, Response<T> response) {
        LogUtil.d(TAG, "response:onFailed "+response.get());
        Exception exception = response.getException();
        if (exception instanceof NetworkError) {// 网络不好
            ToastUtils.showToast(mActivity, "please check network");
        } else if (exception instanceof TimeoutError) {//
            ToastUtils.showToast(mActivity, "time out");
        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            ToastUtils.showToast(mActivity, "can't fount server");
        } else if (exception instanceof URLError) {// URL是错的
            ToastUtils.showToast(mActivity, "url error");
        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            // 没有缓存一般不提示用户，如果需要随你。
        } else {
            ToastUtils.showToast(mActivity, "unkown error");
        }
        LogUtil.d(TAG, "error：" + exception.getMessage());
        if (callback != null) {
            callback.onFailed(what, response);
        }
    }
}
