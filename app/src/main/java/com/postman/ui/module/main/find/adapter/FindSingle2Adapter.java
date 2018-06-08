package com.postman.ui.module.main.find.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.base.utils.LogUtil;
import com.custom.widget.MultiEditInputView;
import com.hint.utils.ToastUtils;
import com.postman.R;


public class FindSingle2Adapter extends DelegateAdapter.Adapter<FindSingle2Adapter.FindSingleHolder> {
    private static final String TAG = "FindSingle2Adapter";
    private Context context;
    private LayoutHelper layoutHelper;
    private int count = 0;
    private String defaultStr = "{out}";

    public FindSingle2Adapter(Context context, LayoutHelper layoutHelper, int count) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
    }

    public void setContent(String str) {
        LogUtil.d(TAG, "setContent:"+str);
        if(TextUtils.isEmpty(str)){
            defaultStr = "{}";
        }else{
            defaultStr = str;
        }
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
        holder.mevView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(defaultStr);
                ToastUtils.showToast(context, "had copied");
                return true;
            }
        });
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
            mevView.setFocusable(false);
            mevView.setFocusableInTouchMode(false);
        }
    }
}
