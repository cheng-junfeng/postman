package com.postman.ui.module.main.find.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.hint.utils.ToastUtils;
import com.postman.R;


public class FindStickyAdapter extends DelegateAdapter.Adapter<FindStickyAdapter.FindStickyHolder> {
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;

    private int typeIndex = 0;
    private String[] allTypes = {"GET", "POST", "PUT", "PATCH", "DELETE", "COPY", "HEAD"};

    public FindStickyAdapter(Context context, LayoutHelper layoutHelper, int count) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
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
                        .items(allTypes)
                        .widgetColorRes(R.color.color_green)
                        .itemsCallbackSingleChoice(typeIndex, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                typeIndex=which;
                                holder.tvType.setText(allTypes[typeIndex]);
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
                ToastUtils.showToast(context, "Start request");
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

        private String[] allStrs = {"http://", "https://", "http://172.", "https://www"};

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
