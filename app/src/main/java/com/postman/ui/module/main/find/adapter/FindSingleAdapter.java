package com.postman.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.hint.listener.OnChooseListener;
import com.postman.R;
import com.postman.config.Cache;
import com.postman.config.enums.OptionsConfig;
import com.postman.db.cache.PostmanCache;
import com.postman.ui.module.main.find.bean.KeyListBean;
import com.postman.ui.module.main.find.utils.FindCacheUtil;

import java.util.ArrayList;

public class FindSingleAdapter extends DelegateAdapter.Adapter<FindSingleAdapter.FindSingleHolder> {

    private Context context;
    private LayoutHelper layoutHelper;
    private OptionsConfig option;

    private PostmanCache myCache;
    ArrayList<KeyListBean> allDatas;
    KeyListAdapter keyListAdapter;

    public FindSingleAdapter(Context context, LayoutHelper layoutHelper) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        initAllData();
    }

    private void initAllData(){
        myCache = PostmanCache.get(context);
        FindCacheUtil.initKey(myCache);
        setOption(OptionsConfig.HEADER);
    }

    private void setOption(OptionsConfig option) {
        this.option = option;
        myCache.put(Cache.CACHE_OPTION, option.name());
    }

    public void loadInputAdapter() {
        allDatas = new ArrayList<>();
        allDatas.addAll(FindCacheUtil.getKeyBean(myCache, option));

        if (keyListAdapter == null) {
            keyListAdapter = new KeyListAdapter(context, allDatas);
            keyListAdapter.setOnListener(new OnChooseListener() {
                @Override
                public void onPositive(int pos) {
                    FindCacheUtil.delete(myCache, option, pos);
                }
            });
        } else {
            keyListAdapter.setDatas(allDatas);
            keyListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FindSingleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindSingleHolder(LayoutInflater.from(context).inflate(R.layout.item_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final FindSingleHolder holder, int position) {
        holder.rb_head.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setOption(OptionsConfig.HEADER);
                    holder.rb_head.setTextColor(context.getResources().getColor(R.color.color_light_white));
                    loadInputAdapter();
                    notifyDataSetChanged();
                } else {
                    holder.rb_head.setTextColor(context.getResources().getColor(R.color.color_green));
                }
            }
        });
        holder.rb_body.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setOption(OptionsConfig.BODY);
                    holder.rb_body.setTextColor(context.getResources().getColor(R.color.color_light_white));
                    loadInputAdapter();
                    notifyDataSetChanged();
                } else {
                    holder.rb_body.setTextColor(context.getResources().getColor(R.color.color_green));
                }
            }
        });

        loadInputAdapter();
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(keyListAdapter);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class FindSingleHolder extends RecyclerView.ViewHolder {

        RadioButton rb_head;
        RadioButton rb_body;

        RecyclerView recyclerView;

        public FindSingleHolder(View itemView) {
            super(itemView);
            rb_head = itemView.findViewById(R.id.rb_head);
            rb_body = itemView.findViewById(R.id.rb_body);

            recyclerView = itemView.findViewById(R.id.recycle_view);
        }
    }
}
