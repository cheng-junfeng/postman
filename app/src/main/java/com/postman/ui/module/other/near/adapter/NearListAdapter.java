package com.postman.ui.module.other.near.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.app.adapter.BaseRecyAdapter;
import com.hint.listener.OnChooseListener;
import com.postman.R;
import com.postman.ui.module.main.find.bean.KeyListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearListAdapter extends BaseRecyAdapter<NearListAdapter.ViewHolder> {

    OnChooseListener mOnClickListener;
    List<KeyListBean> data;

    Context mContext;

    public NearListAdapter(Context context, List<KeyListBean> data) {
        this.mContext = context;
        this.data = data;
    }

    public void setDatas(List<KeyListBean> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.data = datas;
    }

    public void setOnListener(OnChooseListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    @Override
    public void myBindViewHolder(final ViewHolder holder, final int position) {
        KeyListBean userModel = data.get(position);
        holder.ivItemContent.setText(userModel.key);
        holder.ivTime.setText(userModel.value);
        holder.ivItemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onPositive(position);
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_new, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public KeyListBean getItem(int pos) {
        if (data == null || pos >= data.size()) {
            return null;
        }
        return data.get(pos);
    }

    public void delete(int index) {
        data.remove(index);
    }

    public void clear() {
        data.clear();
    }

    public List<KeyListBean> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_itemContent)
        TextView ivItemContent;
        @BindView(R.id.iv_Time)
        TextView ivTime;
        @BindView(R.id.iv_itemview)
        LinearLayout ivItemview;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
