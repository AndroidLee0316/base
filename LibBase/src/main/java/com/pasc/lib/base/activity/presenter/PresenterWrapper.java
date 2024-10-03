package com.pasc.lib.base.activity.presenter;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxu678 on 2018/7/12.
 */
public final class PresenterWrapper<T extends IBaseView> implements IPresenter<T> {
    private List<IPresenter<T>> presenters;

    public PresenterWrapper() {
        presenters = new ArrayList<>();
    }

    public void wrap(IPresenter<T> presenter) {
        if (null == presenter) {
            throw new NullPointerException("presenter should not be empty");
        }
        this.presenters.add(presenter);
    }

    @Override
    public void attach(T view) {
        if (presenters != null && !presenters.isEmpty()) {
            for (IPresenter<T> presenter : presenters) {
                presenter.attach(view);
            }
        }
    }

    @Override
    public void detach() {
        if (presenters != null && !presenters.isEmpty()) {
            for (IPresenter<T> presenter : presenters) {
                presenter.detach();
            }
        }
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        if (presenters != null && !presenters.isEmpty()) {
            for (IPresenter<T> presenter : presenters) {
                presenter.restoreInstanceState(bundle);
            }
        }
    }

    @Override
    public void saveInstanceState(Bundle bundle) {
        if (presenters != null && !presenters.isEmpty()) {
            for (IPresenter<T> presenter : presenters) {
                presenter.saveInstanceState(bundle);
            }
        }
    }
}
