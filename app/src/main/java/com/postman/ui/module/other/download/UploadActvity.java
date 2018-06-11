package com.postman.ui.module.other.download;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.utils.LogUtil;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseActivity;
import com.postman.config.Extra;
import com.postman.config.enums.TypesConfig;
import com.postman.net.HttpListener;
import com.postman.net.downUp.Upload;
import com.postman.ui.module.main.find.utils.FindDBUtil;
import com.yanzhenjie.nohttp.OnUploadListener;
import com.yanzhenjie.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.OnClick;

public class UploadActvity extends BaseActivity {

    private final static String TAG = "UploadActvity";
    @BindView(R.id.file_path)
    EditText filePath;
    @BindView(R.id.hm_speed)
    TextView hmSpeed;
    @BindView(R.id.customProgressBar)
    ProgressBar customProgressBar;
    @BindView(R.id.hm_url)
    TextView hmUrl;

    private String url;
    private Context mContext;
    private String result = "";

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString(Extra.DATA_URL, "");
        }
        hmUrl.setText(url);
        mContext = this;

    }

    @OnClick({R.id.ic_empty, R.id.tvFile, R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ic_empty:
                filePath.setText("");
                break;
            case R.id.tvFile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType(“image/*”);//选择图片
                //intent.setType(“audio/*”); //选择音频
                //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
                //intent.setType(“video/*;image/*”);//同时选择视频和图片
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_cancel:
                finish();
                UploadActvity.this.finish();
                break;
            case R.id.btn_confirm:
                String myPath = filePath.getText().toString().trim();
                if (TextUtils.isEmpty(url)) {
                    ToastUtils.showToast(mContext, "url is empty:" + filePath);
                    return;
                }
                if (TextUtils.isEmpty(myPath)) {
                    ToastUtils.showToast(mContext, "file is empty:" + filePath);
                    return;
                }
                startUpload();
                break;
        }
    }

    private void startUpload() {
        Upload.getInstance(mContext).start(url, path, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                LogUtil.d(TAG, "onSucceed:" + response.get());
                result = "onSucceed";
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                LogUtil.d(TAG, "onFailed:" + response.get());
            }
        }, new OnUploadListener() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onCancel(int what) {

            }

            @Override
            public void onProgress(int what, int progress) {
                customProgressBar.setProgress(progress);
                hmSpeed.setText(progress + "%");
            }

            @Override
            public void onFinish(int what) {

            }

            @Override
            public void onError(int what, Exception exception) {
                result = exception.getMessage();
            }
        });
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                filePath.setText(path);
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                filePath.setText(path);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                filePath.setText(path);
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        FindDBUtil.insertData(TypesConfig.UPLOAD, url, path, result);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}

