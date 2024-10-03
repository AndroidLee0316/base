package com.pasc.debug.component.libbase;

import android.app.Application;
import android.content.Context;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.AppUtils;

public class TheApplication extends Application {

    private static Context applicationContext;

    // 这个配置后面统一放BusinessBase里面[注意这里后面是有反斜杠的]
    private static String PRODUCT_HOST = "http://ntgsc-smt.pingan.com.cn/";
    private static String BETA_HOST = "http://smt-app-stg.pingan.com.cn/";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        if (AppUtils.getPIDName(this).equals(getPackageName())) {//主进程
            // for libbase 【尽量在其他module之前init】
            AppProxy.getInstance().init(this, false)
                    .setIsDebug(BuildConfig.DEBUG)
                    .setProductType(BuildConfig.PRODUCT_FLAVORS_TYPE)
                    .setHost(AppProxy.TYPE_PRODUCT_PRODUCT == BuildConfig.PRODUCT_FLAVORS_TYPE ? PRODUCT_HOST : BETA_HOST)
                    .setVersionName(BuildConfig.VERSION_NAME);
        }
    }
}
