package com.lspooo.example.plugin.common.tools;


import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class SwipeActivityManager {

    private static final String TAG = "RongXin.SwipeActivityManager";

    private static LinkedList<WeakReference<SwipeListener>> mLinkedList = new LinkedList<>();

    public static void notifySwipe(float scrollParent) {
        if(mLinkedList.size() <= 0) {
            return ;
        }
        SwipeListener swipeListener = mLinkedList.get(0).get();
        if(swipeListener == null) {
            return ;
        }
        swipeListener.onScrollParent(scrollParent);
    }

    public static void pushCallback(SwipeListener listener) {
        WeakReference<SwipeListener> swipeListenerWeakReference = new WeakReference<>(listener);
        mLinkedList.add(0, swipeListenerWeakReference);
    }

    public static boolean popCallback(SwipeListener listener) {
        int size = mLinkedList.size();
        if(listener == null) {
            return true;
        }
        LinkedList<Integer> list = new LinkedList<>();
        for(int i = 0 ; i < mLinkedList.size() ; i ++) {
            if(listener == mLinkedList.get(i).get()) {
                mLinkedList.remove(i);
                break;
            }
            list.add(0 , i);
        }
        if(!listener.isEnableGesture() || list.size() == size) {
            return  false;
        }

        for (Integer next : list) {
            WeakReference<SwipeListener> remove = mLinkedList.remove(next.intValue());
        }
        return list.isEmpty();
    }


    public static void notifySettle(boolean open , int speed) {
        if(mLinkedList.size() <= 0) {
            return ;
        }
        SwipeListener swipeListener = mLinkedList.get(0).get();
        if(swipeListener == null) {
            return ;
        }
        swipeListener.notifySettle(open , speed);
    }


    public interface SwipeListener {
        /**
         * Invoke when state change
         * @param scrollPercent scroll percent of this view
         */
        void onScrollParent(float scrollPercent);

        void notifySettle(boolean open, int speed);

        /**
         * 是否可以滑动
         * @return 是否可用滑动
         */
        boolean isEnableGesture();
    }
}
