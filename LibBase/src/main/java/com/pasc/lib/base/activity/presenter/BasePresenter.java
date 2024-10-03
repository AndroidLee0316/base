package com.pasc.lib.base.activity.presenter;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by zhangxu678 on 2018/7/12.
 */
public abstract class BasePresenter<V extends IBaseView> implements IPresenter<V> {
    protected CompositeDisposable disposables = new CompositeDisposable();
    protected V baseView;

    @CallSuper
    @Override
    public void attach(V view) {
        this.baseView = view;
    }

    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void saveInstanceState(Bundle outState) {
    }

    @CallSuper
    @Override
    public void detach() {
        disposables.clear();
        baseView = null;
    }


}
