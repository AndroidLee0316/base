package com.pasc.lib.base.permission;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.widget.IPermissionDialog;

/**
 * Created by zhangxu678 on 2018/12/26.
 */
public interface IPermissionClickListener {
  public static final IPermissionClickListener DEFAULT = new IPermissionClickListener() {

    @Override public void onSetting(IPermissionDialog iPermissionDialog) {
      if (null != iPermissionDialog) iPermissionDialog.dismiss();
      PermissionUtils.gotoApplicationDetails(AppProxy.getInstance().getContext());
    }

    @Override public void onCancel(IPermissionDialog iPermissionDialog) {
      if (null != iPermissionDialog) iPermissionDialog.dismiss();
    }
  };

  void onSetting(IPermissionDialog iPermissionDialog);

  void onCancel(IPermissionDialog iPermissionDialog);
}
