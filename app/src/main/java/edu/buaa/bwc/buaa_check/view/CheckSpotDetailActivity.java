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

import edu.buaa.bwc.buaa_check.Api.CheckSpotService;
import edu.buaa.bwc.buaa_check.POJOs.CheckSpotDetail;
import edu.buaa.bwc.buaa_check.POJOs.CheckSpotDetailHeader;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.GsonUtils;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import edu.buaa.bwc.buaa_check.adapter.MyCheckSpotDetailAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckSpotDetailActivity extends AppCompatActivity {

    private TextView checkName;
    private TextView otherCheckName;
    private TextView checkUnit;
    private TextView checkTime;
    private TextView checkLocation;
    private MyCheckSpotDetailAdapter myCheckSpotDetailAdapter;
    private List<CheckSpotDetail> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_check_spot_detail_avtivity);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        setTitle(intent.getStringExtra("title"));
        initView();
        loadData(id);
        startActivity(new Intent());
    }

    private void initView() {
        checkName = (TextView) findViewById(R.id.checkspotName);
        otherCheckName = (TextView) findViewById(R.id.otherCheckspotName);
        checkUnit = (TextView) findViewById(R.id.checkspotUnit);
        checkTime = (TextView) findViewById(R.id.checkspotTime);
        checkLocation = (TextView) findViewById(R.id.checkspotLocation);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.checkspot_detail_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        myCheckSpotDetailAdapter = new MyCheckSpotDetailAdapter(mData);
        recyclerView.setAdapter(myCheckSpotDetailAdapter);
    }

    private void loadData(String id) {
        if (id == null) {
            Toast.makeText(this, "未获取记录ID", Toast.LENGTH_SHORT).show();
        }
        System.out.println(id);
        CheckSpotService service = RetrofitWrapper.getInstance().create(CheckSpotService.class);
        Call<CheckSpotDetailHeader> call = service.getCheckDetailHeader(id);
        call.enqueue(new Callback<CheckSpotDetailHeader>() {
            @Override
            public void onResponse(Call<CheckSpotDetailHeader> call, Response<CheckSpotDetailHeader> response) {
                CheckSpotDetailHeader header = response.body();
                if (header != null) {
                    checkName.setText(header.name);
                    otherCheckName.setText(header.otherName);
                    checkUnit.setText(header.unit);
                    checkTime.setText(header.checkTime);
                    checkLocation.setText(header.checkAdd);
                }
            }

            @Override
            public void onFailure(Call<CheckSpotDetailHeader> call, Throwable t) {
                Toast.makeText(CheckSpotDetailActivity.this, "获取Header数据失败", Toast.LENGTH_SHORT).show();
            }
        });
        CheckSpotService detailService = RetrofitWrapper.getInstance().createScalar(CheckSpotService.class);
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
                        mData = GsonUtils.jsonToList(checkDetailArray.toString(), CheckSpotDetail[].class);
                        myCheckSpotDetailAdapter.setData(mData);
                        myCheckSpotDetailAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(CheckSpotDetailActivity.this, "获取Detail数据失败", Toast.LENGTH_SHORT).show();
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
