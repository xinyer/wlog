# wlog

write logcat message to files in sdcard, timing.

# How to use it

Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```
dependencies {
    compile 'com.github.User:Repo:Tag'
}
```

init WLog in your Application onCreate

```java
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogConfig config = new LogConfig.Builder()
                .intervalMillis(3600_000)
                .backupDays(15)
                .build();
        WLog.init(this, config);
    }
}
```

## default configs

- log file saved path - /sdcard/wlog/{your package name}/{date}.log
- intervalMillis - 3600000 ms
- backupDays - 15 days

## config

- intervalMillis - the interval of write logcat message to file, you can set this time according your application log quantity.
- backupDays - the days of log file backup.

and more configs in plan

- fileNamePattern
- fileSavedPath
