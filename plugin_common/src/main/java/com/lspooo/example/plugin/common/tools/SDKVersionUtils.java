package com.lspooo.example.plugin.common.tools;

import android.os.Build;

public class SDKVersionUtils {

    public static boolean isSmallerVersion(int version) {
        return (Build.VERSION.SDK_INT < version);
    }

    public static boolean isGreatThanOrEqualTo(int version) {
        return (Build.VERSION.SDK_INT >= version);
    }

    public static boolean isSmallerorEqual(int version) {
        return (Build.VERSION.SDK_INT <= version);
    }
}
