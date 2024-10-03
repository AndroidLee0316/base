package com.pasc.debug.component.libbase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.pasc.lib.base.fragment.BaseFragment;

/**
 * Created by zhangxu678 on 2018/11/14.
 */
public class TestFragment extends BaseFragment {
  private static TestFragment testFragment;

  public static TestFragment getInstance(Bundle bundle) {
    testFragment = new TestFragment();
    testFragment.setArguments(bundle);
    return testFragment;
  }

  @Override protected int layoutResId() {
    return R.layout.fragment_test;
  }

  @Override protected void beforeView(View rootView) {
    super.beforeView(rootView);
    ((TextView) rootView.findViewById(R.id.tv)).setText("设置数据");
  }

  @Override protected void onInit(@Nullable Bundle savedInstanceState) {

  }
}
