package com.postman.ui.module.main.find.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.hint.listener.OnChooseListener;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.config.Cache;
import com.postman.config.enums.OptionsConfig;
import com.postman.db.cache.PostmanCache;
import com.postman.db.cache.bean.InputBean;
import com.postman.ui.module.main.find.bean.KeyListBean;
import com.postman.ui.module.main.find.view.InputAddActvity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
        myCache = PostmanCache.get(context);
        setOption(OptionsConfig.HEADER);

    }

    private void setOption(OptionsConfig option){
        this.option = option;
        myCache.put(Cache.CACHE_OPTION, option.name());
    }

    public void loadInputAdapter(){
        allDatas = new ArrayList<KeyListBean>();

        InputBean oldEntity = (InputBean) myCache.getAsObject(option.name());
        if(oldEntity != null && oldEntity.keyValue != null){
            for(HashMap.Entry<String, String> entry: oldEntity.keyValue.entrySet()) {
                KeyListBean tempBean = new KeyListBean();
                tempBean.key = entry.getKey();
                tempBean.value = entry.getValue();
                allDatas.add(tempBean);
            }
        }

        if (keyListAdapter == null) {
            keyListAdapter = new KeyListAdapter(context, allDatas);
            keyListAdapter.setOnListener(new OnChooseListener() {
                @Override
                public void onPositive(int pos) {
                    KeyListBean bean = keyListAdapter.getItem(pos);
                    InputBean oldEntity = (InputBean) myCache.getAsObject(option.name());
                    if(oldEntity != null && oldEntity.keyValue != null){
                        int count = 0;
                        for (Iterator<HashMap.Entry<String, String>> it = oldEntity.keyValue.entrySet().iterator(); it.hasNext();){
                            if(pos == count){
                                it.remove();
                                break;
                            }
                            count++;
                        }
                    }
                    keyListAdapter.delete(pos);
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
                ToastUtils.showToast(context, "Head:"+b);
                if(b){
                    setOption(OptionsConfig.HEADER);
                    loadInputAdapter();
                    holder.rb_head.setTextColor(context.getResources().getColor(R.color.color_light_white));
                }else{
                    holder.rb_head.setTextColor(context.getResources().getColor(R.color.color_green));
                }
            }
        });
        holder.rb_body.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ToastUtils.showToast(context, "rb_body:"+b);
                if(b){
                    setOption(OptionsConfig.BODY);
                    loadInputAdapter();holder.rb_body.setTextColor(context.getResources().getColor(R.color.color_light_white));
                }else{
                    holder.rb_body.setTextColor(context.getResources().getColor(R.color.color_green));
                }
            }
        });
        holder.rb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, InputAddActvity.class));
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

        ImageView rb_add;

        RecyclerView recyclerView;

        public FindSingleHolder(View itemView) {
            super(itemView);
            rb_head = itemView.findViewById(R.id.rb_head);
            rb_body = itemView.findViewById(R.id.rb_body);
            rb_add = itemView.findViewById(R.id.rb_add);

            recyclerView = itemView.findViewById(R.id.recycle_view);
        }
    }
}
