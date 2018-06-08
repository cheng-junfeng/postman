package com.postman.ui.module.main.data.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseActivity;
import com.postman.config.Extra;

import butterknife.BindView;
import butterknife.OnClick;


public class DataDetailActvity extends BaseActivity {


    @BindView(R.id.dialog_input)
    TextView dialogInput;
    @BindView(R.id.dialog_output)
    TextView dialogOutput;

    String input = "{}";
    String output = "{}";

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_data_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            input = bundle.getString(Extra.DATA_INPUT, "{}");
            output = bundle.getString(Extra.DATA_OUTPUT, "{}");
        }
        dialogInput.setText(input);
        dialogOutput.setText(output);
        dialogInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(input);
                ToastUtils.showToast(getApplicationContext(), "had copied");
            }
        });
        dialogOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(output);
                ToastUtils.showToast(getApplicationContext(), "had copied");
            }
        });
    }

    @OnClick(R.id.start)
    public void onViewClicked() {
        finish();
    }
}
