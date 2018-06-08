package com.postman.ui.module.other.scan;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hint.listener.OnChooseListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.base.utils.DensityUtils;
import com.base.utils.ToolbarUtil;
import com.postman.R;
import com.postman.app.activity.BaseCompatActivity;
import com.webview.ui.WebViewNormalActivity;
import com.webview.config.WebConfig;
import com.zxing.control.decode.DecodeType;
import com.zxing.control.inter.CameraOpenCallBack;
import com.zxing.control.inter.OnDiscernListener;
import com.zxing.result.ErrorResult;
import com.zxing.result.SuccessResult;
import com.zxing.widget.ZxingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScanActivity extends BaseCompatActivity {

    protected static String TAG = "ScanActivity";
    @BindView(R.id.zxingView)
    ZxingView zxingView;

    private View tv_scan_hint;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "Scan", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mContext = this;

        initView();
    }

    private void initView() {
        initZxingView(zxingView);
    }

    private void initZxingView(final ZxingView zxingView) {
        if (zxingView == null) {
            return;
        }
        zxingView.setCropStyle(ZxingView.CROP_STYLE_QR_CODE);
        zxingView.setDebug(false);
        zxingView.setDecodeType(DecodeType.SINGLE_ALL);
        zxingView.setNeedCrop(true);
        zxingView.setCameraOpenCallBack(new CameraOpenCallBack() {
            @Override
            public void onSuccess(Camera camera) {
                zxingView.startRepeatAnimation();
                zxingView.startDiscern();
            }

            @Override
            public void onException(Exception exception) {
                ToastUtils.showToast(mContext, "Exception");
            }
        });

        zxingView.setOnDiscernListener(new OnDiscernListener() {
            @Override
            public void onSuccess(SuccessResult successResult) {
                if (successResult.getResult() != null) {
                    handScanResult(successResult.rawResult.getText());
                } else {
                    zxingView.startDiscern();
                }
            }

            @Override
            public void onError(ErrorResult errorResult) {
                zxingView.startDiscern();
            }
        });

        zxingView.onCreate();
    }

    private Vibrator vibrator;

    private void handScanResult(final String barcodes) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};
        vibrator.vibrate(pattern, 1);
        tv_scan_hint.postDelayed(new Runnable() {
            @Override
            public void run() {
                vibrator.cancel();
            }
        }, 1000);

        List<String> allStr = new ArrayList<String>();
        allStr.add("Copy to the plate");
        allStr.add("Open webview");
        allStr.add("Share");
        DialogUtils.showChooseDialog(this, allStr, new OnChooseListener() {
            @Override
            public void onPositive(int pos) {
                switch (pos){
                    case 0:{
                        ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(barcodes);
                        ToastUtils.showToast(getApplicationContext(), "had copied");
                        restartPreviewAfterDelay(1000);
                    }break;
                    case 1:{
                        Bundle bundle = new Bundle();
                        bundle.putString(WebConfig.JS_URL, barcodes);
                        readGo(WebViewNormalActivity.class, bundle);
                    }break;
                    case 2:{
                        Intent textIntent = new Intent(Intent.ACTION_SEND);
                        textIntent.setType("text/plain");
                        textIntent.putExtra(Intent.EXTRA_TEXT, barcodes);
                        startActivity(Intent.createChooser(textIntent, "postman"));
                    }break;
                    default:break;
                }
            }
        });
    }

    public void restartPreviewAfterDelay(long delayMS) {
        zxingView.startRepeatAnimation();
        zxingView.startPreview();
        zxingView.startDiscernDelayed(delayMS);
    }

    @Override
    protected void onResume() {
        zxingView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        zxingView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        zxingView.onDestroy();
        super.onDestroy();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}