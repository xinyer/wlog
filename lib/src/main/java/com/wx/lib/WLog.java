package com.wx.lib;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import org.joda.time.LocalDate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Arrays;


public final class WLog {

    static LogConfig config;

    private static final String TAG = "WLog";
    private static final String FORMAT = "yyyy-MM-dd";
    private static final int MAX_LEN = 10000;

    private WLog() {

    }

    public static void init(Context context) {
        LogConfig config = new LogConfig.Builder()
                .intervalMillis(3600_000)
                .backupDays(15)
                .build();
        init(context, config);
    }

    public static void init(Context context, LogConfig config) {
        WLog.config = config;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long startTime = SystemClock.elapsedRealtime() + config.getIntervalMillis();
        Intent intent = new Intent(context, WLogService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, config.getIntervalMillis(), pendingIntent);
    }

    public static void writeLogCat(Context context) {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -v time");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }
            new ProcessBuilder().command("logcat", "-c").redirectErrorStream(true).start();
            File file = logFile(context);
            RandomAccessFile randomFile = new RandomAccessFile(file.getAbsolutePath(), "rw");
            while (log.length() > MAX_LEN) {
                long fileLength = randomFile.length();
                randomFile.seek(fileLength);
                String content = log.substring(0, MAX_LEN);
                log = log.delete(0, MAX_LEN);
                randomFile.writeUTF(content);
            }
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeUTF(log.toString());
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "write logcat IOE.", e);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e(TAG, "write logcat OOM.", e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Exception", e);
        }
    }

    private static File logFileDir(Context context) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + File.separator + "wlog"
                + File.separator + context.getApplicationContext().getPackageName());
        Log.d(TAG, "log dir:" + dir.getPath());
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            Log.d(TAG, "mkdirs:" + mkdirs);
        }
        return dir;
    }

    private static File logFile(Context context) {
        File dir = logFileDir(context);
        return new File(dir, LocalDate.now().toString(FORMAT) + ".log");
    }

    public static void processBackup(Context context) {
        File dir = logFileDir(context);
        File[] files = dir.listFiles();
        if (files != null && files.length > config.getBackupDays()) {
            Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
            Log.d(TAG, "delete file:" + files[0].getName());
            files[0].delete();
        }
    }

}

