package com.lspooo.example.plugin.common.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.MediaMetadataRetriever;
import android.os.SystemClock;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackwardSupportUtil {

    private static float density;

    static {
        density = -1.0F;
    }

    /**
     * 当前手机分辨率-宽
     * @param context 上下文
     * @return 手机分辨率-宽
     */
    public static int getWidthPixels(Context context) {
        if(context == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 当前手机分辨率-高
     * @param context 上下文
     * @return 手机分辨率-高
     */
    public static int getHeightPixels(Context context) {
        if(context == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断当前是否一个手机号码
     * @param mobiles 手机号码
     * @return 是否手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 屏幕密度转换
     * @param ctx 上下文
     * @param dp dp
     * @return 转换结果
     */
    public static int fromDPToPix(Context ctx, int dp) {
        return Math.round(getDensity(ctx) * dp);
    }

    public static float getDensity(Context ctx) {
        Context context = ctx;
        if (density < 0.0F)
            density = context.getResources().getDisplayMetrics().density;
        return density;
    }


    /**
     * 像素转换成手机屏幕密度
     * @param pxValue 像素
     * @return 密度
     */
    public static int px2dip(Context ctx , float pxValue) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return Math.round(displayMetrics.densityDpi * pxValue / 160.0F);
    }

    /**
     * 按照一定的格式截取字符串
     * @param srcText 源字符串
     * @param p 截取字符
     * @return 结果
     */
    public static String getLastWords(String srcText, String p) {
        try {
            String[] array = TextUtils.split(srcText, p);
            int index = (array.length - 1 < 0) ? 0 : array.length - 1;
            return array[index];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否为空
     * @param value 判断字符串
     * @return 是否空
     */
    public static boolean isNullOrNil(String value) {
        return !((value != null) && (value.length() > 0));
    }

    /**
     * 如果为空则返回控制付出
     * @param value 源字符串
     * @return 结果
     */
    public static String nullAsNil(String value) {
        return isNullOrNil(value) ? "" : value;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /**
     * 是否全数字
     * @param number 是否数字
     * @return 验证结果
     */
    public static boolean number(String number) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(number);
        return m.matches();
    }


    /**
     * 将字符串转换成Long数据，如果为空则返回默认值
     * @param str
     * @param def
     * @return
     */
    public static long getLong(String str, long def) {
        try {
            if (str == null) {
                return def;
            }
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return def;
    }


    /**
     * 将字符串转换成整型，如果为空则返回默认值
     * @param str 字符串
     * @param def 默认值
     * @return
     */
    public static int getInt(String str, int def) {
        try {
            if (str == null) {
                return def;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        return def;
    }

    /**
     * 将集合转换成字符串，用特殊字符做分隔符
     * @param srcList 转换前集合
     * @param separator 分隔符
     * @return 字符串
     */
    public static String listToString(List<String> srcList, String separator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < srcList.size(); ++i)
            if (i == srcList.size() - 1) {
                sb.append(srcList.get(i).trim());
            } else {
                sb.append(srcList.get(i).trim() + separator);
            }
        return sb.toString();
    }

    /**
     * 字符串数组转换成字符串
     * @param srcList 数组
     * @param separator 分隔符
     * @return 字符串
     */
    public static String arrayToString(String[] srcList, String separator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < srcList.length; ++i)
            if (i == srcList.length - 1) {
                sb.append(srcList[i].trim());
            } else {
                sb.append(srcList[i].trim() + separator);
            }
        return sb.toString();
    }

    /**
     * 将集合转换成字符串，用特殊字符做分隔符
     * @param str 转换前集合
     * @param separator 分隔符
     * @return 字符串
     */
    public static List<String> stringtoList(String str , String separator) {
        if(isNullOrNil(str)) {
            return new ArrayList<>();
        }

        String[] split = str.split(separator);
        return Arrays.asList(split);
    }

    /**
     * 将字符串数组转换成字符串集合o
     * @param src
     * @return
     */
    public static List<String> stringsToList(String[] src) {
        if ((src == null) || (src.length == 0)) {
            return null;
        }
        ArrayList<String> dest = new ArrayList<String>();
        for (int i = 0; i < src.length; ++i) {
            dest.add(src[i]);
        }
        return dest;
    }

    /**
     * 获取当前状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 25;

        try {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
        }

        return statusBarHeight;
    }
}
