package com.pasc.lib.base.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.pasc.lib.base.AppProxy;

import com.pasc.lib.base.widget.IPermissionDialog;
import com.pasc.lib.base.widget.PermissionDialog;
import java.util.Arrays;
import java.util.HashSet;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by huanglihou519 on 2017/11/15.
 * 适配大部分国内机型，支持魅族摄像头权限
 */

public class PermissionUtils {
  private PermissionUtils() {
    throw new AssertionError("no instances");
  }

  // 官方定义的需要动态请求的权限组
  public static final class Groups {
    public final static String[] LOCATION = {
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    };
    public final static String[] CALENDAR = new String[] {
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    };

    public final static String[] CAMERA = new String[] {
        Manifest.permission.CAMERA
    };

    public final static String[] CONTACTS = new String[] {
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.GET_ACCOUNTS
    };

    public final static String[] MICROPHONE = new String[] {
        Manifest.permission.RECORD_AUDIO
    };

    public final static String[] PHONE = new String[] {
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.USE_SIP,
        Manifest.permission.PROCESS_OUTGOING_CALLS
    };

    public final static String[] SENSORS = new String[] {
        Manifest.permission.BODY_SENSORS
    };

    public final static String[] SMS = new String[] {
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_WAP_PUSH,
        Manifest.permission.RECEIVE_MMS
    };

    public final static String[] STORAGE = new String[] {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
  }

  private static RxPermissions createRxPermissions(@NonNull Activity activity) {
    RxPermissions rxPermissions = new RxPermissions(activity);
    rxPermissions.setLogging(AppProxy.getInstance().isDebug());
    return rxPermissions;
  }

  public static Observable<Boolean> request(@NonNull Activity activity,
      final String... permissions) {
    return createRxPermissions(activity).request(permissions);
  }

  public static Observable<Permission> requestEach(@NonNull Activity activity,
      final String... permissions) {
    return createRxPermissions(activity).requestEach(permissions)
        .map(new Function<com.pasc.lib.base.permission.Permission, Permission>() {
          @Override
          public Permission apply(com.pasc.lib.base.permission.Permission rxPermission) {
            return new Permission(rxPermission.name,
                rxPermission.granted,
                rxPermission.shouldShowRequestPermissionRationale);
          }
        });
  }

  public static Observable<Boolean> shouldShowRequestPermissionRationale(final Activity activity,
      final String... permissions) {
    return createRxPermissions(activity)
        .shouldShowRequestPermissionRationale(activity, permissions);
  }

  public static class Permission {
    public final String name;
    public final boolean granted;
    public final boolean shouldShowRequestPermissionRationale;

    public Permission(String name, boolean granted) {
      this(name, granted, false);
    }

    public Permission(String name, boolean granted, boolean shouldShowRequestPermissionRationale) {
      this.name = name;
      this.granted = granted;
      this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean equals(final Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final com.pasc.lib.base.permission.Permission that =
          (com.pasc.lib.base.permission.Permission) o;

      if (granted != that.granted) return false;
      if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) {
        return false;
      }
      return name.equals(that.name);
    }

    @Override
    public int hashCode() {
      int result = name.hashCode();
      result = 31 * result + (granted ? 1 : 0);
      result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
      return result;
    }

    @Override
    public String toString() {
      return "Permission{" +
          "name='" + name + '\'' +
          ", granted=" + granted +
          ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
          '}';
    }
  }

  public static String getPermissionName(String[] permissions) {
    if (permissions == null || permissions.length < 1) {
      return "相关权限";
    }
    HashSet<String> set = new HashSet<>(Arrays.asList(permissions));
    String name = "";
    int num = 0;
    if (set.removeAll(Arrays.asList(Groups.CAMERA))) {
      name = "相机";
      num++;
    }
    if (set.removeAll(Arrays.asList(Groups.LOCATION))) {
      name = "定位";
      num++;
    }
    if (set.removeAll(Arrays.asList(Groups.STORAGE))) {
      name = "存储";
      num++;
    }
    if (set.removeAll(Arrays.asList(Groups.PHONE))) {
      name = "电话";
      num++;
    }
    if (num != 1) {
      name = "相关权限";
    }
    return name;
  }

  /**
   * @param context
   * @param permissions
   * @return
   */
  public static boolean checkPermission(Context context, String... permissions) {
    if (permissions == null || permissions.length <= 0) return false;
    for (String permission : permissions) {
      if (ActivityCompat.checkSelfPermission(context, permission)
          != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  public static Observable<Boolean> requestWithDialog(@NonNull Activity activity,
      final String... permissions) {
    return PermissionUtils.request(activity, permissions).map(a -> {
      if (!a) {
        showPermissionDialog(activity, permissions);
      }
      return a;
    });
  }

  public static Observable<Boolean> requestWithDialog(@NonNull Activity activity,
      PermissionDialogBuilder builder, final String... permissions) {
    return PermissionUtils.request(activity, permissions).map(a -> {
      if (!a) {
        showPermissionDialog(activity, builder, permissions);
      }
      return a;
    });
  }

  Intent appIntent;

  public static void gotoApplicationDetails(Context context) {
    Intent appIntent;

    //魅族(目前发现魅族 M6 Note（Android 7.1.2，Flyme 6.1.4.7A）出现在应用信息页打开权限不管用的情况，必须在管家中打开方可生效，所以魅族手机暂定跳转手机管家)
    appIntent = context.getPackageManager()
        .getLaunchIntentForPackage("com.meizu.safe");
    if (appIntent != null) {
      context.startActivity(appIntent);
      return;
    }

    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Build.VERSION.SDK_INT >= 9) {
      intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      intent.setData(Uri.fromParts("package", context.getPackageName(), null));
    } else if (Build.VERSION.SDK_INT <= 8) {
      intent.setAction(Intent.ACTION_VIEW);
      intent.setClassName("com.android.settings",
          "com.android.settings.InstalledAppDetails");
      intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
    }
    context.startActivity(intent);
  }

  private static IPermissionDialog showPermissionDialog(Activity activity,
      @NonNull PermissionDialogBuilder builder,
      String... permissions) {
    return builder.show(activity);
  }

  public static IPermissionDialog showPermissionDialog(Activity activity, String... permissions) {
    PermissionIdentifier.PermissionBean permissionBean =
        PermissionIdentifier.getInstance().fromPermissions(permissions);
    return showPermissionDialog(activity,
        PermissionDialogBuilder.builder()
            .IPermissionDialog(new PermissionDialog(activity))
            .title(permissionBean.title)
            .hint(permissionBean.hint)
            .icon(permissionBean.icon)
            .build(), permissions);
  }
}
