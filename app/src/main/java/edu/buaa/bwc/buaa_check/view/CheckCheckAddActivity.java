package edu.buaa.bwc.buaa_check.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import edu.buaa.bwc.buaa_check.Api.CheckCheckService;
import edu.buaa.bwc.buaa_check.POJOs.CheckRole;
import edu.buaa.bwc.buaa_check.POJOs.CheckUnit;
import edu.buaa.bwc.buaa_check.POJOs.OtherCheckPeople;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckCheckAddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mCheckRole;
    private EditText mCheckRecorder;
    private EditText mOtherCheckPeople;
    private EditText mCheckCompany;
    private EditText mThreeClassCompany;
    private EditText mCheckTime;
    private EditText mCheckLocation;
    private EditText mFloor;
    private EditText mRoom;
    private Button mChoosePlateBt;
    private Button mSaveBt;

    private List<CheckRole> mCheckRoleData;
    private List<OtherCheckPeople> mOtherCheckPeopleData;
    private List<CheckUnit> mCheckUnitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_check_check_add);
        setTitle("添加");
        initView();
        loadDate();
    }

    private void initView() {
        mCheckRole = (EditText) findViewById(R.id.check_role);
        mCheckRole.setOnClickListener(this);
        mCheckRecorder = (EditText) findViewById(R.id.check_recorder);
        mCheckRecorder.setOnClickListener(this);
        mOtherCheckPeople = (EditText) findViewById(R.id.other_check_people);
        mOtherCheckPeople.setOnClickListener(this);
        mCheckCompany = (EditText) findViewById(R.id.check_company);
        mCheckCompany.setOnClickListener(this);
        mThreeClassCompany = (EditText) findViewById(R.id.three_class_company);
        mThreeClassCompany.setOnClickListener(this);
        mCheckTime = (EditText) findViewById(R.id.check_time);
        mCheckTime.setOnClickListener(this);
        mCheckLocation = (EditText) findViewById(R.id.check_location);
        mCheckLocation.setOnClickListener(this);
        mFloor = (EditText) findViewById(R.id.floor);
        mFloor.setOnClickListener(this);
        mRoom = (EditText) findViewById(R.id.room);
        mRoom.setOnClickListener(this);

        mChoosePlateBt = (Button) findViewById(R.id.choose_plate);
        mSaveBt = (Button) findViewById(R.id.save);
        mChoosePlateBt.setOnClickListener(this);
        mSaveBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_plate:
                Intent intent = new Intent(this, CheckAddUploadActivity.class);
                startActivity(intent);
                break;
            case R.id.save:
                break;
            case R.id.check_role:
                if (mCheckRoleData == null || mCheckRoleData.size() == 0) break;
                String[] checkRoleStrs = new String[mCheckRoleData.size()];
                for (int i = 0; i < mCheckRoleData.size(); i++) {
                    checkRoleStrs[i] = mCheckRoleData.get(i).getPosition();
                }
                showDialog(checkRoleStrs, mCheckRole);
                break;
            case R.id.other_check_people:
                if (mOtherCheckPeopleData == null || mOtherCheckPeopleData.size() == 0) break;
                String otherCheckPeopleStrs[] = new String[mOtherCheckPeopleData.size()];
                for (int i = 0; i < mOtherCheckPeopleData.size(); i++) {
                    otherCheckPeopleStrs[i] = mOtherCheckPeopleData.get(i).getText();
                }
                showDialog(otherCheckPeopleStrs, mOtherCheckPeople);
                break;
            case R.id.check_company:
                if (mCheckUnitData == null || mCheckUnitData.size() == 0) break;
                String[] checkUnitDataStrs = new String[mCheckUnitData.size()];
                for (int i = 0; i < mCheckUnitData.size(); i++) {
                    checkUnitDataStrs[i] = mCheckUnitData.get(i).getText();
                }
                showDialog(checkUnitDataStrs, mCheckCompany);
                break;
        }
    }

    private void loadDate() {
        CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
        Call<List<CheckRole>> callOne = service.getCheckRole();
        callOne.enqueue(new Callback<List<CheckRole>>() {
            @Override
            public void onResponse(Call<List<CheckRole>> call, Response<List<CheckRole>> response) {
                mCheckRoleData = response.body();
                if (mCheckRoleData != null && mCheckRoleData.size() > 0) {
                    mCheckRecorder.setText(mCheckRoleData.get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<CheckRole>> call, Throwable t) {

            }
        });
        Call<List<OtherCheckPeople>> callTwo = service.getOtherCheckPeople();
        callTwo.enqueue(new Callback<List<OtherCheckPeople>>() {
            @Override
            public void onResponse(Call<List<OtherCheckPeople>> call, Response<List<OtherCheckPeople>> response) {
                mOtherCheckPeopleData = response.body();
            }

            @Override
            public void onFailure(Call<List<OtherCheckPeople>> call, Throwable t) {

            }
        });
        Call<List<CheckUnit>> callThree = service.getCheckUnit("0");
        callThree.enqueue(new Callback<List<CheckUnit>>() {
            @Override
            public void onResponse(Call<List<CheckUnit>> call, Response<List<CheckUnit>> response) {
                mCheckUnitData = response.body();
            }

            @Override
            public void onFailure(Call<List<CheckUnit>> call, Throwable t) {

            }
        });

    }

    private void showDialog(final String[] data, final EditText editText) {
        new AlertDialog.Builder(this).setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.setText(data[which]);
            }
        }).show();
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
