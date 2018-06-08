package com.postman;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.base.utils.LogUtil;
import com.postman.db.control.AppDbManager;
import com.yanzhenjie.nohttp.NoHttp;

import java.util.List;

public class PostmanApplication extends MultiDexApplication {
    public static final String TAG = "SmartApplication";
    private Context mContext;
    private static PostmanApplication instance;

    public static synchronized PostmanApplication getInstance() {
        if (null == instance) {
            instance = new PostmanApplication();
        }
        return instance;
    }

    public PostmanApplication(){}

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        LogUtil.d(TAG, "onCreate");

        //util
        Utils.init(mContext);
        if(!shouldInit()){
            LogUtil.d(TAG, "return while not main process");
            return;
        }
        LogUtil.d(TAG, "init");

        //db
        AppDbManager.init(mContext);
        //nohttp
        NoHttp.initialize(this);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        LogUtil.d(TAG, "shouldInit myPid:" + myPid);
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
