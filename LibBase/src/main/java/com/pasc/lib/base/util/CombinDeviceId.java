package com.pasc.lib.base.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

class CombinDeviceId {
    private static final String SP_NAME = "sp_device";
    private static final String SP_DEVICEID_KEY = "key_device_id";
    private static final String DEVICEID_FILE_NAME = ".pasc_device_id";

    private static final String MAC_ADDRESS_DEFAULT = "02:00:00:00:00:00";
    private static final String MAC_ADDRESS_INVALID = "00:00:00:00:00:00";
    private static final Pattern MAC_PATTERN = Pattern.compile("^([0-9A-F]{2}:){5}([0-9A-F]{2})$");
    private static final Pattern DEVICEID_PATTERN_MD5_BASE64 = Pattern.compile("[0-3][0-9a-f]{24,32}");
    private static final Pattern DEVICEID_PATTERN_MD5 = Pattern.compile("[0-3][0-9a-f]{32}");


    static TelephonyManager sTelephonyManager;
    static String sDeviceID;

    public static String getDeviceId(Context context) {
        if (TextUtils.isEmpty(sDeviceID)) {
            try {
                sDeviceID = getDeviceID(context);
            } catch (Exception e) {

            }
        }
        return sDeviceID;
    }

    static String getFileDeviceId(Context context) {
        if (Build.VERSION.SDK_INT >= 23 && !checkPermission(context, "android.permission.READ_EXTERNAL_STORAGE")) {
            return null;
        } else {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return getFromFile(new File(Environment.getExternalStorageDirectory(), DEVICEID_FILE_NAME));
            } else {
                return null;
            }
        }
    }

    private static String getDeviceID(Context context) {
        String spDeviceID = (String) SPUtils.getInstance().getParam(SP_NAME, SP_DEVICEID_KEY, "");
        String fileDeviceID = getFileDeviceId(context);
        String[] lastList = new String[]{spDeviceID, fileDeviceID};
        String deviceId = null;
        int size = lastList.length;

        String value;
        for (int i = 0; i < size; ++i) {
            value = lastList[i];
            if (!TextUtils.isEmpty(value) && DEVICEID_PATTERN_MD5.matcher(value).matches()) {
                deviceId = value;
                break;
            }
        }

        if (TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(spDeviceID) && Math.random() < 0.99D) {
            for (int i = 0; i < size; ++i) {
                value = lastList[i];
                if (!TextUtils.isEmpty(value) && DEVICEID_PATTERN_MD5_BASE64.matcher(value).matches()) {
                    deviceId = value;
                    break;
                }
            }
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = creatDeviceId(context);
        }

        if (!deviceId.equals(spDeviceID)) {
            SPUtils.getInstance().setParam(SP_NAME, SP_DEVICEID_KEY, deviceId);
        }

        if (!deviceId.equals(fileDeviceID)) {
            save2File(deviceId);
        }

        return deviceId;
    }

    private static void save2File(String deviceId) {
        write2File(new File(Environment.getExternalStorageDirectory(), DEVICEID_FILE_NAME), deviceId);
    }

    private static void write2File(File file, String value) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(value.getBytes());
            fileOutputStream.close();
            if (Build.VERSION.SDK_INT >= 9) {
                Method method = file.getClass().getMethod("setReadable", Boolean.TYPE, Boolean.TYPE);
                method.invoke(file, true, false);
            } else {
                Runtime.getRuntime().exec("chmod 444 " + file.getAbsolutePath());
            }
        } catch (Exception e) {

        }

    }

    private static String creatDeviceId(Context context) {
        String deviceId = appendDeviceId(context);
        return "3" + EncryptUtils.getMD5(deviceId);
    }

    private static String appendDeviceId(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(getIMEI(context)).append('-').append(getMac(context)).append('-').append(getAndroidId(context));
        return builder.toString();
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), "android_id");

    }

    public static String getIMEI(Context context) {

        if (Build.VERSION.SDK_INT >= 23 && !checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            return null;
        }

        String imei = null;
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            ArrayList<String> imeiList = ImeiUtil.getImeiList(context);
            if (imeiList != null && imeiList.size() > 0) {
                if (imeiList.size() == 2) {
                    imei = imeiList.get(1);
                }
                if (TextUtils.isEmpty(imei)) {
                    imei = imeiList.get(0);
                }
            }

            if (imei == null) {
                if (sTelephonyManager == null) {
                    sTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                }
                imei = sTelephonyManager.getDeviceId();
            }
        }
        return imei;

    }

    private static String getFromFile(File file) {
        FileInputStream inputStream = null;
        try {
            if (file.exists() && file.canRead()) {
                inputStream = new FileInputStream(file);
                byte[] bytes = new byte[128];
                int i = inputStream.read(bytes);
                return new String(bytes, 0, i);
            }
        } catch (IOException e) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static String getMac(Context context) {
        String mac = null;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                ArrayList networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                if (networkInterfaces == null || networkInterfaces.size() <= 0) {
                    return MAC_ADDRESS_DEFAULT;
                }

                Iterator iterator = networkInterfaces.iterator();

                while (true) {
                    NetworkInterface networkInterface;
                    do {
                        if (!iterator.hasNext()) {
                            return !TextUtils.isEmpty(mac) ? mac : MAC_ADDRESS_DEFAULT;
                        }
                        networkInterface = (NetworkInterface) iterator.next();
                    } while (!networkInterface.getName().equalsIgnoreCase("wlan0"));

                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null) {
                        return !TextUtils.isEmpty(mac) ? mac : MAC_ADDRESS_DEFAULT;
                    }

                    StringBuilder builder = new StringBuilder();
                    int size = hardwareAddress.length;

                    for (int i = 0; i < size; ++i) {
                        byte address = hardwareAddress[i];
                        builder.append(String.format("%02X:", address));
                    }

                    if (builder.length() > 0) {
                        builder.deleteCharAt(builder.length() - 1);
                    }

                    mac = builder.toString().toUpperCase().trim();
                }
            } catch (Exception e) {
                return !TextUtils.isEmpty(mac) ? mac : MAC_ADDRESS_DEFAULT;
            }
        }

        if (checkPermission(context, "android.permission.ACCESS_WIFI_STATE")) {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (manager.isWifiEnabled()) {
                WifiInfo wifiInfo = manager.getConnectionInfo();
                if (wifiInfo != null) {
                    mac = wifiInfo.getMacAddress();
                    if (mac != null) {
                        mac = mac.toUpperCase().trim();
                        if (MAC_ADDRESS_INVALID.equals(mac) || !MAC_PATTERN.matcher(mac).matches()) {
                            mac = null;
                        }
                    }
                }
            }
        }
        return mac;

    }

    public static boolean checkPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
