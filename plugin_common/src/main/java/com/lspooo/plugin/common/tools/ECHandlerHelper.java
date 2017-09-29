package com.lspooo.plugin.common.tools;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by LSP on 2017/9/21.
 */

public class ECHandlerHelper {
    private static Handler b;
    private Handler c;
    private HandlerThread d;

    public ECHandlerHelper() {
        b = null;
        this.c = null;
        this.d = new HandlerThread(ECHandlerHelper.class.getSimpleName(), 0);
        this.d.start();
    }

    private static Handler a() {
        if(b == null) {
            b = new Handler(Looper.getMainLooper());
        }

        return b;
    }

    public static void postDelayedRunnOnUI(Runnable var0, long var1) {
        if(var0 != null) {
            a().postDelayed(var0, var1);
        }
    }

    public static void postRunnOnUI(Runnable var0) {
        if(var0 != null) {
            a().post(var0);
        }
    }

    public static void removeCallbacksRunnOnUI(Runnable var0) {
        if(var0 != null) {
            a().removeCallbacks(var0);
        }
    }

    public void postDelayedRunnOnThead(Runnable var1, long var2) {
        if(var1 != null) {
            this.getTheadHandler().postDelayed(var1, var2);
        }
    }

    public void setHighPriority() {
        if(this.d != null && this.d.isAlive()) {
            int var1 = this.d.getThreadId();

            try {
                if(Process.getThreadPriority(var1) == -8) {
                    return;
                }

                Process.setThreadPriority(var1, -8);
            } catch (Exception var2) {
                return;
            }
        } else {
        }
    }

    public boolean checkInHighPriority() {
        boolean var1 = true;
        if(this.d != null && this.d.isAlive()) {
            int var2 = this.d.getThreadId();

            try {
                var1 = Process.getThreadPriority(var2) == -8;
            } catch (Exception var3) {
            }

            return var1;
        } else {
            return false;
        }
    }

    public void setLowPriority() {
        if(this.d != null && this.d.isAlive()) {
            int var1 = this.d.getThreadId();

            try {
                if(Process.getThreadPriority(var1) == 0) {
                    return;
                }

                Process.setThreadPriority(var1, 0);
            } catch (Exception var2) {
                return;
            }
        } else {
        }
    }

    public static boolean isMainThread() {
        return Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId();
    }

    public Handler getTheadHandler() {
        if(this.c == null) {
            this.c = new Handler(this.d.getLooper());
        }

        return this.c;
    }

    public final Looper getLooper() {
        return this.d.getLooper();
    }

    public boolean isRunnOnThead() {
        return Thread.currentThread().getId() != this.d.getId();
    }

    public int postRunnOnThead(Runnable var1) {
        if(var1 == null) {
            return -1;
        } else {
            this.getTheadHandler().post(var1);
            return 0;
        }
    }
}
