package com.postman.ui.module.other.download;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.utils.FileUtil;
import com.base.utils.LogUtil;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseActivity;
import com.postman.config.Extra;
import com.postman.config.enums.StatusConfig;
import com.postman.config.enums.TypesConfig;
import com.postman.net.downUp.Download;
import com.postman.net.downUp.FileUtils;
import com.postman.ui.module.main.find.utils.FindDBUtil;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


public class DownloadActvity extends BaseActivity {

    private final static String TAG = "DownloadActvity";
    @BindView(R.id.customProgressBar)
    ProgressBar customProgressBar;
    @BindView(R.id.hm_url)
    TextView hmUrl;
    @BindView(R.id.hm_speed)
    TextView hmSpeed;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.hm_file)
    TextView hmFile;

    private String url;
    private Context mContext;
    private StatusConfig status;
    private File downFile;

    private String result = "";

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_download;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString(Extra.DATA_URL, "");
        }
        hmUrl.setText(url);
        status = StatusConfig.INIT;

        String downloadDir = FileUtils.getDownloadDir();
        downFile = new File(downloadDir + File.separator + FileUtils.getFileName(url));
        hmFile.setText("File:" + downFile.getAbsolutePath());
    }

    private void startDownload() {
        Download.getInstance(mContext).start(url, FileUtils.getFileName(url), new DownloadListener() {

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                LogUtil.d(TAG, "onStart:" + rangeSize + ":" + allCount);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                customProgressBar.setProgress(progress);
                hmSpeed.setText("Progress:" + progress + "%");
                LogUtil.d(TAG, "onProgress:" + progress + ":" + FileUtil.formetFileSize(speed));
            }

            @Override
            public void onFinish(int what, String filePath) {
                LogUtil.d(TAG, "onFinish:" + filePath);
                ToastUtils.showToast(mContext, "finish:" + filePath);
            }

            @Override
            public void onDownloadError(int what, Exception exception) {
                LogUtil.d(TAG, "onDownloadError:" + exception.toString());
                result = exception.toString();
            }

            @Override
            public void onCancel(int what) {
                LogUtil.d(TAG, "onCancel:");
            }
        });
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                Download.getInstance(mContext).cancle(url);
                status = StatusConfig.INIT;
                finish();
                DownloadActvity.this.finish();
                break;
            case R.id.btn_confirm:
                switch (status) {
                    case INIT:
                    case PAUSE:
                        if (TextUtils.isEmpty(url)) {
                            ToastUtils.showToast(mContext, "url is empty");
                            return;
                        }
                        startDownload();
                        status = StatusConfig.RUN;
                        btnConfirm.setText("Pause");
                        break;
                    case RUN:
                        Download.getInstance(mContext).cancle(url);
                        status = StatusConfig.PAUSE;
                        btnConfirm.setText("Start");
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (downFile != null && downFile.exists()) {
            result = "down:"+downFile.getAbsolutePath();
        }
        FindDBUtil.insertData(TypesConfig.DOWN, url, "", result);
    }
}
