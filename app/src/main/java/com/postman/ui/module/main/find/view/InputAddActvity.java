package com.postman.ui.module.main.find.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.base.app.event.RxBusHelper;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseActivity;
import com.postman.app.event.MyEvent;
import com.postman.app.event.MyType;
import com.postman.config.Cache;
import com.postman.config.Extra;
import com.postman.config.enums.OptionsConfig;
import com.postman.config.enums.TypesConfig;
import com.postman.db.cache.PostmanCache;
import com.postman.ui.module.main.find.bean.KeyListBean;
import com.postman.ui.module.main.find.utils.FindCacheUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class InputAddActvity extends BaseActivity {

    @BindView(R.id.hm_type)
    TextView hmType;
    @BindView(R.id.hm_options)
    TextView hmOptions;
    @BindView(R.id.hm_key)
    EditText hmKey;
    @BindView(R.id.hm_value)
    EditText hmValue;

    int pos = 0;
    String type = TypesConfig.POST.name();
    String option = OptionsConfig.HEADER.name();
    private Context mContext;
    private PostmanCache myCache;

    @Override
    protected boolean setToolbar() {
        return false;
    }

    @Override
    protected int setContentView() {
        return R.layout.dialog_add_input;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pos = bundle.getInt(Extra.DATA_INPUT_POS, 0);
        }
        mContext = this;
        myCache = PostmanCache.get(mContext);
        type = myCache.getAsString(Cache.CACHE_TYPE);
        option = myCache.getAsString(Cache.CACHE_OPTION);
        hmType.setText(type);
        hmOptions.setText(option);

        KeyListBean myBean = FindCacheUtil.getKeyBean(myCache, OptionsConfig.value(option), pos);
        if(myBean != null && myBean.set){
            hmKey.setText(myBean.key);
            hmValue.setText(myBean.value);
        }
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_confirm:
                String key1 = hmKey.getText().toString().trim();
                if (TextUtils.isEmpty(key1)) {
                    ToastUtils.showToast(mContext, "key is empty");
                    return;
                }
                String value1 =  hmValue.getText().toString().trim();
                if (TextUtils.isEmpty(value1)) {
                    ToastUtils.showToast(mContext, "value is empty");
                    return;
                }

                KeyListBean bean = new KeyListBean();
                bean.set = true;
                bean.key = key1;
                bean.value = value1;
                FindCacheUtil.setKeyBean(myCache, OptionsConfig.value(option), pos, bean);

                RxBusHelper.post(new MyEvent.Builder(MyType.INPUT_UPDATE).build());
                finish();
                break;
        }
    }
}
