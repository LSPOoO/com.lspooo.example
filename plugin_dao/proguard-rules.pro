# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\developer\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

# 数据库混淆配置
-keep class de.greenrobot.dao.** {*;}
#保持greenDao的方法不被混淆
#用来保持生成的表名不被混淆
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
     public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class data.db.dao.*$Properties {
     public static <fields>;
}
-keepclassmembers class data.db.dao.** {
    public static final <fields>;
}

-keepclassmembers class com.yuntongxun.plugin.greendao3.helper.RXDepartment {
    <fields>;
}

-keepclassmembers class com.yuntongxun.plugin.greendao3.helper.RXEmployee {
    <fields>;
}