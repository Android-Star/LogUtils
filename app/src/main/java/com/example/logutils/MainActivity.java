package com.example.logutils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.common.log.DatePatternType;
import com.common.log.LogUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 0x123;
    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        } else {
            init();
        }
    }

    public void init() {
        //LogUtils.getInstance(this).init();
        LogUtils.getInstance(this)
                .setUseLogCatAppender(true)
                .setLogCatPattern("%m%n")
                .setFileName(Environment.getExternalStorageDirectory() + File.separator + getPackageName()
                        + File.separator + "log_")
                //.setUseFileAppender(false)
                //.setMaxFileSize(524288L)
                //.setMaxBackupSize(5)
                .setUseDailyFileAppender(true)
                .setKeepDays(7)
                .setDatePatternType(DatePatternType.TOP_OF_DAY)
                .setFilePattern("%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n")
                .init();

    }

    public void onClick(View view) {
        LogUtils.getLogger(TAG).debug("============================level debug");
        LogUtils.getLogger(TAG).error("========================level error");
        LogUtils.getLogger(TAG).info("=================================level info");
        LogUtils.getLogger(TAG).warn("================================level warn");
        LogUtils.getLogger(TAG).fatal("================================level fatal");
        LogUtils.getLogger(TAG).trace("================================level trace");

        LogUtils.d(TAG, "我是日志信息");
        LogUtils.w(TAG, "我是日志信息");
        LogUtils.e(TAG, "我是日志信息");
        LogUtils.i(TAG, "我是日志信息");
        LogUtils.f(TAG, "我是日志信息");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
