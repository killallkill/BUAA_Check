package edu.buaa.bwc.buaa_check.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import edu.buaa.bwc.buaa_check.Api.CheckCheckService;
import edu.buaa.bwc.buaa_check.POJOs.NormalResponse;
import edu.buaa.bwc.buaa_check.POJOs.RectifyUser;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 发起整改共用这个Activity
 * 请通过intent传递
 * @param id 检查的id
 * @param type 检查的类型，如 RectifySendActivity.TYPE_CHECK_CHECK
 */
public class RectifySendActivity extends AppCompatActivity {

    public static final String TYPE_CHECK_CHECK = "JC";
    public static final String TYPE_SELF_CHECK = "ZC";
    public static final String TYPE_SPOT_CHECK = "CC";

    private String type;
    private String id;
    private boolean isSending = false;

    private Spinner rectifyLevel;
    private EditText rectifyDay;
    private Spinner rectifyName;
    private EditText rectifyProposal;
    private Button submit;

    //280为一般隐患 281为重大隐患
    private String[] levelItems = {"请选择", "一般隐患", "重大隐患"};
    private List<RectifyUser> users;
    private ArrayAdapter<String> nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("发起整改");
        setContentView(R.layout.activity_rectify_send);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        id = intent.getStringExtra("id");

        initView();
    }

    private void initView() {
        rectifyLevel = (Spinner) findViewById(R.id.rectify_level);
        rectifyDay = (EditText) findViewById(R.id.rectify_day);
        rectifyName = (Spinner) findViewById(R.id.rectify_name);
        rectifyProposal = (EditText) findViewById(R.id.rectify_proposal);
        submit = (Button) findViewById(R.id.rectify_submit);

        initLevelSpinner();
        initNameSpinner();
        rectifyDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0)
                    return;
                int n = Integer.parseInt(s.toString());
                if (n > 100) {
                    rectifyDay.setText("100");
                }
                if (n <= 0) {
                    rectifyDay.setText("1");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSending && validateForm()) {
                    sendRectify();
                    isSending = true;
                }
            }
        });
    }

    private boolean validateForm() {
        Log.d("validate", "1");
        if (rectifyLevel.getSelectedItemPosition() <= 0) {
            setMessage("隐患级别错误");
            return false;
        }
        Log.d("validate", "2");
        if (rectifyDay.getText().length() == 0) {
            setMessage("整改期限错误");
            return false;
        }
        Log.d("validate", "3");
        if (rectifyName.getSelectedItemPosition() < 0) {
            setMessage("整改人错误");
            return false;
        }
        Log.d("validate", "4");
        return true;
    }

    private void sendRectify() {
        String level = String.valueOf(rectifyLevel.getSelectedItemPosition() + 279);
        int day = Integer.parseInt(rectifyDay.getText().toString());
        String urid = users.get(rectifyName.getSelectedItemPosition()).id;
        String proposal = rectifyProposal.getText().toString();

        switch (type) {
            case TYPE_CHECK_CHECK:
                CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
                Call<NormalResponse> call = service.sendRectify(level, id, day, urid, proposal, type);
                call.enqueue(new Callback<NormalResponse>() {
                    @Override
                    public void onResponse(Call<NormalResponse> call, Response<NormalResponse> response) {
                        isSending = false;
                        setMessage(response.body().message);
                        if (response.body().success) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<NormalResponse> call, Throwable t) {
                        t.printStackTrace();
                        isSending = false;
                        setMessage("Something error.");
                    }
                });
                break;
            case TYPE_SELF_CHECK:
                //TODO
                break;
            case TYPE_SPOT_CHECK:
                //TODO
                break;
        }
    }

    private void initLevelSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, levelItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rectifyLevel.setAdapter(adapter);
        rectifyLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;
                getRectifyUser(String.valueOf(279 + position));
                switch (position) {
                    case 1:
                        rectifyDay.setText("3");
                        break;
                    case 2:
                        rectifyDay.setText("30");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRectifyUser(String level) {
        switch (type) {
            case TYPE_CHECK_CHECK:
                CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
                Call<List<RectifyUser>> call = service.getRectifyUsers(level, id);
                call.enqueue(new Callback<List<RectifyUser>>() {
                    @Override
                    public void onResponse(Call<List<RectifyUser>> call, Response<List<RectifyUser>> response) {
                        users = response.body();
                        setNameSpinner();
                    }

                    @Override
                    public void onFailure(Call<List<RectifyUser>> call, Throwable t) {
                        t.printStackTrace();
                        setMessage("Something wrong.");
                    }
                });
                break;
            case TYPE_SELF_CHECK:
                //TODO
                break;
            case TYPE_SPOT_CHECK:
                //TODO
                break;
        }
    }

    private void setNameSpinner() {
        nameAdapter.clear();
        if (users != null && users.size() > 0) {
            for (RectifyUser i : users) {
                nameAdapter.add(i.pusername);
            }
        }
        nameAdapter.notifyDataSetChanged();
    }

    private void initNameSpinner() {
        nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rectifyName.setAdapter(nameAdapter);
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

    private void setMessage(String message) {
        Snackbar.make(submit, message, Snackbar.LENGTH_SHORT).show();
    }
}
