package com.postman.ui.module.main.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseCompatFragment;
import com.postman.ui.module.main.data.adapter.DataListAdapter;
import com.postman.ui.module.main.data.bean.DataListBean;
import com.postman.ui.module.main.data.contract.DataContract;
import com.postman.ui.module.main.data.presenter.DataPresenter;
import com.base.utils.LogUtil;
import com.base.app.listener.OnClickLongListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

import butterknife.BindView;

public class DataFragment extends BaseCompatFragment implements DataContract.View {

    public static final String TAG = "DataFragment";
    @BindView(R.id.main_view)
    PullLoadMoreRecyclerView mainView;

    private Context mContext;
    DataListAdapter mAdapter;

    private DataContract.Presenter presenter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        mContext = this.getContext();

        presenter = new DataPresenter(this);
        presenter.initAdapterData(pageIndex, pageSize);
        return containerView;
    }

    private void refresh() {
        presenter.initAdapterData(pageIndex, pageSize);
    }

    @Override
    public void setRecyclerData(List<DataListBean> data) {
        if (data == null || data.size() <= 0) {
            mainView.setVisibility(View.GONE);
            return;
        } else {
            mainView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new DataListAdapter(mContext, data);

            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    showStatus("click:" + position);
                }

                @Override
                public void onItemLongClick(int position) {
                    mAdapter.setEditable(true);
                }
            });

            mainView.setLinearLayout();
            mainView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
                @Override
                public void onRefresh() {
                    LogUtil.d(TAG, "onRefresh");
                    loadFresh();
                    presenter.initAdapterData(pageIndex, pageSize);
                    mainView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainView.setPullLoadMoreCompleted();
                        }
                    }, 2000);
                }

                @Override
                public void onLoadMore() {
                    LogUtil.d(TAG, "onLoadMore"+pageIndex);
                    loadMore();
                    presenter.initAdapterData(pageIndex, pageSize);
                    mainView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainView.setPullLoadMoreCompleted();
                        }
                    }, 2000);
                }
            });
            mainView.setAdapter(mAdapter);
        } else {
            mAdapter.setDatas(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showStatus(String toastStr) {
        ToastUtils.showToast(mContext, toastStr);
    }

    @Override
    public RxAppCompatDialogFragment getRxFragment() {
        return this;
    }
}
