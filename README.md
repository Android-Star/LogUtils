# LogUtils
## 引用：
`
implementation 'com.common.log:LogUtils:1.0.3'
`
## 初始化：
**1.默认配置**<br/>
`
LogUtils.getInstance(this).init();
`<br/>
默认配置包括：

- fileName:log_

- 路径：sdcard根目录+包名（sd卡不可用：data/data/包名/files）

- 默认会输出日志到logcat

- 默认使用按天生成日志规则，保留七天的日志信息

- 默认打印的日志level：Level.DEBUG

- 默认输出的日志信息："%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n"（日期+等级+线程+日志发生位置+信息）

**示例：<br/>2020-03-09 10:50:10 WARN main com.example.logutils.MainActivity.onClick(MainActivity.java:21) ================================level warn**<br/>

**2.自定义配置：**
```
LogUtils.getInstance(this)
        .setUseLogCatAppender(true)
        .setLogCatPattern("%m%n")
        .setFileName(Environment.getExternalStorageDirectory() + File.separator + getPackageName()
            + File.separator + "log_")
        .setUseFileAppender(false)
        .setMaxFileSize(524288L)
        .setMaxBackupSize(5)
        .setUseDailyFileAppender(true)
        .setKeepDays(7)
        .setDatePatternType(DatePatternType.TOP_OF_DAY)
        .setFilePattern("%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n")
        .init();
```
**方法含义：**

1. setFileName：设置日志文件名（全路径，如果过期后将会被文件名+datePattern替换掉）

2. setRootLevel：设置日志打印级别，分为：默认为DEBUG，共有OFF，FATAL，ERROR，WARN，INFO，DEBUG，TRACE，ALL

3. setFilePattern：设置日志打印的信息，默认为："%d{yyy-MM-dd HH:mm:ss} %p %t %l %m%n"，配置规则如下

（1）－X号: X信息输出时左对齐。<br/>
（2）%p: 输出日志信息优先级，即DEBUG，INFO，WARN，ERROR，FATAL。<br/>
（3）%d: 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921。<br/>
（4）%r: 输出自应用启动到输出该log信息耗费的毫秒数。<br/>
（5）%c: 输出日志信息所属的类目，通常就是所在类的全名。<br/>
（6）%t: 输出产生该日志事件的线程名。<br/>
（7）%l: 输出日志事件的发生位置，相当于%C.%M(%F:%L)的组合,包括类目名、发生的线程，以及在代码中的行数。举例：Testlog4.main(TestLog4.java:10)。<br/>
（8）%x: 输出和当前线程相关联的NDC(嵌套诊断环境),尤其用到像java servlets这样的多客户多线程的应用中。<br/>
（9）%%: 输出一个"%"字符。<br/>
（10）%F: 输出日志消息产生时所在的文件名称。<br/>
（11）%L: 输出代码中的行号。<br/>
（12）%m: 输出代码中指定的消息,产生的日志具体信息。<br/>
（13）%n: 输出一个回车换行符，Windows平台为"\r\n"，Unix平台为"\n"输出日志信息换行。<br/>
可以在%与模式字符之间加上修饰符来控制其最小宽度、最大宽度、和文本的对齐方式。如：<br/>
(1)%20c：指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，默认的情况下右对齐。<br/>
(2)%-20c:指定输出category的名称，最小的宽度是20，如果category的名称小于20的话，"-"号指定左对齐。<br/>
(3)%.30c:指定输出category的名称，最大的宽度是30，如果category的名称大于30的话，就会将左边多出的字符截掉，但小于30的话也不会有空格。<br/>
(4)%20.30c:如果category的名称小于20就补空格，并且右对齐，如果其名称长于30字符，就从左边交远销出的字符截掉。<br/>

4. setUseFileAppender：是否启用FileAppender（即根据文件大小和保留文件数输出日志到文件），与DailyFileAppender互斥

5. setLogCatPattern：设置logcat输出内容的格式，参数同setFilePattern

6. setMaxBackupSize：当设置setUseFileAppender为true时生效，设置保留最大文件数

7. setMaxFileSize：当设置setUseFileAppender为true时生效，设置文件最大size

8. setUseLogCatAppender：是否启用LogcatAppender，默认启用，如果关闭，logcat将不打印日志

9. setUseDailyFileAppender：是否启用DailyFileAppender（即按天生成日志文件，与setKeepDays配合可以设置保留多少天的日志）与FileAppender互斥

10. setKeepDays：setUseDailyFileAppender为true时生效，设置保留多少天内或多少分钟内等的日志

11. setDatePatternType：setUseDailyFileAppender为true时生效，设置生成文件的规则，该方法是对log4j中的datePattern参数做了封装，包含：TOP_OF_TROUBLE, //默认值 TOP_OF_MINUTE, //每分钟 TOP_OF_HOUR, //每小时 HALF_DAY, //每半天 TOP_OF_DAY, //每天 TOP_OF_WEEK, //每周 TOP_OF_MONTH //每月，分别对应：
```
public void setDatePatternType(DatePatternType datePatternType) {
    this.datePatternType = datePatternType;
    switch (datePatternType) {
      case TOP_OF_TROUBLE:
        datePattern = "";
        break;
      case TOP_OF_MINUTE:
        datePattern = "yyyy-MM-dd-HH-mm'.txt'";
        break;
      case TOP_OF_HOUR:
        datePattern = "yyyy-MM-dd-HH'.txt'";
        break;
      case HALF_DAY:
        datePattern = "yyyy-MM-dd-a'.txt'";
        break;
      case TOP_OF_DAY:
        datePattern = "yyyy-MM-dd'.txt'";
        break;
      case TOP_OF_WEEK:
        datePattern = "yyyy-ww'.txt'";
        break;
      case TOP_OF_MONTH:
        datePattern = "yyyy-MM'.txt'";
        break;
      default:
        datePattern = "";
    }
  }
```
##使用：
```
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
```
**文件中效果如下：**<br/>
2020-03-09 11:34:57 DEBUG main com.example.logutils.MainActivity.onClick(MainActivity.java:18) ============================level debug<br/>
2020-03-09 11:34:57 ERROR main com.example.logutils.MainActivity.onClick(MainActivity.java:19) ========================level error<br/>
2020-03-09 11:34:57 INFO main com.example.logutils.MainActivity.onClick(MainActivity.java:20) =================================level info<br/>
2020-03-09 11:34:57 WARN main com.example.logutils.MainActivity.onClick(MainActivity.java:21) ================================level warn<br/>
2020-03-09 11:34:57 FATAL main com.example.logutils.MainActivity.onClick(MainActivity.java:22) ================================level fatal<br/>
2020-03-09 11:34:57 DEBUG main com.common.log.LogUtils.d(LogUtils.java:203) 我是日志信息<br/>
2020-03-09 11:34:57 WARN main com.common.log.LogUtils.w(LogUtils.java:231) 我是日志信息<br/>
2020-03-09 11:34:57 ERROR main com.common.log.LogUtils.e(LogUtils.java:245) 我是日志信息<br/>
2020-03-09 11:34:57 INFO main com.common.log.LogUtils.i(LogUtils.java:217) 我是日志信息<br/>
2020-03-09 11:34:57 FATAL main com.common.log.LogUtils.f(LogUtils.java:259) 我是日志信息<br/>
#### 可以看到两种方式输出的日志区别在于打印日志的位置信息不同，因为logutils是对log4j原生的Logger对象做了封装，所以如果需要打印位置的信息应该使用获取Logger对象的方式打印日志。
