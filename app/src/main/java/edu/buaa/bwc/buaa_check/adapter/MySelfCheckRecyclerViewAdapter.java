package edu.buaa.bwc.buaa_check.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.buaa.bwc.buaa_check.Api.SelfCheckService;
import edu.buaa.bwc.buaa_check.POJOs.NormalResponse;
import edu.buaa.bwc.buaa_check.POJOs.SelfCheckItem;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import edu.buaa.bwc.buaa_check.view.RectifySendActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MySelfCheckRecyclerViewAdapter extends RecyclerView.Adapter<MySelfCheckRecyclerViewAdapter.ViewHolder> {

    private final List<SelfCheckItem> mData;
    private OnRecycleViewItemClickListener mClickListener;
    private Context mContext;

    public MySelfCheckRecyclerViewAdapter(List<SelfCheckItem> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_selfcheck_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mNameView.setText(mData.get(position).name);
        holder.mTimeView.setText(mData.get(position).checkTime);
        holder.isOK.setText(mData.get(position).qualified.equals("0") ? "合格" : "不合格");
        holder.mStatus.setText(mData.get(position).rectifyStateVal);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener listener) {
        this.mClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mNameView;
        private final TextView mTimeView;
        private final TextView isOK;
        private final TextView mStatus;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.selfcheck_item_name);
            mTimeView = (TextView) view.findViewById(R.id.selfcheck_item_time);
            isOK = (TextView) view.findViewById(R.id.selfcheck_item_isOK);
            mStatus = (TextView) view.findViewById(R.id.selfcheck_item_status);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mClickListener) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            mView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);
        }

        private final View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem menuItem1 = menu.add(1, 1, 1, "删除");
                menuItem1.setOnMenuItemClickListener(mOnMenuItemClickListener);
                SelfCheckItem selfCheckItem = mData.get(getAdapterPosition());
                if (selfCheckItem.qualified.equals("1") && selfCheckItem.rectifyState.equals("1")) {
                    MenuItem menuItem2 = menu.add(1, 2, 2, "整改");
                    menuItem2.setOnMenuItemClickListener(mOnMenuItemClickListener);
                }
            }
        };

        private final MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        final int position = getAdapterPosition();
                        SelfCheckItem selfCheckItemitem = mData.get(position);
                        SelfCheckService service = RetrofitWrapper.getInstance().create(SelfCheckService.class);
                        Call<NormalResponse> call = service.delSelfCheckItem(selfCheckItemitem.id, selfCheckItemitem.userId);
                        call.enqueue(new Callback<NormalResponse>() {
                            @Override
                            public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                                NormalResponse dcr = response.body();
                                if (dcr.success) {
                                    Snackbar.make(mView, dcr.message, Snackbar.LENGTH_SHORT).show();
                                    mData.remove(position);
                                    notifyItemRemoved(position);
                                } else {
                                    Snackbar.make(mView, "Something wrong.", Snackbar.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<NormalResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                        break;
                    case 2:
                        Intent intent = new Intent(mContext, RectifySendActivity.class);
                        intent.putExtra("type", RectifySendActivity.TYPE_SELF_CHECK);
                        intent.putExtra("id", mData.get(getAdapterPosition()).id);
                        mContext.startActivity(intent);
                        break;
                }
                return true;
            }
        };
    }

    public interface OnRecycleViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
