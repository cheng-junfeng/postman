package com.postman.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.custom.widget.MultiEditInputView;
import com.postman.R;


public class FindSingle2Adapter extends DelegateAdapter.Adapter<FindSingle2Adapter.FindSingleHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;
    private String defaultStr = "{output}";

    public FindSingle2Adapter(Context context, LayoutHelper layoutHelper, int count) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
    }

    public void setContent(String str) {
        defaultStr = str;
        notifyDataSetChanged();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FindSingleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindSingleHolder(LayoutInflater.from(context).inflate(R.layout.item_single2_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FindSingleHolder holder, int position) {
        holder.mevView.setContentText(defaultStr);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class FindSingleHolder extends RecyclerView.ViewHolder {
        MultiEditInputView mevView;
        public FindSingleHolder(View itemView) {
            super(itemView);
            mevView = itemView.findViewById(R.id.mev_view);
        }
    }
}
