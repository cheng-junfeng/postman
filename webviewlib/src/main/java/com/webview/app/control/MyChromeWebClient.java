package com.webview.app.control;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

import com.webview.utils.LogUtil;


public class MyChromeWebClient extends WebChromeClient {
    private final static String TAG = "MyChromeWebClient";

    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        LogUtil.e(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId() );
        return true;
    }

    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        LogUtil.d(TAG, "onReceivedTitle:"+title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        LogUtil.d(TAG, "onProgressChanged:"+newProgress);
    }

    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
        LogUtil.d(TAG, "openFileChooser 0 ");
        this.openFileChooser(uploadMsg);
    }

    @SuppressWarnings("unused")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
        LogUtil.d(TAG, "openFileChooser 1 ");
        this.openFileChooser(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        LogUtil.d(TAG, "openFileChooser 2 ");
    }

    /**
     * 覆盖默认的alert展示界面
     * alert("ok");
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (!TextUtils.isEmpty(message)) {
            LogUtil.d(TAG, "onJsAlert :"+message);
        }
        result.confirm();// return false 不拦截，弹出Js对话框
        return true;
    }

    /**
     * 覆盖默认的window.confirm展示界面
     * var bool = window.confirm("您确定删除吗?");
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("对话框")
                .setMessage(message)
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        });

        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
        // return super.onJsConfirm(view, url, message, result);
    }

    /**
     * 覆盖默认的window.prompt展示界面
     * window.prompt('请输入您的域名地址', '618119.com');
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message,
                              String defaultValue, final JsPromptResult result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("对话框").setMessage(message);

        final EditText et = new EditText(view.getContext());
        et.setSingleLine();
        et.setText(defaultValue);
        builder.setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm(et.getText().toString());
                    }

                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });

        // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                Log.v("onJsPrompt", "keyCode==" + keyCode + "event="+ event);
                return true;
            }
        });

        // 禁止响应按back键的事件
        // builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
}
