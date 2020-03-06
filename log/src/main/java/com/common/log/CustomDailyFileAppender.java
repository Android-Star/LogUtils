package com.common.log;

import android.annotation.SuppressLint;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class CustomDailyFileAppender extends FileAppender {
  private static final String TAG = "CustomDailyFileAppender";
  static final int TOP_OF_TROUBLE = -1;
  static final int TOP_OF_MINUTE = 0;
  static final int TOP_OF_HOUR = 1;
  static final int HALF_DAY = 2;
  static final int TOP_OF_DAY = 3;
  static final int TOP_OF_WEEK = 4;
  static final int TOP_OF_MONTH = 5;
  private String datePattern = "'.'yyyy-MM-dd";
  private String scheduledFilename;
  private int keepDays;
  private long nextCheck = System.currentTimeMillis() - 1L;
  Date now = new Date();
  SimpleDateFormat sdf;
  RollingCalendar rc = new RollingCalendar();
  static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

  public CustomDailyFileAppender() {
  }

  public CustomDailyFileAppender(Layout layout, String filename, String datePattern)
      throws IOException {
    super(layout, filename, true);
    this.datePattern = datePattern;
    this.activateOptions();
  }

  public void setDatePattern(String pattern) {
    this.datePattern = pattern;
  }

  public String getDatePattern() {
    return this.datePattern;
  }

  public void activateOptions() {
    super.activateOptions();
    if (this.datePattern != null && this.fileName != null) {

      this.now.setTime(System.currentTimeMillis());
      this.sdf = new SimpleDateFormat(this.datePattern);
      int type = this.computeCheckPeriod();
      this.printPeriodicity(type);
      this.rc.setType(type);
      File file = new File(this.fileName);
      this.scheduledFilename = this.fileName + this.sdf.format(new Date(file.lastModified()));
    } else {
      Log.e(TAG,
          "Either File or DatePattern options are not set for appender [" + this.name + "].");
    }
  }

  void printPeriodicity(int type) {
    switch (type) {
      case 0:
        Log.d(TAG, "Appender [" + this.name + "] to be rolled every minute.");
        break;
      case 1:
        Log.d(TAG, "Appender [" + this.name + "] to be rolled on top of every hour.");
        break;
      case 2:
        Log.d(TAG, "Appender [" + this.name + "] to be rolled at midday and midnight.");
        break;
      case 3:
        Log.d(TAG, "Appender [" + this.name + "] to be rolled at midnight.");
        break;
      case 4:
        Log.d(TAG, "Appender [" + this.name + "] to be rolled at start of week.");
        break;
      case 5:
        Log.d(TAG, "Appender [" + this.name + "] to be rolled at start of every month.");
        break;
      default:
        Log.w(TAG, "Unknown periodicity for appender [" + this.name + "].");
    }
  }

  int computeCheckPeriod() {
    RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
    Date epoch = new Date(0L);
    if (this.datePattern != null) {
      for (int i = 0; i <= 5; ++i) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.datePattern);
        simpleDateFormat.setTimeZone(gmtTimeZone);
        String r0 = simpleDateFormat.format(epoch);
        rollingCalendar.setType(i);
        Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
        String r1 = simpleDateFormat.format(next);
        if (r0 != null && r1 != null && !r0.equals(r1)) {
          return i;
        }
      }
    }

    return TOP_OF_TROUBLE;
  }

  void rollOver() throws IOException {

    /*delete  before keepdays file*/
    Log.d(TAG, "rollOver  -> rollOver");
    if (keepDays != 0) {
      String deleteFileName =
          fileName + sdf.format(new Date(now.getTime() - keepDays * 24 * 60 * 60 * 1000));
      File target = new File(deleteFileName);
      if (target.exists()) {
        target.delete();
        Log.d(TAG, "delete log file -> " + deleteFileName);
      }
    }

    if (this.datePattern == null) {
      this.errorHandler.error("Missing DatePattern option in rollOver().");
    } else {
      String datedFilename = this.fileName + this.sdf.format(this.now);
      if (!this.scheduledFilename.equals(datedFilename)) {
        this.closeFile();
        File target = new File(this.scheduledFilename);
        if (target.exists()) {
          target.delete();
        }

        File file = new File(this.fileName);
        boolean result = file.renameTo(target);
        if (result) {
          Log.d(TAG, this.fileName + " -> " + this.scheduledFilename);
        } else {
          Log.e(TAG,
              "Failed to rename [" + this.fileName + "] to [" + this.scheduledFilename + "].");
        }

        try {
          this.setFile(this.fileName, true, this.bufferedIO, this.bufferSize);
        } catch (IOException var6) {
          this.errorHandler.error("setFile(" + this.fileName + ", true) call failed.");
        }

        this.scheduledFilename = datedFilename;
      }
    }
  }

  protected void subAppend(LoggingEvent event) {
    long n = System.currentTimeMillis();
    if (n >= this.nextCheck) {
      this.now.setTime(n);
      this.nextCheck = this.rc.getNextCheckMillis(this.now);

      try {
        this.rollOver();
      } catch (IOException var5) {
        if (var5 instanceof InterruptedIOException) {
          Thread.currentThread().interrupt();
        }

        Log.e(TAG, "rollOver() failed.", var5);
      }
    }

    super.subAppend(event);
  }

  public int getKeepDays() {
    return keepDays;
  }

  public void setKeepDays(int keepDays) {
    this.keepDays = keepDays;
  }

  class RollingCalendar extends GregorianCalendar {
    private static final long serialVersionUID = -3560331770601814177L;
    int type = TOP_OF_TROUBLE;

    RollingCalendar() {
    }

    RollingCalendar(TimeZone tz, Locale locale) {
      super(tz, locale);
    }

    void setType(int type) {
      this.type = type;
    }

    public long getNextCheckMillis(Date now) {
      return this.getNextCheckDate(now).getTime();
    }

    @SuppressLint("WrongConstant") public Date getNextCheckDate(Date now) {
      this.setTime(now);
      switch (type) {
        case TOP_OF_MINUTE:
          this.set(Calendar.SECOND, 0);
          this.set(Calendar.MILLISECOND, 0);
          this.add(Calendar.MINUTE, 1);
          break;
        case TOP_OF_HOUR:
          this.set(Calendar.MINUTE, 0);
          this.set(Calendar.SECOND, 0);
          this.set(Calendar.MILLISECOND, 0);
          this.add(Calendar.HOUR_OF_DAY, 1);
          break;
        case HALF_DAY:
          this.set(Calendar.MINUTE, 0);
          this.set(Calendar.SECOND, 0);
          this.set(Calendar.MILLISECOND, 0);
          int hour = get(Calendar.HOUR_OF_DAY);
          if (hour < 12) {
            this.set(Calendar.HOUR_OF_DAY, 12);
          } else {
            this.set(Calendar.HOUR_OF_DAY, 0);
            this.add(Calendar.DAY_OF_MONTH, 1);
          }
          break;
        case TOP_OF_DAY:
          this.set(Calendar.HOUR_OF_DAY, 0);
          this.set(Calendar.MINUTE, 0);
          this.set(Calendar.SECOND, 0);
          this.set(Calendar.MILLISECOND, 0);
          this.add(Calendar.DATE, 1);
          break;
        case TOP_OF_WEEK:
          this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
          this.set(Calendar.HOUR_OF_DAY, 0);
          this.set(Calendar.MINUTE, 0);
          this.set(Calendar.SECOND, 0);
          this.set(Calendar.MILLISECOND, 0);
          this.add(Calendar.WEEK_OF_YEAR, 1);
          break;
        case TOP_OF_MONTH:
          this.set(Calendar.DATE, 1);
          this.set(Calendar.HOUR_OF_DAY, 0);
          this.set(Calendar.MINUTE, 0);
          this.set(Calendar.SECOND, 0);
          this.set(Calendar.MILLISECOND, 0);
          this.add(Calendar.MONTH, 1);
          break;
        default:
          throw new IllegalStateException("Unknown periodicity type.");
      }

      return this.getTime();
    }
  }
}
