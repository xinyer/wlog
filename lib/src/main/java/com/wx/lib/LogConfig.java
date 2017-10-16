package com.wx.lib;


public class LogConfig {

    private long intervalMillis = 3600_000;
    private int backupDays = 15;

    public LogConfig(Builder builder) {
        this.intervalMillis = builder.intervalMillis;
        this.backupDays = builder.backupDays;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    public int getBackupDays() {
        return backupDays;
    }

    public static class Builder {

        private long intervalMillis;
        private int backupDays;

        public Builder intervalMillis(long interval) {
            this.intervalMillis = interval;
            return this;
        }

        public Builder backupDays(int backupDays) {
            this.backupDays = backupDays;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(this);
        }
    }


}
