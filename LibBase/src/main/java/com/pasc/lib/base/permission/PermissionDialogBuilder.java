package com.pasc.lib.base.permission;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.pasc.lib.base.widget.IPermissionDialog;
import com.pasc.lib.base.widget.PermissionDialog;

/**
 * Created by zhangxu678 on 2018/12/26.
 */
public class PermissionDialogBuilder {
  public final int icon;
  public final String title;
  public final String hint;
  public final IPermissionClickListener iPermissionClickListener;
  public IPermissionDialog iPermissionDialog;
  public PermissionDialogBuilder(Builder builder){
    this.icon=builder.icon;
    this.title=builder.title;
    this.hint=builder.hint;
    this.iPermissionClickListener=builder.iPermissionClickListener;
    this.iPermissionDialog=builder.iPermissionDialog;
  }
  public static Builder builder(){
    return new Builder();
  }
  public static class Builder{
    private int icon;
    private String title;
    private String hint;
    private IPermissionClickListener iPermissionClickListener;
    private IPermissionDialog iPermissionDialog;
    public Builder icon(@DrawableRes int icon){
      this.icon=icon;
      return this;
    }
    public Builder title(String title){
      this.title=title;
      return this;
    }
    public Builder hint(String hint){
      this.hint=hint;
      return this;
    }
    public Builder IPermissionClickListener(@NonNull IPermissionClickListener iPermissionClickListener){
      this.iPermissionClickListener=iPermissionClickListener;
      return this;
    }
    public Builder IPermissionDialog(@NonNull IPermissionDialog iPermissionDialog){
      this.iPermissionDialog=iPermissionDialog;
      return this;
    }
    public PermissionDialogBuilder build(){
      return new PermissionDialogBuilder(this);
    }
  }
  public IPermissionDialog show(Context context){
    if (this.iPermissionDialog == null) {
      this.iPermissionDialog = new PermissionDialog(context);
    }
    if (this.iPermissionClickListener != null) {
      this.iPermissionDialog.setPermissionClickListener(this.iPermissionClickListener);
    }
    this.iPermissionDialog.setIcon(this.icon);
    this.iPermissionDialog.setHint(this.hint);
    this.iPermissionDialog.setTitle(this.title);
    if(!this.iPermissionDialog.isShowing()) {
      this.iPermissionDialog.show();
    }
    return iPermissionDialog;
  }
}
