package edu.buaa.bwc.buaa_check.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView mCheckRoleEt;
    private TextView mCheckRecorderEt;
    private TextView mOtherCheckPeopleEt;
    private TextView mCheckCompanyEt;
    private TextView mThreeClassCompanyEt;
    private TextView mCheckTimeEt;
    private TextView mCheckLocationEt;
    private TextView mFloorEt;
    private TextView mRoomEt;
    private Button mChoosePlateBt;
    private ProgressBar mProgressBar;

    private List<CheckRole> mCheckRoleData;
    private List<OtherCheckPeople> mOtherCheckPeopleData;
    private List<CheckUnit> mCheckUnitData;
    private List<CheckUnit> mThreeClassCheckUnit;

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
        mCheckRoleEt = (TextView) findViewById(R.id.check_role);
        mCheckRoleEt.setOnClickListener(this);
        mCheckRecorderEt = (TextView) findViewById(R.id.check_recorder);
        mCheckRecorderEt.setOnClickListener(this);
        mOtherCheckPeopleEt = (TextView) findViewById(R.id.other_check_people);
        mOtherCheckPeopleEt.setOnClickListener(this);
        mCheckCompanyEt = (TextView) findViewById(R.id.check_company);
        mCheckCompanyEt.setOnClickListener(this);
        mThreeClassCompanyEt = (TextView) findViewById(R.id.three_class_company);
        mThreeClassCompanyEt.setOnClickListener(this);
        mCheckTimeEt = (TextView) findViewById(R.id.check_time);
        mCheckTimeEt.setOnClickListener(this);
        mCheckLocationEt = (TextView) findViewById(R.id.check_location);
        mCheckLocationEt.setOnClickListener(this);
        mFloorEt = (TextView) findViewById(R.id.floor);
        mFloorEt.setOnClickListener(this);
        mRoomEt = (TextView) findViewById(R.id.room);
        mRoomEt.setOnClickListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setVisibility(View.VISIBLE);

        mChoosePlateBt = (Button) findViewById(R.id.choose_plate);
        mChoosePlateBt.setOnClickListener(this);
    }

    private void loadDate() {
        CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
        Call<List<CheckRole>> getCheckRoleCall = service.getCheckRole();
        getCheckRoleCall.enqueue(new Callback<List<CheckRole>>() {
            @Override
            public void onResponse(Call<List<CheckRole>> call, Response<List<CheckRole>> response) {
                mCheckRoleData = response.body();
                if (mCheckRoleData != null && mCheckRoleData.size() > 0) {
                    mCheckRecorderEt.setText(mCheckRoleData.get(0).getName());
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<CheckRole>> call, Throwable t) {
                Toast.makeText(CheckCheckAddActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
        Call<List<OtherCheckPeople>> getOtherCheckPeopleCall = service.getOtherCheckPeople();
        getOtherCheckPeopleCall.enqueue(new Callback<List<OtherCheckPeople>>() {
            @Override
            public void onResponse(Call<List<OtherCheckPeople>> call, Response<List<OtherCheckPeople>> response) {
                mOtherCheckPeopleData = response.body();
            }

            @Override
            public void onFailure(Call<List<OtherCheckPeople>> call, Throwable t) {

            }
        });
        Call<List<CheckUnit>> getCheckUnitCall = service.getCheckUnit("0");
        getCheckUnitCall.enqueue(new Callback<List<CheckUnit>>() {
            @Override
            public void onResponse(Call<List<CheckUnit>> call, Response<List<CheckUnit>> response) {
                mCheckUnitData = response.body();
            }

            @Override
            public void onFailure(Call<List<CheckUnit>> call, Throwable t) {

            }
        });
    }

    private void loadThreeClassUnitData(String id) {
        CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
        Call<List<CheckUnit>> getThreeClassUnitCall = service.getThreeClassUnit(id);
        getThreeClassUnitCall.enqueue(new Callback<List<CheckUnit>>() {
            @Override
            public void onResponse(Call<List<CheckUnit>> call, Response<List<CheckUnit>> response) {
                mThreeClassCheckUnit = response.body();
            }

            @Override
            public void onFailure(Call<List<CheckUnit>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_plate:
                Intent intent = new Intent(this, CheckAddUploadActivity.class);
                startActivity(intent);
                break;
            case R.id.check_role:
                if (mCheckRoleData == null || mCheckRoleData.size() == 0) break;
                String[] checkRoleStrs = new String[mCheckRoleData.size()];
                for (int i = 0; i < mCheckRoleData.size(); i++) {
                    checkRoleStrs[i] = mCheckRoleData.get(i).getPosition();
                }
                showSingleSelectDialog(checkRoleStrs, mCheckRoleEt);
                break;
            case R.id.other_check_people:
                if (mOtherCheckPeopleData == null || mOtherCheckPeopleData.size() == 0) break;
                String otherCheckPeopleStrs[] = new String[mOtherCheckPeopleData.size()];
                for (int i = 0; i < mOtherCheckPeopleData.size(); i++) {
                    otherCheckPeopleStrs[i] = mOtherCheckPeopleData.get(i).getText();
                }
                showMultiSelectDialog(otherCheckPeopleStrs, mOtherCheckPeopleEt);
                break;
            case R.id.check_company:
                if (mCheckUnitData == null || mCheckUnitData.size() == 0) break;
                String[] checkUnitDataStrs = new String[mCheckUnitData.size()];
                for (int i = 0; i < mCheckUnitData.size(); i++) {
                    checkUnitDataStrs[i] = mCheckUnitData.get(i).getText();
                }
                showSingleSelectDialog(checkUnitDataStrs, mCheckCompanyEt);
                break;
            case R.id.three_class_company:
                if (mThreeClassCheckUnit == null || mThreeClassCheckUnit.size() == 0) break;
                String[] threeClassCheckUnitStrs = new String[mThreeClassCheckUnit.size()];
                for (int i = 0; i < mThreeClassCheckUnit.size(); i++) {
                    threeClassCheckUnitStrs[i] = mThreeClassCheckUnit.get(i).getText();
                }
                showSingleSelectDialog(threeClassCheckUnitStrs, mThreeClassCompanyEt);
                break;
            case R.id.check_time:
                showTimeSelectDialog();
                break;
        }
    }

    private void showSingleSelectDialog(final String[] data, final TextView editText) {
        new AlertDialog.Builder(this).setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editText.setText(data[which]);
                if (editText == mCheckCompanyEt) {
                    loadThreeClassUnitData(mCheckUnitData.get(which).getId());
                }
            }
        }).show();
    }

    private void showMultiSelectDialog(final String[] data, final TextView editText) {
        final boolean[] checked = new boolean[data.length];
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMultiChoiceItems(data, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < data.length; i++) {
                    if (checked[i]) {
                        sb.append(data[i]).append(";");
                    }
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                editText.setText(sb.toString());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        }).show();
    }

    private void showTimeSelectDialog() {
        View content = LayoutInflater.from(this).inflate(R.layout.select_time_dialog, null);
        final DatePicker datePicker = (DatePicker) content.findViewById(R.id.datePicker);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCheckTimeEt.setText(datePicker.getYear() + "-"
                        + datePicker.getMonth() + "-"
                        + datePicker.getDayOfMonth());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        }).setView(content).show();
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
