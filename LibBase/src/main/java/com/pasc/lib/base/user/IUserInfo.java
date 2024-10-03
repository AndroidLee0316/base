package com.pasc.lib.base.user;

import android.os.Bundle;

public interface IUserInfo {
    String getUserId();//用户id

    String getUserName();//用户姓名

    String getSex();//用户性别 0:未知; 1:男; 2:女

    String getMobileNo();//手机号

    String getIdCard();//身份证号

    String getAddress();//地址

    String getEmail();//电子邮箱

    String getHeadImg();//头像地址

    String getIdPassed();//是否认证

    String getMarray();//1: 未婚, 2:已婚

    String getVolk();//民族

    String getBirthday();//出生日期

    String getBirthPlace();//籍贯

    String getCensus();//户籍类别: 1. 农业户口; 2非农户口

    String getToken();

    String getCertiType();//认证类型: 认证类型（1:代表银行卡认证 2:人脸认证 3:两者皆认证）

    String getHasOpenFace();//人脸开关是否开启

    void setUserId(String userId);//用户id

    void setUserName(String username);//用户姓名

    void setSex(String sex);//用户性别 0:未知; 1:男; 2:女

    void setMobileNo(String mobileNo);//手机号

    void setIdCard(String idCard);//身份证号

    void setAddress(String address);//地址

    void setEmail(String email);//电子邮箱

    void setHeadImg(String headImg);//头像地址

    void setIdPassed(String idPassed);//是否认证

    void setMarray(String marray);//1: 未婚, 2:已婚

    void setVolk(String volk);//民族

    void setBirthday(String birthday);//出生日期

    void setBirthPlace(String birthPlace);//籍贯

    void setCensus(String census);//户籍类别: 1. 农业户口; 2非农户口

    void setToken(String token);

    void setCertiType(String certiType);//认证类型: 认证类型（1:代表银行卡认证 2:人脸认证 3:两者皆认证）

    void setHasOpenFace(String hasOpenFace);

    void setNickName(String nickName);

    boolean save();//保存

    Object getValue(int flag);

    Object setValue(int flag, Bundle bundle);

    String getNickName();

    String getNickNameStatus();

    void setNickNameStatus(String nickNameStatus);
}
