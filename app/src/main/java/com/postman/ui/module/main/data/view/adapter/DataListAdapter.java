package com.postman.ui.module.main.data.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.app.adapter.BaseRecyAdapter;
import com.base.app.listener.OnClickLongListener;
import com.postman.R;
import com.postman.ui.module.main.data.view.bean.DataListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataListAdapter extends BaseRecyAdapter<DataListAdapter.ViewHolder> {

    OnClickLongListener mOnClickListener;
    List<DataListBean> data;

    Context mContext;

    public DataListAdapter(Context context, List<DataListBean> data) {
        this.mContext = context;
        this.data = data;
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
        holder.hmTime.setText(userModel.getTime());
        holder.txInput.setText(userModel.getInput());
        holder.txOutput.setText(userModel.getOutput());

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

    public List<DataListBean> getItem(String content) {
        List<DataListBean> allDatas = new ArrayList<>();
        for(DataListBean theBean : data){
            if(theBean.getContent().equals(content)){
                allDatas.add(theBean);
            }
        }
        return allDatas;
    }

    public void remove(DataListBean bean) {
        data.remove(bean);
    }

    public void remove(List<DataListBean> beans) {
        for(DataListBean bean: beans){
            remove(bean);
        }
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
        LinearLayout hmRoot;
        @BindView(R.id.tx_input)
        TextView txInput;
        @BindView(R.id.tx_output)
        TextView txOutput;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
