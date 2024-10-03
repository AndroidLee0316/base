package com.pasc.lib.base.user;

import android.content.Context;
import android.os.Bundle;


public interface IUserManager {
    void setUserInfo(IUserInfo user);

    IUserInfo getUserInfo();

    boolean isLogin();

    void updateUser(IUserInfo user);

    String getUserId();

    String getUserName();

    String getToken();

    String getMobile();

    boolean exitUser(Context context);

    boolean isLoginAccount(String mobileNo);

    boolean isOpenFaceVerify(String mobileNo);

    boolean isOpenFaceVerify();

    boolean isCertified();

    String getCertifyType();

    IUserManager init(Context context, String urlAssetsPath);

    Object getUserInfo(int flag, Bundle bundle);

    Object setUserInfo(int flag, Bundle bundle);

    Object updataUserInfo(int flag, Bundle bundle);

    void userKicked(Context context);
}
