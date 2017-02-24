package edu.buaa.bwc.buaa_check.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.buaa.bwc.buaa_check.POJOs.SelfCheckDetail;
import edu.buaa.bwc.buaa_check.R;


public class MySelfCheckDetailAdapter extends RecyclerView.Adapter<MySelfCheckDetailAdapter.ViewHolder> {

    private List<SelfCheckDetail> mData;

    public MySelfCheckDetailAdapter(List<SelfCheckDetail> data) {
        mData = data;
    }

    public void setData(List<SelfCheckDetail> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selfcheck_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.check_name.setText(mData.get(position).text);
        String value = mData.get(position).val;
        holder.check_result.setText(TextUtils.isDigitsOnly(value) ? "分值:" + value :
                value.equalsIgnoreCase("N") ? "不合格" : "合格");
        //holder.check_standard.setText(mData.get(position).remark);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView check_name;
        private final TextView check_result;
        private final TextView check_standard;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            check_name = (TextView) view.findViewById(R.id.check_name);
            check_result = (TextView) view.findViewById(R.id.check_result);
            check_standard = (TextView) view.findViewById(R.id.check_standard);
        }
    }
}
