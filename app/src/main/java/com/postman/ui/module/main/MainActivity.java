package com.postman.ui.module.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.blankj.utilcode.util.ToastUtils;
import com.hint.listener.OnChooseListener;
import com.hint.utils.HintListAdapter;
import com.postman.R;
import com.postman.app.activity.BaseCompatActivity;
import com.postman.ui.module.main.data.DataFragment;
import com.postman.ui.module.main.find.FindFragment;
import com.postman.ui.module.main.main.adapter.ViewPagerAdapter;
import com.postman.ui.module.main.main.contract.MainContract;
import com.postman.ui.module.main.main.presenter.MainPresenter;
import com.postman.ui.module.other.near.NearActivity;
import com.postman.ui.module.other.ping.PingActivity;
import com.postman.ui.module.other.scan.ScanActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.base.utils.ToolbarUtil;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseCompatActivity implements MainContract.View, ViewPager.OnPageChangeListener {

    public static final String TAG = "SearchActivity";

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_my)
    RadioButton rbMy;

    private int normalColor;
    private int selectColor;

    int pre_index = -1;
    MainPresenter mainPresenter;
    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.activity_control;
    }

    @Override
    protected int setErrorView() {
        return R.layout.base_empty;
    }

    @Override
    protected int setFootView() {
        return R.layout.item_control_footer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        normalColor = getResources().getColor(R.color.tab_host_text_normal);
        selectColor = getResources().getColor(R.color.tab_host_text_select);
        mContext = this;

        initData();
        initToolbar();
        initViewPager();
        initRadio();
        setTabSelect(0);
    }

    private void initData(){
        mainPresenter = new MainPresenter(this);
    }

    private void initToolbar() {
        ToolbarUtil.setToolbarLeft(toolbar, "Home", getString(R.string.app_name), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        });
        ToolbarUtil.setToolbarRight(toolbar, R.mipmap.ic_action_scan, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readGo(ScanActivity.class);
            }
        });
    }

    PopupWindow typePopupWindow;
    private void showPopupWindow() {
        if (typePopupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_choose, null);
            ListView mainView = view.findViewById(com.hint.R.id.main_view);
            List<String> allStr = new ArrayList<>();
            allStr.add("Ping");
            HintListAdapter mAdapter = new HintListAdapter(mContext, allStr);
            mAdapter.setOnListener(new OnChooseListener() {
                @Override
                public void onPositive(int pos) {
                    typePopupWindow.dismiss();
                    switch (pos){
                        case 0:
                            readGo(PingActivity.class);
                            break;
//                        case 1:
//                            readGo(NearActivity.class);
//                            break;
                        default:break;
                    }
                }
            });
            mainView.setAdapter(mAdapter);
            typePopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            typePopupWindow.setOutsideTouchable(true);
            typePopupWindow.setFocusable(true);
            typePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        typePopupWindow.showAsDropDown(toolbar);
    }

    FindFragment findFragment;
    DataFragment dataFragment;
    Fragment[] mFragments;

    // 初始化数据
    private void initViewPager() {
        findFragment = new FindFragment();
        dataFragment = new DataFragment();
        mFragments = new Fragment[]{findFragment, dataFragment};
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        vpMain.setAdapter(adapter);
        vpMain.addOnPageChangeListener(this);
        vpMain.setOffscreenPageLimit(2);
    }

    RadioButton[] radioButtons;

    private void initRadio() {
        radioButtons = new RadioButton[2];
        radioButtons[0] = rbHome;
        radioButtons[1] = rbMy;
        radioButtons[0].setChecked(true);
    }

    @OnClick({R.id.rb_home, R.id.rb_my})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_home:
                setTabSelect(0);
                break;
            case R.id.rb_my:
                setTabSelect(1);
                break;
        }
    }

    private void setTabSelect(int position) {
        if (position == pre_index) {
            return;
        }
        switch (position) {
            case 0:
                ToolbarUtil.setToolbar(toolbar, "Home");
                break;
            case 1:
                ToolbarUtil.setToolbar(toolbar, "History");
                break;
        }
        if (pre_index >= 0) {
            radioButtons[pre_index].setChecked(false);
            radioButtons[pre_index].setTextColor(normalColor);
        }

        vpMain.setCurrentItem(position);
        radioButtons[position].setChecked(true);
        radioButtons[position].setTextColor(selectColor);
        pre_index = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setTabSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private long firstTime;

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            ToastUtils.showLong("Again for exit");
            firstTime = secondTime;
        } else {
            appExit();
        }
    }

    @Override
    public RxAppCompatActivity getRxActivity() {
        return this;
    }
}
