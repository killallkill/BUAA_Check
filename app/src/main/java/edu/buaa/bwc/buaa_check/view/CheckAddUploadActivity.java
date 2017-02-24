package edu.buaa.bwc.buaa_check.view;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.io.File;

import edu.buaa.bwc.buaa_check.R;
import edu.buaa.bwc.buaa_check.Utils.FileUtils;

public class CheckAddUploadActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PHOTO_SELECT = 100;
    public static final int CAMERA = 200;
    public static final int VIDEO = 300;
    private PopupWindow mPopupWindow;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRootView = LayoutInflater.from(this).inflate(R.layout.activity_check_add_upload, null);
        setContentView(mRootView);
        setTitle("上传检查场景");
        findViewById(R.id.button).setOnClickListener(this);
    }

    private void initPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.check_select_dialog, null, false);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.findViewById(R.id.photo_select).setOnClickListener(this);
        view.findViewById(R.id.camera).setOnClickListener(this);
        view.findViewById(R.id.video).setOnClickListener(this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                return false;
            }
        });
    }

    private File file;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button:
                showPopupWindow();
                break;
            case R.id.photo_select:
                mPopupWindow.dismiss();
                intent = new Intent();
                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                }
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, PHOTO_SELECT);
                break;
            case R.id.camera:
                mPopupWindow.dismiss();
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = FileUtils.getImageFile();
                if (file == null) break;
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAMERA);
                break;
            case R.id.video:
                mPopupWindow.dismiss();
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
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

                break;
            case CAMERA:
                if (file != null) {
                    //TODO upload image
                }
                break;
            case VIDEO:
                Uri videoUri = data.getData();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void showPopupWindow() {
        if (mPopupWindow == null) {
            initPopupWindow();
        }
        mPopupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
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
