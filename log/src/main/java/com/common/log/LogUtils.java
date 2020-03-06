package com.common.log;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LogUtils {
  private static final String DEFAULT_FILE_NAME = "log_";
  private static final String DEFAULT_FILE_PATTERN = "%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n";
  private static LogUtils instance;

  /** log开关 */
  private static boolean SWITCH_LOG = true;
  private CustomLogConfigurator logConfigurator;
  private Context context;

  public static LogUtils getInstance(Context context) {
    if (instance == null) {
      synchronized (LogUtils.class) {
        if (instance == null) {
          instance = new LogUtils(context);
        }
      }
    }
    return instance;
  }

  public LogUtils setFileName(String fileName) {
    if (!TextUtils.isEmpty(fileName)) {
      logConfigurator.setFileName(fileName);
    }
    return instance;
  }

  public LogUtils setRootLevel(Level level) {
    logConfigurator.setRootLevel(level);
    return instance;
  }

  public LogUtils setFilePattern(String filePattern) {
    logConfigurator.setFilePattern(filePattern);
    return instance;
  }

  public LogUtils setLogCatPattern(String logCatPattern) {
    logConfigurator.setLogCatPattern(logCatPattern);
    return instance;
  }

  public LogUtils setMaxBackupSize(int maxBackupSize) {
    logConfigurator.setMaxBackupSize(maxBackupSize);
    return instance;
  }

  public LogUtils setMaxFileSize(long maxFileSize) {
    logConfigurator.setMaxFileSize(maxFileSize);
    return instance;
  }

  public LogUtils setUseFileAppender(boolean useFileAppender) {
    logConfigurator.setUseFileAppender(useFileAppender);
    return instance;
  }

  public LogUtils setUseLogCatAppender(boolean useLogCatAppender) {
    logConfigurator.setUseLogCatAppender(useLogCatAppender);
    return instance;
  }

  public LogUtils setUseDailyFileAppender(boolean useDailyFileAppender) {
    logConfigurator.setUseDailyFileAppender(useDailyFileAppender);
    return instance;
  }

  public LogUtils setResetConfiguration(boolean resetConfiguration) {
    logConfigurator.setResetConfiguration(resetConfiguration);
    return instance;
  }

  public LogUtils setKeepDays(int keepDays) {
    logConfigurator.setKeepDays(keepDays);
    return instance;
  }

  public LogUtils setDatePatternType(DatePatternType datePatternType) {
    logConfigurator.setDatePatternType(datePatternType);
    return instance;
  }

  public void init() {
    logConfigurator.setResetConfiguration(true);
    logConfigurator.configure();
  }

  private LogUtils(Context context) {
    this.context = context;
    logConfigurator = new CustomLogConfigurator();
    if (isSdcardMounted()) {
      logConfigurator.setFileName(
          Environment.getExternalStorageDirectory() + File.separator + context.getPackageName()
              + File.separator + DEFAULT_FILE_NAME);
    } else {
      logConfigurator.setFileName(
          "//data//data//" + context.getPackageName() + "//files" + File.separator
              + DEFAULT_FILE_NAME);
    }
    logConfigurator.setUseLogCatAppender(true);
    logConfigurator.setUseDailyFileAppender(true);
    logConfigurator.setKeepDays(7);
    logConfigurator.setDatePatternType(DatePatternType.TOP_OF_DAY);
    //以下设置是按指定大小来生成新的文件
    //logConfigurator.setUseFileAppender(true);
    //logConfigurator.setMaxBackupSize(4);
    //logConfigurator.setMaxFileSize(MAX_FILE_SIZE);
    //以下为通用配置
    logConfigurator.setImmediateFlush(true);
    logConfigurator.setRootLevel(Level.DEBUG);
    logConfigurator.setFilePattern(DEFAULT_FILE_PATTERN);
  }

  public static void switchLog(boolean switchLog) {
    SWITCH_LOG = switchLog;
  }

  public static void d(String tag, String message) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.debug(message);
    }
  }

  public static void d(String tag, String message, Throwable exception) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.debug(message, exception);
    }
  }

  public static void i(String tag, String message) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.info(message);
    }
  }

  public static void i(String tag, String message, Throwable exception) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.info(message, exception);
    }
  }

  public static void w(String tag, String message) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.warn(message);
    }
  }

  public static void w(String tag, String message, Throwable exception) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.warn(message, exception);
    }
  }

  public static void e(String tag, String message) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.error(message);
    }
  }

  public static void e(String tag, String message, Throwable exception) {
    if (SWITCH_LOG) {
      Logger LOGGER = getLogger(tag);
      LOGGER.error(message, exception);
    }
  }

  public static Logger getLogger(String tag) {
    Logger logger;
    if (TextUtils.isEmpty(tag)) {
      logger = Logger.getRootLogger();
    } else {
      logger = Logger.getLogger(tag);
    }
    return logger;
  }

  private boolean isSdcardMounted() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }
}
