package com.pasc.lib.base;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pasc.lib.base.statistics.IPascStatistics;
import com.pasc.lib.base.user.IUserManager;
import com.pasc.lib.log.LogConfiguration;
import com.pasc.lib.log.PascLog;
import com.pasc.lib.log.printer.AndroidPrinter;
import com.pasc.lib.log.printer.Printer;
import com.pasc.lib.log.printer.file.FilePrinter;
import com.pasc.lib.log.printer.file.naming.DateFileNameGenerator;
import com.pasc.lib.log.utils.SDCardUtils;

import java.util.Map;

/*
 * Copyright (C) 2018 pasc Licensed under the Apache License, Version 2.0 (the "License");
 * @author chenshangyong872
 * @date 2018-06-25
 * @des 应用程序在底层LibBase处的代理，方便上层调用应用程序的基础数据：1、application 2、isDbug 3、Context
 *      另外，在这里会对LibBase的基础库功能进行初始化
 * @version V1.0
 * @modify On 2018-08-28 by author for reason ...
 */
public class AppProxy implements IPascStatistics {
    private static final String TAG = AppProxy.class.getSimpleName();

    // 注意这里的值,和主工程的渠道配置是对应的
    public static final int TYPE_PRODUCT_PRODUCT = 1;
    public static final int TYPE_PRODUCT_BETA = 2;

    private static String SDCARD_LOG_FILE_DIR = "Smart/log";//日志保存目录
    private static String DEFAULT_LOG_TAG = "smt";//日志tag
    private static String SYSTEM_ID = "wdsz";//日志搜集app系统标识

    private String versionName;
    private static Application mApplication;

    private static boolean sIsDebug = false;

    // 默认是beta（测试环境）
    private static int sProductType = TYPE_PRODUCT_BETA;

    private static String sHost = null;

    public static IPascStatistics sPascStatistics;
    private IUserManager iUserManager;
    private static String h5Host = null;

    public static AppProxy getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void onEvent(String eventID) {
        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onEvent(eventID);
    }

    @Override
    public void onEvent(String eventID, String label) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onEvent(eventID, label);

    }

    @Override
    public void onEvent(String eventID, Map<String, String> map) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onEvent(eventID, map);
    }

    @Override
    public void onEvent(String eventID, String label, Map<String, String> map) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onEvent(eventID, label, map);
    }

    @Override
    public void onPageStart(String pageName) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onPageStart(pageName);
    }

    @Override
    public void onPageEnd(String pageName) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onPageEnd(pageName);
    }

    @Override
    public void onResume(Context context) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onResume(context);
    }

    @Override
    public void onPause(Context context) {

        if (sPascStatistics == null) {
            Log.e(TAG, "PascStatistics is null, need set sPascStatistics first");
            return;
        }
        sPascStatistics.onPause(context);
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final AppProxy instance = new AppProxy();
    }

    public AppProxy init(Application application,
                         boolean productModel) {
        if (null == application) {
            throw new IllegalArgumentException("Illega application Exception, please check~ !");
        }

        AppProxy.mApplication = application;


        // PascLog
        initPascLog(productModel);


        PascLog.d(TAG, "debug? => " + sIsDebug);

        return this;
    }

    public AppProxy setUserManager(IUserManager userManager) {
        if (userManager == null) {
            throw new NullPointerException("IUserManager为空");
        }
        this.iUserManager = userManager;
        return this;
    }

    public IUserManager getUserManager() {
        if (iUserManager == null) {
            throw new NullPointerException("IUserManager为空");
        }
        return iUserManager;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionName() {
        return versionName;
    }

    public AppProxy setIsDebug(boolean isDebug) {
        sIsDebug = isDebug;
        return this;
    }

    public AppProxy setProductType(int productType) {
        sProductType = productType;

        return this;
    }

    public AppProxy setHost(String host) {
        sHost = host;

        return this;
    }
    public AppProxy setH5Host(String host) {
        h5Host = host;
        return this;
    }
    public AppProxy setPascStatistics(IPascStatistics sPascStatistics) {
        AppProxy.sPascStatistics = sPascStatistics;
        return this;
    }

    public Application getApplication() {
        if (null == mApplication) {
            throw new IllegalAccessError("Please initialize the AppProxy first.");
        }

        return mApplication;
    }

    public boolean isDebug() {
        return sIsDebug;
    }

    public Context getContext() {
        if (null == mApplication) {
            throw new IllegalAccessError("Please initialize the AppProxy first.");
        }

        return mApplication.getApplicationContext();
    }

    public int getProductType() {
        return sProductType;
    }


    public final String getHost() {
        if (TextUtils.isEmpty(sHost)) {
            throw new IllegalAccessError("Please call setHost to initialize the Host first.");
        }

        return sHost;
    }
    public final String getH5Host(){
        if(TextUtils.isEmpty(h5Host)){
            throw new IllegalAccessError("Please call setH5Host to initialize the h5Host first.");
        }
        return h5Host;
    }
    // 正式环境
    public boolean isProductionEvn() { //
        return TYPE_PRODUCT_PRODUCT == sProductType;
    }

    // 测试环境
    public boolean isBetaEvn() {
        return TYPE_PRODUCT_BETA == sProductType;
    }


    /**
     * 初始化PascLog
     */
    private void initPascLog(boolean productModel) {
        LogConfiguration config = new LogConfiguration.Builder()
                .tag(DEFAULT_LOG_TAG)                                  // 指定 TAG，默认为 "X-LOG"
                .threadInfoEnable()                                    // 允许打印线程信息，默认禁止
                .stackTraceEnable(2)                                   // 允许打印深度为2的调用栈信息，默认禁止
                .borderEnable()                                        // 允许打印日志边框，默认禁止
                .build();
        Printer printer;
        if (!productModel) {                                         //测试环境直接打印
            printer = new AndroidPrinter();                              // 通过 android.util.Log 打印日志的打印器
        } else {                                                        //正式环境文件保存
            String logFileDir = SDCardUtils.getAppDir(mApplication, SDCARD_LOG_FILE_DIR);
            printer = new FilePrinter                                    // 打印日志到文件的打印器
                    .Builder(logFileDir)                                 // 指定保存日志文件的路径
                    .fileNameGenerator(new DateFileNameGenerator())      // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                    .fileSaveTime(7)
                    .build();
        }
        PascLog.init(mApplication, SYSTEM_ID, config, printer);
    }
}
