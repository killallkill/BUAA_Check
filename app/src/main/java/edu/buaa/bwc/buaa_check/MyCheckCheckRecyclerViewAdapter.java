package edu.buaa.bwc.buaa_check;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.buaa.bwc.buaa_check.Api.CheckCheckService;
import edu.buaa.bwc.buaa_check.POJOs.CheckCheckItem;
import edu.buaa.bwc.buaa_check.POJOs.DeleteCheckResponse;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyCheckCheckRecyclerViewAdapter extends RecyclerView.Adapter<MyCheckCheckRecyclerViewAdapter.ViewHolder> {

    private final List<CheckCheckItem> mData;
    private OnRecycleViewItemClickListener mClickListener;

    public MyCheckCheckRecyclerViewAdapter(List<CheckCheckItem> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_checkcheck_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mNameView.setText(mData.get(position).name);
        holder.mTimeView.setText(mData.get(position).checkTime);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnRecycleViewItemClickListener listener) {
        this.mClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mTimeView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.checkcheck_item_name);
            mTimeView = (TextView) view.findViewById(R.id.checkcheck_item_time);

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
                MenuItem menuItem = menu.add("删除");
                menuItem.setOnMenuItemClickListener(mOnMenuItemClickListener);
            }
        };

        private final MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int position = getAdapterPosition();
                CheckCheckItem checkCheckItemitem = mData.get(position);
                CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
                Call<DeleteCheckResponse> call = service.delCheckCheckItem(checkCheckItemitem.id, checkCheckItemitem.userId);
                call.enqueue(new Callback<DeleteCheckResponse>() {
                    @Override
                    public void onResponse(Call<DeleteCheckResponse> call, Response<DeleteCheckResponse> response) {
                        DeleteCheckResponse dcr = response.body();
                        if (dcr.success) {
                            Snackbar.make(mView, dcr.message, Snackbar.LENGTH_SHORT).show();
                            mData.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            Snackbar.make(mView, "Something wrong.", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteCheckResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                return true;
            }
        };
    }

    public interface OnRecycleViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
