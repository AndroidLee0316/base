package com.pasc.lib.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pasc.lib.base.activity.presenter.BasePresenter;
import com.pasc.lib.base.activity.presenter.IBaseView;
import com.pasc.lib.base.activity.presenter.IPresenter;
import com.pasc.lib.base.activity.presenter.PresenterWrapper;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Fragment基类
 * <p>
 * Created by duyuan797 on 2016/11/19.
 */
public abstract class BaseFragment extends RxFragment {
  @SuppressWarnings("unchecked") private final PresenterWrapper presenterWrapper =
      new PresenterWrapper();
  
  @CallSuper @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    if (layoutResId() != 0) {
      View view = inflater.inflate(layoutResId(), container, false);
      beforeView(view);
      return view;
    }
    return super.onCreateView(inflater, container, savedInstanceState);
  }
  @CallSuper
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    onInit(savedInstanceState);
  }

  @SuppressWarnings("unchecked") protected void wrapPresenter(IPresenter presenter, IBaseView view) {
    presenterWrapper.wrap(presenter);
    presenterWrapper.attach(view);
  }


  @Override public void onDestroyView() {
    super.onDestroyView();
    presenterWrapper.detach();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  /**
   * 返回页面布局
   *
   * @return layout id
   */
  protected abstract int layoutResId();

  protected void beforeView(View rootView) {
  }

  /**
   * onViewCreated中执行的方法
   * @param savedInstanceState
   */
  protected abstract void onInit(@Nullable Bundle savedInstanceState);
}
