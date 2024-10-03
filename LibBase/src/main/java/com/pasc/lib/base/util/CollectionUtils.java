package com.pasc.lib.base.util;

import java.util.Collection;

/**
 * Created by yintangwen952 on 2018/9/2.
 */

public class CollectionUtils {

    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(Collection c) {
        return c != null && !c.isEmpty();
    }

}
