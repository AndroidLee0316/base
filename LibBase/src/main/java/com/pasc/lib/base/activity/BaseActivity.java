package com.pasc.lib.base.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import com.pasc.lib.base.activity.presenter.IBaseView;
import com.pasc.lib.base.activity.presenter.IPresenter;
import com.pasc.lib.base.activity.presenter.PresenterWrapper;
import com.pasc.lib.base.util.ActivityManager;
import com.pasc.lib.base.util.InputMethodUtils;
import com.pasc.lib.base.widget.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends RxAppCompatActivity {
  protected final String TAG = this.getClass().getSimpleName();
  @SuppressWarnings("unchecked") protected final PresenterWrapper presenterWrapper =
      new PresenterWrapper();
  private Dialog mLoadingDialog;
  final private List<Dialog> dialogs = new ArrayList<>();
  private ActivityManager activityManager;

  @CallSuper @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();
    onInit(savedInstanceState);
    if (activityManager != null) {
      activityManager.add(this);
    }
  }

  private void init() {
    if (layoutResId() != 0) {
      setContentView(layoutResId());
    }
    activityManager = ActivityManager.get();
  }

  /**
   * 返回页面布局
   *
   * @return layout id
   */
  protected abstract @LayoutRes int layoutResId();

  /**
   * activity onCreate中执行的方法，不需要设置layout
   */
  protected abstract void onInit(@Nullable Bundle savedInstanceState);

  @Override public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
  }

  public void actionStart(Class<? extends Activity> activity) {
    Intent intent = new Intent(this, activity);
    startActivity(intent);
  }

  public void actionStart(Class<? extends Activity> activity, Bundle extras) {
    Intent intent = new Intent(this, activity);
    intent.putExtras(extras);
    startActivity(intent);
  }

  public void actionStartForResult(Class<? extends Activity> activity, Bundle extras, int code) {
    Intent intent = new Intent(this, activity);
    intent.putExtras(extras);
    startActivityForResult(intent, code);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {

    //if (ev.getAction() == MotionEvent.ACTION_DOWN) {
    //  //if (Math.abs(lastX)>0&&Math.abs(lastY)>0){
    //  //  if (ev.getX(ev.getActionIndex())-lastX>50&&ev.getY(ev.getActionIndex())-lastY>50){
    //  //    return super.dispatchTouchEvent(ev);
    //  //  }
    //  //}
    //  lastX = ev.getX(ev.getActionIndex());
    //  lastY = ev.getY(ev.getActionIndex());
    //  if (isTouchView(filterViewByIds(), ev)) return super.dispatchTouchEvent(ev);
    //  if (hideSoftByEditViewIds() == null || hideSoftByEditViewIds().length == 0) {
    //    return super.dispatchTouchEvent(ev);
    //  }
    //  View v = getCurrentFocus();
    //  if (isFocusEditText(v, hideSoftByEditViewIds())) {
    //    if (isTouchView(hideSoftByEditViewIds(), ev)) return super.dispatchTouchEvent(ev);
    //    //隐藏键盘
    //    InputMethodUtils.hideInputMethod(this);
    //    if (isCleanFocus()) {
    //      clearViewFocus(v, hideSoftByEditViewIds());
    //    }
    //  }
    //} else if (ev.getAction() == MotionEvent.ACTION_UP) {
    //  double dx = ev.getX(ev.getActionIndex()) - lastX;
    //  double dy = ev.getY(ev.getActionIndex()) - lastY;
    //  if (Math.abs(dx) < touchSlop && Math.abs(dy) < touchSlop) {
    //    if (!isCanFastClick() && CommonUtils.isFastClick(ev)) {
    //      return true;
    //    }
    //  }
    //}
    return super.dispatchTouchEvent(ev);
  }

  @SuppressWarnings("unchecked")
  protected void wrapPresenter(IPresenter presenter, IBaseView view) {
    presenterWrapper.wrap(presenter);
    presenterWrapper.attach(view);
  }

  @CallSuper @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    presenterWrapper.saveInstanceState(outState);
  }

  @CallSuper @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    presenterWrapper.restoreInstanceState(savedInstanceState);
  }

  public void showLoading() {
    showLoading("");
  }

  public void showLoading(DialogInterface.OnDismissListener dismissListener) {
    showLoading();
    mLoadingDialog.setOnDismissListener(dismissListener);
  }

  public void dismissLoading(DialogInterface.OnDismissListener dismissListener) {
    if (mLoadingDialog != null) {
      mLoadingDialog.setOnDismissListener(dismissListener);
    }
    dismissLoading();
  }

  public void showLoading(@StringRes int resId) {
    showLoading(getString(resId));
  }

  public void showLoading(String loadingTip) {
    showLoading(loadingTip, true);
  }

  public void showLoading(String loadingTip, boolean cancelable) {
    mLoadingDialog = createLoadingDialog(loadingTip);
    mLoadingDialog.setCancelable(cancelable);
    mLoadingDialog.setCanceledOnTouchOutside(cancelable);
    mLoadingDialog.show();
  }

  /**
   * hide soft keyboard when call finish();
   */
  @CallSuper @Override public void finish() {
    View view = getCurrentFocus();
    if (view != null) {
      InputMethodUtils.hideKeyboard(view);
    }
    super.finish();
  }

  public Dialog createLoadingDialog(String loadingTip){
    LoadingDialog loadingDialog=new LoadingDialog(this,loadingTip);
    //内容为空时，取消文本显示
    if (TextUtils.isEmpty(loadingTip)){
      loadingDialog.setHasContent(false);
    }else {
      loadingDialog.setHasContent(true);
      loadingDialog.setContent(loadingTip);
    }
    return loadingDialog;
  }

  public void dismissLoading() {
    if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
      //long loadingEndTime = SystemClock.currentThreadTimeMillis();
      //long diff = loadingEndTime - loadingStartTime;
      //if (0 < diff && diff < 1000) {
      //    handler.postDelayed(new Runnable() {
      //        @Override public void run() {
      //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      //                if (isDestroyed()) return;
      //            }
      //            if (isFinishing()) return;
      //            if (mLoadingDialog.isShowing())
      //                mLoadingDialog.dismiss();
      //        }
      //    }, 1000 - diff);
      //    return;
      //}
      mLoadingDialog.dismiss();
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    dismissDialogs();
    presenterWrapper.detach();
  }

  @Override public Resources getResources() {//不跟随系统改变字体大小
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.fontScale = 1.0f;
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;
  }

  /**
   * 统一添加dialog的方式,不需要其它地方调用dialog.show方法。直接统一显示
   */
  public void addDialog(Dialog dialog) {
    if (isFinishing()) {
      return;
    }
    if (!dialog.isShowing()) {
      dialog.show();
    }
    dialogs.add(dialog);
  }

  final protected void dismissDialogs() {
    for (Dialog dialog : dialogs) {
      if (dialog != null && dialog.isShowing()) {
        dialog.dismiss();
      }
    }
    dismissLoading();
  }

  /**
   *
   * @param id 用来替换fragment 的layout
   * @param fragment
   * @param isAddStack 是否添加到底栈
   */
  public void changeFragment(int id, @NonNull Fragment fragment, boolean isAddStack) {
    if (id <= 0 || fragment == null) {
      throw new IllegalArgumentException(
          "The layout of view MUST has argument 'id' and the new fragment MUST be NOT NULL.");
    }
    String tagName = fragment.getClass().getSimpleName();
    Fragment addedFragment = getSupportFragmentManager().findFragmentByTag(tagName);
    if (addedFragment != null) {
      fragment = addedFragment;
    }
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(id, fragment, tagName);
    if (isAddStack) {
      transaction.addToBackStack(tagName);
    }
    transaction.commitAllowingStateLoss();
  }

  /**
   * 获取loading 对话框，可能为空
   * @return
   */
  public Dialog getLoadingDialog(){
    return mLoadingDialog;
  }
}
