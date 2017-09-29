package com.lspooo.plugin.common.common.bar;

import android.os.Build;

/**
 *
 * @author 容联•云通讯
 * @since 2017-05-12
 */
public class StatusBarExclude {
    static boolean exclude = false;

    public static void excludeIncompatibleFlyMe() {
        try {
            Build.class.getMethod("hasSmartBar");
        } catch (NoSuchMethodException e) {
            exclude |= Build.BRAND.contains("Meizu");
        }
    }
}
