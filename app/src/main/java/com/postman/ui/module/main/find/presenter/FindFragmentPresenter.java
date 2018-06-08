package com.postman.ui.module.main.find.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.postman.R;
import com.postman.app.adapter.BaseDelegateAdapter;
import com.postman.app.adapter.BaseViewHolder;
import com.postman.config.Constant;
import com.postman.db.entity.NoteEntity;
import com.postman.db.helper.NoteHelper;
import com.postman.ui.module.main.find.adapter.FindLinearAdapter;
import com.postman.ui.module.main.find.contract.FindFragmentContract;

import java.util.ArrayList;
import java.util.List;


public class FindFragmentPresenter implements FindFragmentContract.Presenter {

    private FindFragmentContract.View mView;
    private Fragment mFragment;
    private Context mContext;

    public FindFragmentPresenter(FindFragmentContract.View androidView, Fragment activity) {
        this.mView = androidView;
        this.mFragment = activity;
        this.mContext = activity.getContext();
    }

    @Override
    public DelegateAdapter initRecyclerView(RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }

    @Override
    public DelegateAdapter.Adapter initMenu4() {
        NoteHelper helper = NoteHelper.getInstance();
        List<NoteEntity> allData = helper.queryList();

        //设置线性布局
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        //设置Item个数
        linearLayoutHelper.setItemCount(allData.size());
        //设置间隔高度
        linearLayoutHelper.setDividerHeight(1);
        //设置布局底部与下个布局的间隔
        linearLayoutHelper.setMarginBottom(80);

        return new FindLinearAdapter(mContext, linearLayoutHelper, allData);
    }

    private void readGo(Class<?> cls){
        readGo(cls, null);
    }

    private void readGo(Class<?> cls, Bundle bundle){
        Intent inten = new Intent(mContext, cls);
        if(bundle != null){
            inten.putExtras(bundle);
        }
        mFragment.startActivity(inten);
    }
}
