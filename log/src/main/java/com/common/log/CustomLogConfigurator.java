package com.common.log;

import de.mindpipe.android.logging.log4j.LogCatAppender;
import java.io.IOException;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

public class CustomLogConfigurator {
  private Level rootLevel = Level.DEBUG;
  private String filePattern = "%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n";
  private String logCatPattern = "%m%n";
  private String fileName = "android-log4j.log";
  private int maxBackupSize = 5;
  private long maxFileSize = 524288L;
  private boolean immediateFlush = true;
  private boolean useLogCatAppender = true;
  private boolean useFileAppender;
  private boolean useDailyFileAppender;
  private boolean resetConfiguration = false;
  private boolean internalDebugging = false;
  private int keepDays;

  public CustomLogConfigurator() {
  }

  public CustomLogConfigurator(String fileName) {
    this.fileName = fileName;
  }

  public CustomLogConfigurator(String fileName, Level rootLevel) {
    this(fileName);
    this.setRootLevel(rootLevel);
  }

  public CustomLogConfigurator(String fileName, Level rootLevel, String filePattern) {
    this(fileName);
    this.setRootLevel(rootLevel);
    this.setFilePattern(filePattern);
  }

  public CustomLogConfigurator(String fileName, int maxBackupSize, long maxFileSize,
      String filePattern, Level rootLevel) {
    this(fileName, rootLevel, filePattern);
    this.setMaxBackupSize(maxBackupSize);
    this.setMaxFileSize(maxFileSize);
  }

  public void configure() {
    Logger root = Logger.getRootLogger();
    if (this.isResetConfiguration()) {
      LogManager.getLoggerRepository().resetConfiguration();
    }

    LogLog.setInternalDebugging(this.isInternalDebugging());
    if (this.isUseFileAppender()) {
      this.configureFileAppender();
    }

    if (this.isUseLogCatAppender()) {
      this.configureLogCatAppender();
    }

    if (this.isUseDailyFileAppender()) {
      this.configureDailyFileAppender();
    }

    root.setLevel(this.getRootLevel());
  }

  public void setLevel(String loggerName, Level level) {
    Logger.getLogger(loggerName).setLevel(level);
  }

  private void configureFileAppender() {
    Logger root = Logger.getRootLogger();
    PatternLayout fileLayout = new PatternLayout(this.getFilePattern());

    RollingFileAppender rollingFileAppender;
    try {
      rollingFileAppender = new RollingFileAppender(fileLayout, this.getFileName());
    } catch (IOException var5) {
      throw new RuntimeException("Exception configuring log system", var5);
    }

    rollingFileAppender.setMaxBackupIndex(this.getMaxBackupSize());
    rollingFileAppender.setMaximumFileSize(this.getMaxFileSize());
    rollingFileAppender.setImmediateFlush(this.isImmediateFlush());
    root.addAppender(rollingFileAppender);
  }

  private void configureLogCatAppender() {
    Logger root = Logger.getRootLogger();
    Layout logCatLayout = new PatternLayout(this.getLogCatPattern());
    LogCatAppender logCatAppender = new LogCatAppender(logCatLayout);
    root.addAppender(logCatAppender);
  }

  private void configureDailyFileAppender() {
    Logger root = Logger.getRootLogger();
    PatternLayout fileLayout = new PatternLayout(this.getFilePattern());
    CustomDailyFileAppender dailyAppender = null;
    try {
      dailyAppender =
          new CustomDailyFileAppender(fileLayout, this.getFileName(), "yyyy-MM-dd'.log'");
    } catch (IOException e) {
      e.printStackTrace();
    }
    dailyAppender.setKeepDays(this.getKeepDays());

    root.addAppender(dailyAppender);
  }

  public Level getRootLevel() {
    return this.rootLevel;
  }

  public void setRootLevel(Level level) {
    this.rootLevel = level;
  }

  public String getFilePattern() {
    return this.filePattern;
  }

  public void setFilePattern(String filePattern) {
    this.filePattern = filePattern;
  }

  public String getLogCatPattern() {
    return this.logCatPattern;
  }

  public void setLogCatPattern(String logCatPattern) {
    this.logCatPattern = logCatPattern;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getMaxBackupSize() {
    return this.maxBackupSize;
  }

  public void setMaxBackupSize(int maxBackupSize) {
    this.maxBackupSize = maxBackupSize;
  }

  public long getMaxFileSize() {
    return this.maxFileSize;
  }

  public void setMaxFileSize(long maxFileSize) {
    this.maxFileSize = maxFileSize;
  }

  public boolean isImmediateFlush() {
    return this.immediateFlush;
  }

  public void setImmediateFlush(boolean immediateFlush) {
    this.immediateFlush = immediateFlush;
  }

  public boolean isUseFileAppender() {
    return this.useFileAppender;
  }

  public void setUseFileAppender(boolean useFileAppender) {
    this.useFileAppender = useFileAppender;
  }

  public boolean isUseLogCatAppender() {
    return this.useLogCatAppender;
  }

  public void setUseLogCatAppender(boolean useLogCatAppender) {
    this.useLogCatAppender = useLogCatAppender;
  }

  public boolean isUseDailyFileAppender() {
    return useDailyFileAppender;
  }

  public void setUseDailyFileAppender(boolean useDailyFileAppender) {
    this.useDailyFileAppender = useDailyFileAppender;
  }

  public void setResetConfiguration(boolean resetConfiguration) {
    this.resetConfiguration = resetConfiguration;
  }

  public boolean isResetConfiguration() {
    return this.resetConfiguration;
  }

  public void setInternalDebugging(boolean internalDebugging) {
    this.internalDebugging = internalDebugging;
  }

  public boolean isInternalDebugging() {
    return this.internalDebugging;
  }

  public int getKeepDays() {
    return keepDays;
  }

  public void setKeepDays(int keepDays) {
    this.keepDays = keepDays;
  }
}
