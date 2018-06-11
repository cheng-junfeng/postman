package com.postman.ui.module.other.ping.helper;

import android.text.TextUtils;

import com.webview.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class NetPingHelper {
    private String TAG = "NetPingHelper";
    private Builder mBuilder;

    private Process process;
    private boolean flag;

    private NetPingHelper(Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    private StringBuilder stringBuilder;

    public void startPing() {
        flag = true;
        if (stringBuilder == null) {
            stringBuilder = new StringBuilder();
        } else {
            stringBuilder.setLength(0);
        }
        stringBuilder.append("ping -c ");
        stringBuilder.append(mBuilder.count);
        stringBuilder.append(" -s ");
        stringBuilder.append(mBuilder.size);
        stringBuilder.append(" -w ");
        stringBuilder.append(mBuilder.time);
        stringBuilder.append(" ");
        stringBuilder.append(mBuilder.host);

        Observable.create(new ObservableOnSubscribe<PingResult>() {
            @Override
            public void subscribe(ObservableEmitter<PingResult> emitter) throws Exception {
                LogUtil.d(TAG, "executePing");
                executePing(stringBuilder.toString(), emitter);
            }
        }).subscribe(new Observer<PingResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.d(TAG, "subscribe");
                if (onPingListener != null) {
                    onPingListener.onStart();
                }
            }

            @Override
            public void onNext(PingResult value) {
                LogUtil.d(TAG, "onNext" + value.successState);
                if (onPingListener == null) {
                    return;
                }
                if (value.successState) {
                    onPingListener.onSucceed(value.result);
                } else {
                    onPingListener.onError(value.result);
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                LogUtil.d(TAG, "complete");
                if (onPingListener != null) {
                    onPingListener.onStop();
                    stopPing();
                }
            }
        });
    }

    public void stopPing() {
        if (process != null) {
            process.destroy();
        }

        flag = false;
    }

    private void executePing(String command, ObservableEmitter<PingResult> emitter) {
        BufferedReader successReader = null;
        BufferedReader errorReader = null;

        PingResult pingResult;
        try {
            process = Runtime.getRuntime().exec(command, null, null);
            InputStream in = process.getInputStream();

            successReader = new BufferedReader(new InputStreamReader(in));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String lineStr;
            while ((lineStr = successReader.readLine()) != null && flag) {
                if (!TextUtils.isEmpty(lineStr)) {
                    pingResult = new PingResult();
                    pingResult.result = lineStr;
                    pingResult.successState = true;
                    emitter.onNext(pingResult);
                }
            }

            while ((lineStr = errorReader.readLine()) != null && flag) {
                pingResult = new PingResult();
                pingResult.result = lineStr;
                pingResult.successState = false;
                emitter.onNext(pingResult);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (successReader != null) {
                    successReader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
//                process.destroyForcibly();
                process = null;
            }
        }
        emitter.onComplete();
    }

    private class PingResult {
        private String result;
        private boolean successState;
    }

    private OnPingListener onPingListener;

    public void setOnPingListener(OnPingListener onPingListener) {
        this.onPingListener = onPingListener;
    }

    public interface OnPingListener {
        /**
         * 开始ping,工作在主线程
         */
        void onStart();

        /**
         * 成功返回一条信息
         *
         * @param succeedStr
         */
        void onSucceed(String succeedStr);

        /**
         * 失败返回一条信息
         *
         * @param errorStr
         */
        void onError(String errorStr);

        /**
         * ping 停止
         */
        void onStop();
    }

    public static class Builder {
        private int count = 3;
        private int size = 32;
        private String host;
        /**
         * 单位s
         */
        private int time = 3;

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder time(int time) {
            this.time = time;
            return this;
        }

        public NetPingHelper build() {
            return new NetPingHelper(this);
        }
    }
}