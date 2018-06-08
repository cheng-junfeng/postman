package com.postman.ui.module.main.data.contract;

import com.postman.ui.module.main.data.bean.DataListBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import java.util.List;

public interface DataContract {

    interface View {
        void setRecyclerData(List<DataListBean> data);
        void showStatus(String statusStr);
        RxAppCompatDialogFragment getRxFragment();
    }

    interface Presenter {
        void initAdapterData(int pageIndex, int pageSize);
        void deleteBeans(List<DataListBean> allBean);
    }
}
