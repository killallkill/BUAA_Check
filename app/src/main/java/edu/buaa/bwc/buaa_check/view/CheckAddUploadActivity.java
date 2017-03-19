package edu.buaa.bwc.buaa_check.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.buaa.bwc.buaa_check.Api.CheckCheckService;
import edu.buaa.bwc.buaa_check.POJOs.UploadResponse;
import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.FileUtils;
import edu.buaa.bwc.buaa_check.Utils.RetrofitWrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckAddUploadActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PHOTO_SELECT = 100;
    public static final int CAMERA = 200;
    public static final int VIDEO = 300;
    private PopupWindow mSelectPW;
    private View mRootView;
    private TextView mFilePathTv;
    private ProgressBar mProgress;
    private Button mSaveBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_check_add_upload, null);
        setContentView(mRootView);
        setTitle("上传检查场景");
        findViewById(R.id.choose).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
        mFilePathTv = (TextView) findViewById(R.id.file_path);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PHOTO_SELECT);
        }
    }

    private void initPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.check_select_dialog, null, false);
        mSelectPW = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mSelectPW.setContentView(view);
        mSelectPW.setBackgroundDrawable(new BitmapDrawable());
        view.findViewById(R.id.photo_select).setOnClickListener(this);
        view.findViewById(R.id.camera).setOnClickListener(this);
        view.findViewById(R.id.video).setOnClickListener(this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mSelectPW.isShowing()) {
                    mSelectPW.dismiss();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.save:
                //Todo 保存记录
                break;
            case R.id.choose:
                showPopupWindow();
                break;
            case R.id.upload:
                if (mFilePathTv.getText().toString().trim().length() > 0)
                    upload(mFilePathTv.getText().toString());
                break;
            case R.id.photo_select:
                mSelectPW.dismiss();
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, PHOTO_SELECT);
                break;
            case R.id.camera:
                mSelectPW.dismiss();
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = FileUtils.getImageFile();
                if (file == null) break;
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA);
                break;
            case R.id.video:
                mSelectPW.dismiss();
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                File dir = new File(Environment.getExternalStorageDirectory() + "/buaaCheck");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File videoFile = new File(dir + "/" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()).format(new Date()) + ".mp4");
                Uri videoUri = Uri.fromFile(videoFile);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//0是low,1是high
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);// 以秒为单位
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024 * 1024);// 以字节为单位
                intent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true);// 以字节为单位
                intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);// 默认值为true,这意味着自动退出电影播放器活动电影完成后玩。
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(intent, VIDEO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case PHOTO_SELECT:
                Uri imageUri = data.getData();
                mFilePathTv.setText(imageUri.getPath());
                break;
            case CAMERA:
                mFilePathTv.setText(data.getData().getPath());
                break;
            case VIDEO:
                Uri videoUri = data.getData();
                mFilePathTv.setText(videoUri.getPath());
                break;
        }
    }

    private void upload(String fileName) {
        mProgress.setVisibility(View.VISIBLE);
        CheckCheckService service = RetrofitWrapper.getInstance().create(CheckCheckService.class);
        File file = new File(fileName);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("Filedata", file.getName(), requestFile);
        RequestBody description =
                RequestBody.create(MediaType.parse("multipart/form-data"), "检查管理");

        Call<UploadResponse> upload = service.upload(description, body);
        upload.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                mProgress.setVisibility(View.GONE);
                Toast.makeText(CheckAddUploadActivity.this, "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                Toast.makeText(CheckAddUploadActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mSelectPW != null && mSelectPW.isShowing()) {
            mSelectPW.dismiss();
        }
    }

    private void showPopupWindow() {
        if (mSelectPW == null) {
            initPopupWindow();
        }
        mSelectPW.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
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
