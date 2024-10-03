package com.pasc.lib.base.widget;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.pasc.lib.base.permission.IPermissionClickListener;

/**
 * Created by zhangxu678 on 2018/12/26.
 */
public interface IPermissionDialog {
  void show();

  void dismiss();

  boolean isShowing();

  void setIcon(@DrawableRes int icon);

  void setTitle(String title);

  void setHint(String hint);

  void setPermissionClickListener(@NonNull IPermissionClickListener iPermissionClickListener);
}
