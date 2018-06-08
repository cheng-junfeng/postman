package com.postman.ui.module.main.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.base.app.event.RxBusHelper;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseCompatFragment;
import com.postman.app.event.NoteEvent;
import com.postman.app.event.NoteType;
import com.postman.app.listener.OnRequestListener;
import com.postman.config.Types;
import com.postman.net.CallServer;
import com.postman.net.HttpListener;
import com.postman.ui.module.main.find.adapter.FindSingle2Adapter;
import com.postman.ui.module.main.find.adapter.FindSingleAdapter;
import com.postman.ui.module.main.find.adapter.FindStickyAdapter;
import com.postman.ui.module.main.find.contract.FindFragmentContract;
import com.postman.ui.module.main.find.presenter.FindFragmentPresenter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;


import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class FindFragment extends BaseCompatFragment implements FindFragmentContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FindFragmentContract.Presenter presenter;
    private List<DelegateAdapter.Adapter> mAdapters;

    DelegateAdapter delegateAdapter;
    FindSingleAdapter singleAdapter1;
    FindSingle2Adapter singleAdapter2;

    private Context mContext;

    @Override
    protected int setContentView() {
        return R.layout.fragment_find;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        mContext = this.getContext();
        RxBusHelper.doOnMainThread(this, NoteEvent.class, new RxBusHelper.OnEventListener<NoteEvent>() {
            @Override
            public void onEvent(NoteEvent noteEvent) {
                if((noteEvent.getType() == NoteType.NEW) || (noteEvent.getType() == NoteType.DELETE)){
                    reloadNote();
                }
            }
        });
        presenter = new FindFragmentPresenter(this, this);
        initView();
        return containerView;
    }

    public void initView() {
        mAdapters = new LinkedList<>();
        initRecyclerView();
    }

    private void initRecyclerView() {
        delegateAdapter = presenter.initRecyclerView(recyclerView);
        //设置Sticky布局
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(true);
        mAdapters.add(new FindStickyAdapter(this.getContext(), stickyLayoutHelper, new OnRequestListener() {
            @Override
            public void onStart(String url, Types type) {
                switch (type){
                    case GET:
                        getRequest(url);
                        break;
                    default:break;
                }
            }
        }));

        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        singleAdapter1 = new FindSingleAdapter(this.getContext(), singleLayoutHelper, 1);
        mAdapters.add(singleAdapter1);

        singleAdapter2 = new FindSingle2Adapter(this.getContext(), singleLayoutHelper, 1);
        mAdapters.add(singleAdapter2);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
    }

    private void reloadNote(){
        initView();
    }

    private void getRequest(String url){
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
            CallServer.getInstance().request(this.getActivity(), 1, stringRequest, new HttpListener<String>() {
                @Override
                public void onSucceed(int what, Response<String> response) {
                    String responseString = response.get();
                    if (!TextUtils.isEmpty(responseString)) {
                        singleAdapter2.setContent(responseString);
                    }else {
                        ToastUtils.showToast(mContext, "success: no data");
                    }
                }

                @Override
                public void onFailed(int what, Response<String> response) {

                }
            },true,true);
    }
}
