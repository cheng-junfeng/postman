package com.postman.ui.module.main.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.app.adapter.BaseRecyAdapter;
import com.base.app.listener.OnClickLongListener;
import com.postman.R;
import com.postman.ui.module.main.data.bean.DataListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataListAdapter extends BaseRecyAdapter<DataListAdapter.ViewHolder> {

    OnClickLongListener mOnClickListener;
    List<DataListBean> data;
    List<DataListBean> removeData;
    boolean isEditable = false;

    Context mContext;

    public DataListAdapter(Context context, List<DataListBean> data) {
        this.mContext = context;
        this.data = data;
    }

    public List<DataListBean> getRemoveData() {
        return removeData;
    }

    public void setEditable(boolean isEdit) {
        if (isEdit) {
            removeData = new ArrayList<>();
        } else {
            if (removeData != null) {
                removeData.clear();
            }
        }
        isEditable = isEdit;
        notifyDataSetChanged();
    }

    public void setDatas(List<DataListBean> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.data = datas;
    }

    public void setOnListener(OnClickLongListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    @Override
    public void myBindViewHolder(final ViewHolder holder, final int position) {
        DataListBean userModel = data.get(position);
        holder.hmContent.setText(userModel.getContent());

        holder.hmRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnClickListener.onItemLongClick(position);
                return false;
            }
        });
        holder.hmRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnClickListener.onItemClick(position);

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_list, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public DataListBean getItem(int pos) {
        if (data == null || pos >= data.size()) {
            return null;
        }
        return data.get(pos);
    }

    public void removeData(int adapterPosition) {
        data.remove(adapterPosition);
    }

    public void clear() {
        data.clear();
    }

    public List<DataListBean> getData() {
        return data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hm_content)
        TextView hmContent;
        @BindView(R.id.hm_time)
        TextView hmTime;
        @BindView(R.id.hm_root)
        RelativeLayout hmRoot;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
