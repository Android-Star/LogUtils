package com.example.logutils;

import android.app.Application;
import com.common.log.LogUtils;

public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    LogUtils.getInstance(this).init();
  }
}
