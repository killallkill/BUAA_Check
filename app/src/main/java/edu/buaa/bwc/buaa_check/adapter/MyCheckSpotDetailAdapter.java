package edu.buaa.bwc.buaa_check.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.buaa.bwc.buaa_check.POJOs.CheckSpotDetail;
import edu.buaa.bwc.buaa_check.R;


public class MyCheckSpotDetailAdapter extends RecyclerView.Adapter<MyCheckSpotDetailAdapter.ViewHolder> {

    private List<CheckSpotDetail> mData;

    public MyCheckSpotDetailAdapter(List<CheckSpotDetail> data) {
        mData = data;
    }

    public void setData(List<CheckSpotDetail> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checkspot_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.checkspot_name.setText(mData.get(position).text);
        String value = mData.get(position).val;
        holder.checkspot_result.setText(TextUtils.isDigitsOnly(value) ? "分值:" + value :
                value.equalsIgnoreCase("N") ? "不合格" : "合格");
        holder.checkspot_standard.setText(mData.get(position).remark);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView checkspot_name;
        private final TextView checkspot_result;
        private final TextView checkspot_standard;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            checkspot_name = (TextView) view.findViewById(R.id.checkspot_name);
            checkspot_result = (TextView) view.findViewById(R.id.checkspot_result);
            checkspot_standard = (TextView) view.findViewById(R.id.checkspot_standard);
        }
    }
}
