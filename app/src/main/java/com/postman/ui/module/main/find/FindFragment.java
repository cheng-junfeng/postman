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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.activity.BaseCompatFragment;
import com.postman.app.event.MyEvent;
import com.postman.app.event.MyType;
import com.postman.app.listener.OnRequestListener;
import com.postman.config.Extra;
import com.postman.config.enums.OptionsConfig;
import com.postman.config.enums.TypesConfig;
import com.postman.db.cache.PostmanCache;
import com.postman.net.CallServer;
import com.postman.net.HttpListener;
import com.postman.ui.module.main.find.adapter.FindSingle2Adapter;
import com.postman.ui.module.main.find.adapter.FindSingleAdapter;
import com.postman.ui.module.main.find.adapter.FindStickyAdapter;
import com.postman.ui.module.main.find.bean.KeyListBean;
import com.postman.ui.module.main.find.contract.FindFragmentContract;
import com.postman.ui.module.main.find.presenter.FindFragmentPresenter;
import com.postman.ui.module.main.find.utils.FindCacheUtil;
import com.postman.ui.module.main.find.utils.FindDBUtil;
import com.postman.ui.module.other.download.DownloadActvity;
import com.postman.ui.module.other.download.UploadActvity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class FindFragment extends BaseCompatFragment implements FindFragmentContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private FindFragmentContract.Presenter presenter;
    private List<DelegateAdapter.Adapter> mAdapters;

    DelegateAdapter delegateAdapter;

    FindStickyAdapter stickyAdapter;
    FindSingleAdapter singleAdapter1;
    FindSingle2Adapter singleAdapter2;

    private Context mContext;
    private PostmanCache myCache;

    @Override
    protected int setContentView() {
        return R.layout.fragment_find;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);
        mContext = this.getContext();
        myCache = PostmanCache.get(mContext);
        RxBusHelper.doOnMainThread(this, MyEvent.class, new RxBusHelper.OnEventListener<MyEvent>() {
            @Override
            public void onEvent(MyEvent noteEvent) {
                if (noteEvent.getType() == MyType.NOTE_NEW) {
                } else if (noteEvent.getType() == MyType.INPUT_UPDATE) {
                    loadInput();
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

        stickyAdapter = new FindStickyAdapter(this.getContext(), stickyLayoutHelper, new OnRequestListener() {
            @Override
            public void onStart(String url, TypesConfig type) {
                switch (type) {
                    case GET:
                        getRequest(url);
                        break;
                    case POST:
                        postRequest(url);
                        break;
                    case DOWN:
                        downloadRequest(url);
                        break;
                    case UPLOAD:
                        uploadRequest(url);
                        break;
                    default:
                        break;
                }
            }
        });
        mAdapters.add(stickyAdapter);

        SingleLayoutHelper singleLayoutHelper = new SingleLayoutHelper();
        singleAdapter1 = new FindSingleAdapter(this.getContext(), singleLayoutHelper);
        mAdapters.add(singleAdapter1);

        singleAdapter2 = new FindSingle2Adapter(this.getContext(), singleLayoutHelper);
        mAdapters.add(singleAdapter2);

        //设置适配器
        delegateAdapter.setAdapters(mAdapters);
    }

    private void loadInput() {
        singleAdapter1.loadInputAdapter();
        singleAdapter1.notifyDataSetChanged();
    }

    private void getRequest(final String url) {
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.GET);
        ArrayList<KeyListBean> allHeader = FindCacheUtil.getKeyBean(myCache, OptionsConfig.HEADER);
        final JsonObject jsonObject = new JsonObject();
        JsonArray arrayHeader = new JsonArray();
        for (KeyListBean beans : allHeader) {
            if (beans.set) {
                stringRequest.addHeader(beans.key, beans.value);
                JsonObject obj = new JsonObject();
                obj.addProperty(beans.key, beans.value);
                arrayHeader.add(obj);
            }
        }
        jsonObject.add(OptionsConfig.HEADER.name(), arrayHeader);
        ArrayList<KeyListBean> allBody = FindCacheUtil.getKeyBean(myCache, OptionsConfig.BODY);
        JsonArray arrayBody = new JsonArray();
        for (KeyListBean beans : allBody) {
            if (beans.set) {
                stringRequest.add(beans.key, beans.value);
                JsonObject obj = new JsonObject();
                obj.addProperty(beans.key, beans.value);
                arrayBody.add(obj);
            }
        }
        jsonObject.add(OptionsConfig.BODY.name(), arrayBody);
        CallServer.getInstance().request(this.getActivity(), 1, stringRequest, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String responseString = response.get();
                if (!TextUtils.isEmpty(responseString)) {
                    singleAdapter2.setContent(responseString);
                    FindDBUtil.insertData(TypesConfig.GET, url, jsonObject.toString(), responseString);
                } else {
                    ToastUtils.showToast(mContext, "success: no data");
                    FindDBUtil.insertData(TypesConfig.GET, url, jsonObject.toString(), "success:no data");
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                FindDBUtil.insertData(TypesConfig.GET, url, jsonObject.toString(), "faild:" + response.getException());
            }
        }, true, true);
    }

    private void postRequest(final String url) {
        Request<String> stringRequest = NoHttp.createStringRequest(url, RequestMethod.POST);
        ArrayList<KeyListBean> allHeader = FindCacheUtil.getKeyBean(myCache, OptionsConfig.HEADER);
        final JsonObject jsonObject = new JsonObject();
        JsonArray arrayHeader = new JsonArray();
        for (KeyListBean beans : allHeader) {
            if (beans.set) {
                stringRequest.addHeader(beans.key, beans.value);
                JsonObject obj = new JsonObject();
                obj.addProperty(beans.key, beans.value);
                arrayHeader.add(obj);
            }
        }
        jsonObject.add(OptionsConfig.HEADER.name(), arrayHeader);
        ArrayList<KeyListBean> allBody = FindCacheUtil.getKeyBean(myCache, OptionsConfig.BODY);
        JsonArray arrayBody = new JsonArray();
        for (KeyListBean beans : allBody) {
            if (beans.set) {
                stringRequest.add(beans.key, beans.value);
                JsonObject obj = new JsonObject();
                obj.addProperty(beans.key, beans.value);
                arrayBody.add(obj);
            }
        }
        jsonObject.add(OptionsConfig.BODY.name(), arrayBody);
        CallServer.getInstance().request(this.getActivity(), 1, stringRequest, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String responseString = response.get();
                if (!TextUtils.isEmpty(responseString)) {
                    singleAdapter2.setContent(responseString);
                    FindDBUtil.insertData(TypesConfig.POST, url, jsonObject.toString(), responseString);
                } else {
                    ToastUtils.showToast(mContext, "success: no data");
                    FindDBUtil.insertData(TypesConfig.POST, url, jsonObject.toString(), "success:no data");
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                FindDBUtil.insertData(TypesConfig.POST, url, jsonObject.toString(), "faild:" + response.getException());
            }
        }, true, true);
    }

    private void downloadRequest(final String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Extra.DATA_URL, url);
        readGo(DownloadActvity.class, bundle);
    }

    private void uploadRequest(final String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Extra.DATA_URL, url);
        readGo(UploadActvity.class, bundle);
    }
}
