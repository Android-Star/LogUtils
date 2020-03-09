package com.example.logutils;

import android.app.Application;
import android.os.Environment;
import com.common.log.DatePatternType;
import com.common.log.LogUtils;
import java.io.File;

public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
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
}
