package com.pasc.lib.base.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.trello.rxlifecycle2.internal.Preconditions.checkNotNull;

/**
 * A manager to manage lifecycle of {@link Activity}.
 *
 */
public final class ActivityManager implements Application.ActivityLifecycleCallbacks {
  private static ActivityManager singleton;
  private final List<WeakReference<Activity>> activityWeakRefs;

  private ActivityManager() {
    this.activityWeakRefs = new ArrayList<>();
  }

  /**
   * Get global single instance of {@link ActivityManager}. And then we must call {@link
   * #register(Application)} in {@link Application}.
   *
   * @return {@link ActivityManager}
   * @see #register(Application)
   */
  public static ActivityManager get() {
    if (singleton == null) {
      synchronized (ActivityManager.class) {
        if (singleton == null) {
          singleton = new ActivityManager();
        }
      }
    }

    return singleton;
  }

  @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    add(activity);
  }

  @Override public void onActivityStarted(Activity activity) {
  }

  @Override public void onActivityResumed(Activity activity) {
  }

  @Override public void onActivityPaused(Activity activity) {
  }

  @Override public void onActivityStopped(Activity activity) {
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
  }

  @Override public void onActivityDestroyed(Activity activity) {
  }

  public void add(Activity activity) {
    checkNotNull(activity, "activity == null");
    WeakReference<Activity> weakRef = new WeakReference<>(activity);
    activityWeakRefs.add(weakRef);
  }

  private int size() {
    return activityWeakRefs.size();
  }

  public void finish(WeakReference<Activity> weakRef) {
    Activity activity = weakRef.get();
    finish(activity);
  }

  public void finish(Activity activity) {
    if (activity != null && !activity.isFinishing()) {
      activity.finish();
    }
  }

  /**
   * Register {@link ActivityManager}, this should be called in {@link Application#onCreate()}.
   *
   * @param application {@link Application}
   */
  public void register(Application application) {
    application.registerActivityLifecycleCallbacks(this);
  }

  /**
   * Get the {@link Activity} at top.
   *
   * @return {@link Activity}
   */
  public Activity getTopActivity() {
    final int size = size();
    if (size > 0) {
      return activityWeakRefs.get(size - 1).get();
    }
    return null;
  }

  /**
   * To check if an {@link Activity} existed in this manager.
   *
   * @param activity {@link Activity}
   * @return true if existed, otherwise return false
   */
  public boolean contains(Activity activity) {
    if (activity != null) {
      final int size = size();
      for (int i = 0; i < size; i++) {
        WeakReference<Activity> weakRef = activityWeakRefs.get(i);
        Activity ac = weakRef.get();
        if (ac == activity) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Remove an {@link Activity} if existed.
   *
   * @param activity {@link Activity}
   */
  public void remove(@NonNull Activity activity) {
    checkNotNull(activity, "activity == null");
    final int size = size();
    for (int i = 0; i < size; i++) {
      WeakReference<Activity> weakRef = activityWeakRefs.get(i);
      Activity ac = weakRef.get();
      if (ac == activity) {
        activityWeakRefs.remove(weakRef);
        finish(ac);
      }
    }
  }

  /**
   * Clear all the activity and finish them.
   */
  public void clear() {
    final int size = size();
    for (int i = 0; i < size; i++) {
      WeakReference<Activity> weakRef = activityWeakRefs.get(i);
      finish(weakRef);
    }
    activityWeakRefs.clear();
  }
}
