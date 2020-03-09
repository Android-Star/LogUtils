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

  /**
   * 设置日志文件名（如果过期后将会被文件名+datePattern替换掉）
   */
  public LogUtils setFileName(String fileName) {
    if (!TextUtils.isEmpty(fileName)) {
      logConfigurator.setFileName(fileName);
    }
    return instance;
  }

  /**
   * 设置日志打印级别，分为：默认为DEBUG
   * OFF
   * FATAL
   * ERROR
   * WARN
   * INFO
   * DEBUG
   * TRACE
   * ALL
   */
  public LogUtils setRootLevel(Level level) {
    logConfigurator.setRootLevel(level);
    return instance;
  }

  /**
   * 设置日志打印的信息，配置方式：
   * 默认为："%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n"
   * （1）－X号: X信息输出时左对齐。
   * （2）%p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL。
   * （3）%d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日
   * 22：10：28，921。
   * （4）%r: 输出自应用启动到输出该log信息耗费的毫秒数。
   * （5）%c: 输出日志信息所属的类目，通常就是所在类的全名。
   * （6）%t: 输出产生该日志事件的线程名。
   * （7）%l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)。
   * （8）%x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。
   * （9）%%: 输出一个"%"字符。
   * （10）%F: 输出日志消息产生时所在的文件名称。
   * （11）%L: 输出代码中的行号。
   * （12）%m: 输出代码中指定的消息,产生的日志具体信息。
   * （13）%n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行。
   * 可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：
   * (1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。
   * (2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。
   * (3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。
   * (4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉。
   */
  public LogUtils setFilePattern(String filePattern) {
    logConfigurator.setFilePattern(filePattern);
    return instance;
  }

  /**
   * 设置logcat输出内容的格式，参数同setFilePattern
   */
  public LogUtils setLogCatPattern(String logCatPattern) {
    logConfigurator.setLogCatPattern(logCatPattern);
    return instance;
  }

  /**
   * 当设置setUseFileAppender为true时生效，设置保留最大文件数
   */
  public LogUtils setMaxBackupSize(int maxBackupSize) {
    logConfigurator.setMaxBackupSize(maxBackupSize);
    return instance;
  }

  /**
   * 当设置setUseFileAppender为true时生效，设置文件最大size
   */
  public LogUtils setMaxFileSize(long maxFileSize) {
    logConfigurator.setMaxFileSize(maxFileSize);
    return instance;
  }

  /**
   * 是否启用FileAppender（即根据文件大小和保留文件数输出日志到文件），与DailyFileAppender互斥
   */
  public LogUtils setUseFileAppender(boolean useFileAppender) {
    logConfigurator.setUseFileAppender(useFileAppender);
    return instance;
  }

  /**
   * 是否启用LogcatAppender，默认启用，如果关闭，logcat将不打印日志
   */
  public LogUtils setUseLogCatAppender(boolean useLogCatAppender) {
    logConfigurator.setUseLogCatAppender(useLogCatAppender);
    return instance;
  }

  /**
   * 是否启用DailyFileAppender（即按天生成日志文件，与setKeepDays配合可以设置保留多少天的日志）与FileAppender互斥
   */
  public LogUtils setUseDailyFileAppender(boolean useDailyFileAppender) {
    logConfigurator.setUseDailyFileAppender(useDailyFileAppender);
    return instance;
  }

  /**
   * 是否重置log4j的配置，使新配置生效
   */
  public LogUtils setResetConfiguration(boolean resetConfiguration) {
    logConfigurator.setResetConfiguration(resetConfiguration);
    return instance;
  }

  /**
   * setUseDailyFileAppender为true时生效，设置保留多少天内或多少分钟内等的日志
   */
  public LogUtils setKeepDays(int keepDays) {
    logConfigurator.setKeepDays(keepDays);
    return instance;
  }

  /**
   * setUseDailyFileAppender为true时生效，设置生成文件的规则
   * 默认：TOP_OF_DAY
   * TOP_OF_TROUBLE,     //默认值
   * TOP_OF_MINUTE,      //每分钟
   * TOP_OF_HOUR,        //每小时
   * HALF_DAY,           //每半天
   * TOP_OF_DAY,         //每天
   * TOP_OF_WEEK,        //每周
   * TOP_OF_MONTH        //每月
   */
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
