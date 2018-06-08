package com.postman.ui.module.main.data.presenter;

import com.postman.db.entity.DataEntity;
import com.postman.db.helper.DataHelper;
import com.postman.ui.module.main.data.bean.DataListBean;
import com.postman.ui.module.main.data.contract.DataContract;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import java.util.LinkedList;
import java.util.List;


public class DataPresenter implements DataContract.Presenter {
    private final static String TAG = "DataPresenter";

    private DataContract.View mView;
    private RxAppCompatDialogFragment activity;

    private LinkedList<DataListBean> allData;
    private DataHelper helper;


    public DataPresenter(DataContract.View androidView) {
        this.mView = androidView;
        this.activity = androidView.getRxFragment();
        this.helper = DataHelper.getInstance();
    }

    @Override
    public void initAdapterData(int pageIndex, int pageSize) {
        if(allData == null){
            allData = new LinkedList<DataListBean>();
        }else{
            allData.clear();
        }

        List<DataEntity> allMess = helper.queryList();//for user
        if (allMess == null) {
            return;
        }

        for (int i = 0; (i < allMess.size() && i < (pageIndex+pageSize)); i++) {
            DataEntity oneMess = allMess.get(i);

            DataListBean temp = new DataListBean.Builder(oneMess.getData_id())
                    .content(oneMess.getData_name()).build();
            allData.add(temp);
        }
        mView.setRecyclerData(allData);
    }

    @Override
    public void deleteBeans(List<DataListBean> allBean) {
        for (DataListBean bean : allBean) {
            DataEntity entity = helper.queryListByVideoId(bean.getId());
            helper.delete(entity);
        }
    }
}
