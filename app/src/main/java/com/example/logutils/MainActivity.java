package com.example.logutils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.common.log.Log4jConfigure;
import com.common.log.LogUtils;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log4jConfigure.configure();
    LogUtils.getLogger(TAG).debug("============================level debug");
    LogUtils.getLogger(TAG).error("========================level error");
    LogUtils.getLogger(TAG).info("=================================level info");
    LogUtils.getLogger(TAG).warn("================================level warn");
    LogUtils.getLogger(TAG).fatal("================================level fatal");
    LogUtils.getLogger(TAG).trace("================================level trace");
  }
}
