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

import edu.buaa.bwc.buaa_check.Api.SelfCheckService;
import edu.buaa.bwc.buaa_check.POJOs.ListResponse;
import edu.buaa.bwc.buaa_check.POJOs.SelfCheckItem;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.adapter.MySelfCheckRecyclerViewAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {OnListFragmentInteractionListener}
 * interface.
 */
public class SelfCheckFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private List<SelfCheckItem> mData;
    private MySelfCheckRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SelfCheckFragment() {
    }

    // TODO: Customize parameter initialization
    public static SelfCheckFragment newInstance(int columnCount) {
        SelfCheckFragment fragment = new SelfCheckFragment();
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

        mData = new ArrayList<SelfCheckItem>();
        adapter = new MySelfCheckRecyclerViewAdapter(mData,getContext());
        adapter.setOnItemClickListener(new MySelfCheckRecyclerViewAdapter.OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SelfCheckFragment.this.getContext(), SelfCheckDetailActivity.class);
                intent.putExtra("id", mData.get(position).id);
                intent.putExtra("title",getActivity().getTitle());
                startActivity(intent);
            }
        });

        SelfCheckService service = edu.buaa.bwc.buaa_check.view.RetrofitWrapper.getInstance().create(SelfCheckService.class);
        Call<ListResponse<SelfCheckItem>> call = service.getSelfCheckList(1, 20);
        call.enqueue(new Callback<ListResponse<SelfCheckItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SelfCheckItem>> call, Response<ListResponse<SelfCheckItem>> response) {
                for (SelfCheckItem i : response.body().rows) {
                    mData.add(i);
                    Log.d("SelfCheckItem", i.toString());
                }
                if (mData.size() == 0) {
                    Toast.makeText(SelfCheckFragment.this.getContext(), "记录为空", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ListResponse<SelfCheckItem>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selfcheck_list, container, false);

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
            recyclerView.addItemDecoration(new edu.buaa.bwc.buaa_check.view.DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        }
        return view;
    }
}
