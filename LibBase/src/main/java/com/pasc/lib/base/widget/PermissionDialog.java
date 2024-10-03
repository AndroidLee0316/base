package com.pasc.lib.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.pasc.lib.base.R;
import com.pasc.lib.base.permission.IPermissionClickListener;
import com.pasc.lib.base.widget.IPermissionDialog;

/**
 * Created by zhangxu678 on 2018/12/26.
 */
public class PermissionDialog extends Dialog implements IPermissionDialog {
  private ImageView ivIcon;
  private TextView tvTitle;
  private TextView tvHint;
  private Button btnSetting;
  private ImageView ivClose;
  private IPermissionClickListener iPermissionClickListener = IPermissionClickListener.DEFAULT;

  public PermissionDialog(@NonNull Context context) {
    super(context,R.style.common_loading_dialog);
    setContentView(R.layout.base_layout_permission_dialog);
    setCanceledOnTouchOutside(false);
    initViews();
    setListener();
  }

  private void initViews() {
    ivIcon = findViewById(R.id.base_per_icon);
    tvTitle = findViewById(R.id.tv_per_title);
    tvHint = findViewById(R.id.tv_per_hint);
    btnSetting = findViewById(R.id.btn_setting);
    ivClose = findViewById(R.id.iv_close);
  }

  private void setListener() {
    btnSetting.setOnClickListener(view -> iPermissionClickListener.onSetting(PermissionDialog.this));
    ivClose.setOnClickListener(view -> iPermissionClickListener.onCancel(PermissionDialog.this));
  }

  @Override public void setIcon(int icon) {
    if (icon != 0) {
      ivIcon.setImageResource(icon);
    }
  }

  @Override public void setHint(String hint) {
    if (hint != null) {
      tvHint.setText(hint);
    }
  }

  @Override public void setTitle(String title) {
    if (title != null) {
      tvTitle.setText(title);
    }
  }

  @Override
  public void setPermissionClickListener(IPermissionClickListener iPermissionClickListener) {
    if (iPermissionClickListener != null) {
      this.iPermissionClickListener = iPermissionClickListener;
    }
  }
}
