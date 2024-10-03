package com.pasc.lib.base.activity.presenter;

import android.os.Bundle;

/**
 * Created by zhangxu678 on 2018/7/12.
 */
public interface IPresenter<T extends IBaseView> {
    void attach(T view);

    void restoreInstanceState(Bundle savedInstanceState);


    void saveInstanceState(Bundle outState);

    void detach();

}
