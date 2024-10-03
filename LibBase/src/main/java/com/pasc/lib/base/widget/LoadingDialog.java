package com.pasc.lib.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pasc.lib.base.R;

/**
 * 通用LoadingDialog
 * Created by duyuan797 on 16/11/24.
 */
public class LoadingDialog extends Dialog {

  //弹出框中文字内容
   TextView textView;
  //弹出框view
   View dialogView;

   ProgressBar progressBar;

  /**
   * 构造方法
   */
  public LoadingDialog(Context context) {
    super(context, R.style.common_loading_dialog);
    setContentView(R.layout.temp_common_loading);
    dialogView = (RelativeLayout) findViewById(R.id.temp_common_dialog_loading_relativelayout);
    progressBar = (ProgressBar) findViewById(R.id.temp_common_dialog_loading_progressbar);
    textView = (TextView) findViewById(R.id.temp_common_dialog_loading_textview);
  }

  public LoadingDialog(Context context, String loadingMsg) {
    this(context);
    setContent(loadingMsg);
  }

  public LoadingDialog(Context context, int loadingMsgId) {
    this(context);
    setContent(loadingMsgId);
  }

  public void setHasContent(boolean hasContent) {
    textView.setVisibility(hasContent ? View.VISIBLE : View.GONE);
  }

  /**
   * 设置弹出框的背景
   */
  public void setLoadingDialogBackGround(int resourceId) {
    if (null != dialogView) {
      dialogView.setBackgroundResource(resourceId);
    }
  }

  /**
   * 设置弹出框的背景
   */
  public void setLoadingDialogBackGround(Drawable drawable) {
    if (null != dialogView) {
      dialogView.setBackgroundDrawable(drawable);
    }
  }

  /**
   * 获取弹出框view
   *
   * @return dialogView
   */
  public View getLoadingDialogView() {
    return dialogView;
  }

  /**
   * 获取弹出框的textview
   *
   * @return textView
   */
  public TextView getTextView() {
    return textView;
  }

  /**
   * 设置弹出框的文字内容
   */
  public void setContent(String content) {
    if (null != textView) {
      textView.setText(content);
    }
  }

  /**
   * 设置弹出框的文字内容
   */
  public void setContent(int resourceId) {
    if (null != textView) {
      textView.setText(resourceId);
    }
  }






  @Deprecated public static class LoadingDialogDelegate {

    private LoadingDialog mLoadingDialog;

    public LoadingDialogDelegate(Context context) {
      mLoadingDialog = new LoadingDialog(context);
    }

    public LoadingDialogDelegate(Context context, String loadingMsg) {
      mLoadingDialog = new LoadingDialog(context, loadingMsg);
    }

    public LoadingDialogDelegate(Context context, int loadingMsgId) {
      mLoadingDialog = new LoadingDialog(context, loadingMsgId);
    }

    public void showLoadingDialog(int msg) {
      if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
        mLoadingDialog.setContent(msg);
        mLoadingDialog.show();
      }
    }

    public void showLoadingDialog() {
      if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
        mLoadingDialog.show();
      }
    }

    public void showLoadingDialog(String msg) {
      if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
        mLoadingDialog.setContent(msg);
        mLoadingDialog.show();
      }
    }

    public void setContent(String content) {
      mLoadingDialog.setContent(content);
    }

    public boolean isShowingDialog() {
      return mLoadingDialog != null && mLoadingDialog.isShowing();
    }

    public void dismissLoadingDialog() {
      if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
        mLoadingDialog.dismiss();
      }
    }
  }
}
