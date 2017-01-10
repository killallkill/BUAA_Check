package edu.buaa.bwc.buaa_check.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import edu.buaa.bwc.buaa_check.Api.CheckCheckService;
import edu.buaa.bwc.buaa_check.POJOs.CheckCheckDetail;
import edu.buaa.bwc.buaa_check.POJOs.CheckCheckDetailHeader;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.GsonUtils;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import edu.buaa.bwc.buaa_check.adapter.MyCheckCheckDetailAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckCheckDetailActivity extends AppCompatActivity {

    private TextView checkName;
    private TextView otherCheckName;
    private TextView checkUnit;
    private TextView checkTime;
    private TextView checkLocation;
    private MyCheckCheckDetailAdapter myCheckCheckDetailAdapter;
    private List<CheckCheckDetail> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_check_check_detail_avtivity);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        setTitle(intent.getStringExtra("title"));
        initView();
        loadData(id);
        startActivity(new Intent());
    }

    private void initView() {
        checkName = (TextView) findViewById(R.id.checkName);
        otherCheckName = (TextView) findViewById(R.id.otherCheckName);
        checkUnit = (TextView) findViewById(R.id.checkUnit);
        checkTime = (TextView) findViewById(R.id.checkTime);
        checkLocation = (TextView) findViewById(R.id.checkLocation);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.checkcheck_detail_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        myCheckCheckDetailAdapter = new MyCheckCheckDetailAdapter(mData);
        recyclerView.setAdapter(myCheckCheckDetailAdapter);
    }

    private void loadData(String id) {
        if (id == null) {
            Toast.makeText(this, "未获取记录ID", Toast.LENGTH_SHORT).show();
        }
        System.out.println(id);
        CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
        Call<CheckCheckDetailHeader> call = service.getCheckDetailHeader(id);
        call.enqueue(new Callback<CheckCheckDetailHeader>() {
            @Override
            public void onResponse(Call<CheckCheckDetailHeader> call, Response<CheckCheckDetailHeader> response) {
                CheckCheckDetailHeader header = response.body();
                if (header != null) {
                    checkName.setText(header.name);
                    otherCheckName.setText(header.otherName);
                    checkUnit.setText(header.unit);
                    checkTime.setText(header.checkTime);
                    checkLocation.setText(header.checkAdd);
                }
            }

            @Override
            public void onFailure(Call<CheckCheckDetailHeader> call, Throwable t) {
                Toast.makeText(CheckCheckDetailActivity.this, "获取Header数据失败", Toast.LENGTH_SHORT).show();
            }
        });
        CheckCheckService detailService = RetrofitWrapper.getInstance().createScalar(CheckCheckService.class);
        Call<String> detailCall = detailService.getCheckDetail(id);
        detailCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(response.body()).getAsJsonArray().get(0).getAsJsonObject();
                    JsonArray checkDetailArray = jsonObject.getAsJsonArray("children");
                    while (checkDetailArray != null && checkDetailArray.size() >= 1) {
                        if (checkDetailArray.get(0).getAsJsonObject().getAsJsonArray("children") != null) {
                            checkDetailArray = checkDetailArray.get(0).getAsJsonObject().getAsJsonArray("children");
                        } else {
                            break;
                        }
                    }
                    if (checkDetailArray != null) {
                        mData = GsonUtils.jsonToList(checkDetailArray.toString(), CheckCheckDetail[].class);
                        myCheckCheckDetailAdapter.setData(mData);
                        myCheckCheckDetailAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(CheckCheckDetailActivity.this, "获取Detail数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
