package com.postman.ui.module.main.find.contract;


import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.postman.app.adapter.BaseDelegateAdapter;

public interface FindFragmentContract {

    interface View {
//        void setOnclick(int position);
//        void setGridClick(int position);
    }

    interface Presenter {
        //初始化
        DelegateAdapter initRecyclerView(RecyclerView recyclerView);
        DelegateAdapter.Adapter initMenu4();
    }
}
