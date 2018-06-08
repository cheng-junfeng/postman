package com.postman.ui.module.main.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.base.app.event.RxBusHelper;
import com.base.app.listener.OnClickLongListener;
import com.base.utils.LogUtil;
import com.hint.listener.OnChooseListener;
import com.hint.utils.DialogUtils;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseCompatFragment;
import com.postman.app.event.MyEvent;
import com.postman.app.event.MyType;
import com.postman.config.Extra;
import com.postman.ui.module.main.data.view.DataDetailActvity;
import com.postman.ui.module.main.data.view.adapter.DataListAdapter;
import com.postman.ui.module.main.data.view.bean.DataListBean;
import com.postman.ui.module.main.data.contract.DataContract;
import com.postman.ui.module.main.data.presenter.DataPresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DataFragment extends BaseCompatFragment implements DataContract.View {

    public static final String TAG = "DataFragment";
    @BindView(R.id.main_view)
    PullLoadMoreRecyclerView mainView;
    @BindView(R.id.ly_empty)
    FrameLayout lyEmpty;

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
        RxBusHelper.doOnMainThread(this, MyEvent.class, new RxBusHelper.OnEventListener<MyEvent>() {
            @Override
            public void onEvent(MyEvent noteEvent) {
                if((noteEvent.getType() == MyType.DATE_UPDATE)){
                    refresh();
                }
            }
        });
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
            lyEmpty.setVisibility(View.VISIBLE);
            return;
        } else {
            lyEmpty.setVisibility(View.GONE);
            mainView.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new DataListAdapter(mContext, data);

            mAdapter.setOnListener(new OnClickLongListener() {
                @Override
                public void onItemClick(int position) {
                    DataListBean theBean = mAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(Extra.DATA_INPUT, theBean.getInput());
                    bundle.putString(Extra.DATA_OUTPUT, theBean.getOutput());
                    readGo(DataDetailActvity.class, bundle);
                }

                @Override
                public void onItemLongClick(final int position) {
                    List<String> allOptions = new ArrayList<>();
                    allOptions.add("Delete");
                    allOptions.add("Delete same type");
                    allOptions.add("Clear");
                    DialogUtils.showChooseDialog(mContext, allOptions, new OnChooseListener() {
                        @Override
                        public void onPositive(int pos) {
                            switch (pos) {
                                case 0:
                                    DataListBean theBean = mAdapter.getItem(position);
                                    presenter.deleteBeans(theBean);
                                    mAdapter.remove(theBean);
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                case 1:
                                    DataListBean theBean2 = mAdapter.getItem(position);
                                    List<DataListBean> allBeans = mAdapter.getItem(theBean2.getContent());
                                    presenter.deleteBeans(allBeans);
                                    mAdapter.remove(allBeans);
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                case 2:
                                    presenter.clear();
                                    mAdapter.clear();
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                            RxBusHelper.post(new MyEvent.Builder(MyType.DATE_UPDATE).build());
                        }
                    });
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
                    LogUtil.d(TAG, "onLoadMore" + pageIndex);
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
