package com.postman.ui.module.other.ping;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.base.utils.ToolbarUtil;
import com.custom.widget.MultiEditInputView;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseCompatActivity;
import com.postman.ui.module.other.ping.helper.NetPingHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class PingActivity extends BaseCompatActivity {

    protected static String TAG = "PingActivity";
    @BindView(R.id.search_bar)
    AutoCompleteTextView searchBar;
    @BindView(R.id.mev_view)
    MultiEditInputView mevView;

    private Context mContext;
    private String[] allStrs = {"www.baidu.com", "172.16.93.34", "172.16.93.32", "172.16.93.111", "172.0.0.1"};
    private StringBuffer strBuffer;

    @Override
    protected int setContentView() {
        return R.layout.activity_ping;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "Ping", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;
        initView();
    }

    private void initView(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, allStrs);
        searchBar.setAdapter(adapter);

        strBuffer = new StringBuffer();
    }

    @OnClick({R.id.ic_empty, R.id.tvType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_empty:
                searchBar.setText("");
                break;
            case R.id.tvType:
                final String url = searchBar.getText().toString().trim();
                DialogUtils.showLoading(mContext);
                if(TextUtils.isEmpty(url)){
                    ToastUtils.showToast(mContext, "url is empty");
                    return;
                }

                mevView.setContentText("");
                strBuffer.setLength(0);
                mevView.post(new Runnable() {
                    @Override
                    public void run() {
                        NetPingHelper pingHelper = new NetPingHelper.Builder()
                                .count(5)
                                .size(32)
                                .host(url)
                                .build();
                        pingHelper.setOnPingListener(new NetPingHelper.OnPingListener() {
                            @Override
                            public void onStart() {
                                showResult("-------start:");
                            }

                            @Override
                            public void onSucceed(String succeedStr) {
                                showResult(succeedStr);
                            }

                            @Override
                            public void onError(String errorStr) {
                                showResult(errorStr);
                            }

                            @Override
                            public void onStop() {
                            }
                        });
                        pingHelper.startPing();
                    }
                });
                break;
        }
    }

    private void showResult(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                strBuffer.append(str + "\n");
                mevView.setContentText(strBuffer.toString());
                DialogUtils.dismissLoading();
            }
        });
    }
}