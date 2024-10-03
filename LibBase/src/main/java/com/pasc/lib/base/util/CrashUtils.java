package com.pasc.lib.base.util;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresPermission;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.log.PascLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * 异常捕获  by zc 2018年05月07日15:24:15
 */
public final class CrashUtils {

    private static String defaultDir;
    private static String versionName;
    private static int versionCode;

    private static final String FILE_SEP = System.getProperty("file.separator");
    @SuppressLint("SimpleDateFormat")
    private static final Format FORMAT =
            new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");

    private static final Thread.UncaughtExceptionHandler DEFAULT_UNCAUGHT_EXCEPTION_HANDLER;
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_EXCEPTION_HANDLER;

    private static OnCrashListener sOnCrashListener;

    static {
        try {
            PackageInfo pi = AppProxy.getInstance().getApplication()
                    .getPackageManager()
                    .getPackageInfo(AppProxy.getInstance().getApplication().getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER = Thread.getDefaultUncaughtExceptionHandler();

        UNCAUGHT_EXCEPTION_HANDLER = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                if (e == null) {
                    if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                        DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, null);
                    } else {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                    return;
                }

                final String time = FORMAT.format(new Date(System.currentTimeMillis()));
                final StringBuilder sb = new StringBuilder();
                final String head = "************* Log Head ****************"
                        + "\nTime Of Crash      : "
                        + time
                        + "\nDevice Manufacturer: "
                        + Build.MANUFACTURER
                        + "\nDevice Model       : "
                        + Build.MODEL
                        + "\nAndroid Version    : "
                        + Build.VERSION.RELEASE
                        + "\nAndroid SDK        : "
                        + Build.VERSION.SDK_INT
                        + "\nApp VersionName    : "
                        + versionName
                        + "\nApp VersionCode    : "
                        + versionCode
                        + "\n************* Log Head ****************\n\n";
                sb.append(head);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                Throwable cause = e.getCause();
                while (cause != null) {
                    cause.printStackTrace(pw);
                    cause = cause.getCause();
                }
                pw.flush();
                sb.append(sw.toString());
                final String crashInfo = sb.toString();
                final String fullPath = defaultDir + time + ".txt";
                if (createOrExistsFile(fullPath)) {
                    input2File(crashInfo, fullPath);
                } else {
                    PascLog.e("CrashUtils", "create " + fullPath + " failed!");
                }

                if (sOnCrashListener != null) {
                    sOnCrashListener.onCrash(crashInfo, e);
                }

                if (DEFAULT_UNCAUGHT_EXCEPTION_HANDLER != null) {
                    DEFAULT_UNCAUGHT_EXCEPTION_HANDLER.uncaughtException(t, e);
                }
            }
        };
    }

    private CrashUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Initialization
     * <p>Must hold
     * {@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />}</p>
     *
     * @param onCrashListener The crash listener.
     */
    @RequiresPermission(WRITE_EXTERNAL_STORAGE)
    public static void init(
            final OnCrashListener onCrashListener) {
        defaultDir = getDefaultCrashDir();
        sOnCrashListener = onCrashListener;
        Thread.setDefaultUncaughtExceptionHandler(UNCAUGHT_EXCEPTION_HANDLER);
    }

    /**
     * get default crash log dir
     *
     * @return dir
     */
    public static String getDefaultCrashDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && AppProxy.getInstance().getApplication().getExternalCacheDir() != null) {
            return AppProxy.getInstance().getApplication().getExternalCacheDir()
                    + FILE_SEP
                    + "crash_log"
                    + FILE_SEP;
        } else {
            return AppProxy.getInstance().getApplication().getCacheDir()
                    + FILE_SEP
                    + "crash_log"
                    + FILE_SEP;
        }
    }

    private static void input2File(final String input, final String filePath) {
        Future<Boolean> submit =
                Executors.newSingleThreadExecutor().submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        BufferedWriter bw = null;
                        try {
                            bw = new BufferedWriter(new FileWriter(filePath, true));
                            bw.write(input);
                            onClearFile();
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        } finally {
                            try {
                                if (bw != null) {
                                    bw.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        try {
            if (submit.get()) return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        PascLog.e("CrashUtils", "write crash info to " + filePath + " failed!");
    }

    private static boolean createOrExistsFile(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }


    public interface OnCrashListener {
        void onCrash(String crashInfo, Throwable e);
    }

    /**
     * 删除超过2天的文件，防止过多
     */
    private static void onClearFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(getDefaultCrashDir());
            if (file.exists()) {
                if (file.listFiles().length > 3) {
                    int index = 0;
                    for (File temp : file.listFiles()) {
                        long time = System.currentTimeMillis() - temp.lastModified();
                        if (time / 1000 > 3600 * 24 * 2) {
                            temp.delete();
                            index++;
                        }
                        if (index >= file.listFiles().length - 3) break;
                    }
                }
            }
        }
    }
}
