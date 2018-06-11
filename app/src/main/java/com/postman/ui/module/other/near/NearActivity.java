package com.postman.ui.module.other.near;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.utils.LogUtil;
import com.base.utils.ToolbarUtil;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseCompatActivity;
import com.postman.ui.module.main.find.bean.KeyListBean;
import com.postman.ui.module.other.near.adapter.NearListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NearActivity extends BaseCompatActivity {

    protected static String TAG = "NearActivity";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    ArrayList<KeyListBean> allDatas;
    NearListAdapter keyListAdapter;

    private WifiManager wifiManager;

    @Override
    protected int setContentView() {
        return R.layout.activity_near;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "Near", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;
        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        initBroadcastReceiver();
        initView();
    }

    private void initView(){
        allDatas = new ArrayList<>();

        setAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(keyListAdapter);
    }

    private void initBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            LogUtil.d(TAG, "action:"+action);
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                LogUtil.d(TAG, "size:"+scanResults.size());
                allDatas = new ArrayList<>();
                for (ScanResult tempRes : scanResults) {
                    KeyListBean bean = new KeyListBean();
                    bean.key = tempRes.SSID;
                    bean.value = tempRes.BSSID;
                    allDatas.add(bean);
                }
                setAdapter();
            }
            DialogUtils.dismissLoading();
        }
    };

    private void setAdapter() {
        LogUtil.d(TAG, "allDatas:"+allDatas.size());
        if (keyListAdapter == null) {
            keyListAdapter = new NearListAdapter(mContext, allDatas);
        } else {
            keyListAdapter.setDatas(allDatas);
            keyListAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.tvSearch)
    public void onViewClicked() {
        if(!isGpsOpen(mContext)){
            ToastUtils.showToast(mContext, "need GPS");
            return;
        }
        if (!wifiManager.isWifiEnabled()) {
            //开启wifi
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
        DialogUtils.showLoading(mContext);
    }

    private boolean isGpsOpen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}