package com.postman.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.base.utils.IPUtil;
import com.hint.utils.ToastUtils;
import com.postman.R;
import com.postman.app.listener.OnRequestListener;
import com.postman.config.Types;


public class FindStickyAdapter extends DelegateAdapter.Adapter<FindStickyAdapter.FindStickyHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private OnRequestListener onRequestListener;
    private int count = 1;

    private int typeIndex = 0;

    public FindStickyAdapter(Context context, LayoutHelper layoutHelper, OnRequestListener listener) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.onRequestListener = listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public FindStickyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FindStickyHolder(context, LayoutInflater.from(context).inflate(R.layout.item_sticky_item, parent,false));
    }

    @Override
    public void onBindViewHolder(final FindStickyHolder holder, int position) {
        holder.tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(context)
                        .items(Types.getAll())
                        .widgetColorRes(R.color.color_green)
                        .itemsCallbackSingleChoice(typeIndex, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                typeIndex=which;
                                holder.tvType.setText(Types.value(typeIndex).name());
                                return true;
                            }
                        }).show();
            }
        });
        holder.ic_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.search_bar.setText("");
            }
        });
        holder.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = holder.search_bar.getText().toString();
                if(TextUtils.isEmpty(url)){
                    ToastUtils.showToast(context, "need request url");
                    return;
                }

                url = url.trim();
                if(!IPUtil.isValidAddress(url)){
                    ToastUtils.showToast(context, "invalid address");
                }else{
                    onRequestListener.onStart(url, Types.value(typeIndex));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public static class FindStickyHolder extends RecyclerView.ViewHolder {
        TextView tvType;

        AutoCompleteTextView search_bar;
        ImageView ic_empty;
        ImageView tvSearch;

        private String[] allStrs = {"http://", "https://", "https://www", "http://172.16.93.111:8080"};

        public FindStickyHolder(Context context, View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            search_bar = itemView.findViewById(R.id.search_bar);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, allStrs);
            search_bar.setAdapter(adapter);

            ic_empty = itemView.findViewById(R.id.ic_empty);
            tvSearch = itemView.findViewById(R.id.tvSearch);
        }
    }
}
