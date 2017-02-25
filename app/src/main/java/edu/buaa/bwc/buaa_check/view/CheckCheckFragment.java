package edu.buaa.bwc.buaa_check.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.buaa.bwc.buaa_check.Api.CheckCheckService;
import edu.buaa.bwc.buaa_check.POJOs.CheckCheckItem;
import edu.buaa.bwc.buaa_check.POJOs.ListResponse;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import edu.buaa.bwc.buaa_check.adapter.MyCheckCheckRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {OnListFragmentInteractionListener}
 * interface.
 */
public class CheckCheckFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private List<CheckCheckItem> mData;
    private MyCheckCheckRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CheckCheckFragment() {
    }

    // TODO: Customize parameter initialization
    public static CheckCheckFragment newInstance(int columnCount) {
        CheckCheckFragment fragment = new CheckCheckFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        mData = new ArrayList<CheckCheckItem>();
        adapter = new MyCheckCheckRecyclerViewAdapter(mData,getContext());
        adapter.setOnItemClickListener(new MyCheckCheckRecyclerViewAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(CheckCheckFragment.this.getContext(), CheckCheckDetailActivity.class);
                intent.putExtra("id", mData.get(position).id);
                intent.putExtra("title",getActivity().getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
        Call<ListResponse<CheckCheckItem>> call = service.getCheckCheckList(1, 20);
        call.enqueue(new Callback<ListResponse<CheckCheckItem>>() {
            @Override
            public void onResponse(Call<ListResponse<CheckCheckItem>> call, Response<ListResponse<CheckCheckItem>> response) {
                mData.clear();
                for (CheckCheckItem i : response.body().rows) {
                    mData.add(i);
                    Log.d("CheckCheckItem", i.toString());
                }
                if (mData.size() == 0) {
                    Toast.makeText(CheckCheckFragment.this.getContext(), "记录为空", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ListResponse<CheckCheckItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkcheck_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        }
        return view;
    }
}
