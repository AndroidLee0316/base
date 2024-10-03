package com.pasc.debug.component.libbase;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.pasc.lib.base.activity.BaseActivity;
import com.pasc.lib.base.permission.PermissionUtils;
import com.pasc.lib.base.util.NetworkUtils;
import com.pasc.lib.base.util.ToastUtils;
import com.pasc.lib.log.PascLog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.security.Permission;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override protected int layoutResId() {
        return R.layout.libbase_activity_main;
    }

    @Override protected void onInit(@Nullable Bundle savedInstanceState) {
        findViewById(R.id.tv_toast).setOnClickListener(this);
        findViewById(R.id.tv_connect).setOnClickListener(this);
        findViewById(R.id.tv_available).setOnClickListener(this);
        findViewById(R.id.tv_log).setOnClickListener(this);
        testLog();
        findViewById(R.id.tv_toast).postDelayed(new Runnable() {
            @Override public void run() {
                if(isFinishing()){
                    return;
                }
                changeFragment(R.id.fragment,TestFragment.getInstance(null),false);

            }
        },1000);
        PermissionUtils.requestWithDialog(this, Manifest.permission.CAMERA).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<Boolean>(){

            @Override public void accept(Boolean aBoolean) throws Exception {
              if(aBoolean){
                  ToastUtils.toastMsg("获取camera成功");
              }
            }
          });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_toast) {
            ToastUtils.toastMsg("ToastUtils工具类提示");
            PascLog.i(TAG, "ToastUtils工具类提示");
            PascLog.d(TAG, "ToastUtils工具类提示");
            PascLog.e(TAG, "ToastUtils工具类提示");
            PascLog.w(TAG, "ToastUtils工具类提示");
        } else if (view.getId() == R.id.tv_connect) {
            final String rlt = NetworkUtils.isNetworkConnected(this) ? "已连接" : "未连接";
            ToastUtils.toastMsg("网络" + rlt);

            PascLog.i(TAG, "网络测试 %s" + rlt);
            PascLog.d(TAG, "网络测试 %s" + rlt);
            PascLog.e(TAG, "网络测试 %s" + rlt);
            PascLog.w(TAG, "网络测试 %s" + rlt);
        } else if (view.getId() == R.id.tv_available) {
            ToastUtils.toastMsg("网络" + (NetworkUtils.isNetworkAvailable() ? "可用" : "不可用"));
        } else if (view.getId() == R.id.tv_log) {
            PascLog.e("pasc 日志打印");
        }
    }

    private void testLog() {
        PascLog.d(TAG, "testLog 01 test");
        PascLog.d(TAG, "testLog 02 %s", "test");
        Exception here = new Exception("here");
        here.fillInStackTrace();
        PascLog.d(TAG, "testLog 03 test", here);
    }
}
