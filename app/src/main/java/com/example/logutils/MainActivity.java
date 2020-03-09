package com.example.logutils;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.common.log.LogUtils;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClick(View view) {
    LogUtils.getLogger(TAG).debug("============================level debug");
    LogUtils.getLogger(TAG).error("========================level error");
    LogUtils.getLogger(TAG).info("=================================level info");
    LogUtils.getLogger(TAG).warn("================================level warn");
    LogUtils.getLogger(TAG).fatal("================================level fatal");
    LogUtils.getLogger(TAG).trace("================================level trace");

    LogUtils.d(TAG,"我是日志信息");
    LogUtils.w(TAG,"我是日志信息");
    LogUtils.e(TAG,"我是日志信息");
    LogUtils.i(TAG,"我是日志信息");
    LogUtils.f(TAG,"我是日志信息");
  }
}
