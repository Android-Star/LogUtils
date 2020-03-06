package com.common.log;

import android.os.Environment;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Level;

public class Log4jConfigure {
  private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;
  private static final String DEFAULT_LOG_DIR = "//MyApp//Log//";
  private static final String DEFAULT_LOG_FILE_NAME = "log";
  private static final String TAG = "Log4jConfigure";
  // 对应AndroidManifest文件中的package
  private static final String PACKAGE_NAME = "com.king.logtest";

  public static void configure(String fileName) {
    final CustomLogConfigurator logConfigurator = new CustomLogConfigurator();
    try {
      if (isSdcardMounted()) {
        logConfigurator.setFileName(
            Environment.getExternalStorageDirectory() + DEFAULT_LOG_DIR + fileName);
      } else {
        logConfigurator.setFileName(
            "//data//data//" + PACKAGE_NAME + "//files" + File.separator + fileName);
      }
      //以下设置是按指定大小来生成新的文件
      //logConfigurator.setUseFileAppender(true);
      //logConfigurator.setMaxBackupSize(4);
      //logConfigurator.setMaxFileSize(MAX_FILE_SIZE);

      //以下设置是按天生成新的日志文件,与以上两句互斥,MAX_FILE_SIZE将不在起作用
      //文件名形如 MyApp.log.2016-06-02,MyApp.log.2016-06-03
      logConfigurator.setUseDailyFileAppender(true);
      logConfigurator.setKeepDays(1);

      //以下为通用配置
      logConfigurator.setImmediateFlush(true);
      logConfigurator.setRootLevel(Level.DEBUG);
      //logConfigurator.setFilePattern("%d\t%p/%c:\t%m%n");
      logConfigurator.setResetConfiguration(true);
      logConfigurator.configure();
      android.util.Log.e(TAG, "Log4j config finish");
    } catch (Throwable throwable) {
      logConfigurator.setResetConfiguration(true);
      android.util.Log.e(TAG, "Log4j config error, use default config. Error:" + throwable);
    }
  }

  public static void configure() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'.log'");
    //configure(sdf.format(new Date()));
    configure("_");
  }

  private static boolean isSdcardMounted() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }
}
