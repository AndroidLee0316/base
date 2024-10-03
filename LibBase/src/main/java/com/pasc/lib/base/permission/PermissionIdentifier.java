package com.pasc.lib.base.permission;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.method.ReplacementTransformationMethod;
import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.R;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by zhangxu678 on 2018/12/26.
 */
public class PermissionIdentifier {
  static PermissionIdentifier permissionIdentifier = new PermissionIdentifier();
  private final String CAMERA = "相机", LOCATION = "定位", STORAGE = "存储", PHONE = "电话";

  public static PermissionIdentifier getInstance() {
    return permissionIdentifier;
  }

  private String generatePermissionStr(@NonNull String... permissions) {
    HashSet<String> set = new HashSet<>(Arrays.asList(permissions));
    int index=0;
    StringBuffer name = new StringBuffer();
    for(String permission:permissions){
      String permissionName=getPermissionName(permission);
      if(index==0){
        name.append(permissionName);
      }else{
        if(!name.toString().contains(permissionName)){
          name.append(",").append(permissionName);
        }
      }
      index++;
    }
    return name.toString();
  }
  private String getPermissionName(String permission){
    if(Arrays.asList(PermissionUtils.Groups.CAMERA).contains(permission)){
      return CAMERA;
    }
    if(Arrays.asList(PermissionUtils.Groups.LOCATION).contains(permission)){
      return LOCATION;
    }
    if(Arrays.asList(PermissionUtils.Groups.STORAGE).contains(permission)){
      return STORAGE;
    }
    if(Arrays.asList(PermissionUtils.Groups.PHONE).contains(permission)){
      return PHONE;
    }
    return "";
  }

  public PermissionBean fromPermissions(String... permissions) {
    String permissionName = generatePermissionStr(permissions);
    switch (permissionName) {
      case CAMERA:
        return new PermissionBean(R.drawable.pasclibbase_ic_camera,
            AppProxy.getInstance().getContext().getString(R.string.base_open_camera),
            AppProxy.getInstance().getContext().getString(R.string.base_perm_hint_default));
      case LOCATION:
        return new PermissionBean(R.drawable.pasclibbase_ic_loc,
            AppProxy.getInstance().getContext().getString(R.string.base_open_loc),
            AppProxy.getInstance().getContext().getString(R.string.base_perm_hint_default));
      case STORAGE:
        return new PermissionBean(R.drawable.pasclibbase_ic_storage,
            AppProxy.getInstance().getContext().getString(R.string.base_open_storage),
            AppProxy.getInstance().getContext().getString(R.string.base_perm_hint_default));
      case PHONE:
        return new PermissionBean(R.drawable.pasclibbase_ic_call,
            AppProxy.getInstance().getContext().getString(R.string.base_open_phone),
            AppProxy.getInstance().getContext().getString(R.string.base_perm_hint_default));
      default:
        return new PermissionBean(R.drawable.pasclibbase_ic_default,
            AppProxy.getInstance().getContext().getString(R.string.base_open_default),
            permissionName);
    }
  }

  class PermissionBean {
    public final int icon;
    public final String title, hint;

    public PermissionBean(@DrawableRes int icon, String title, String hint) {
      this.icon = icon;
      this.title = title;
      this.hint = hint;
    }

    public @DrawableRes int getIcon() {
      return icon;
    }

    public String getTitle() {
      return title;
    }

    public String getHint() {
      return hint;
    }
  }
}
