package com.baobomb.qrcodescanner.pop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.baobomb.qrcodescanner.Application;


/**
 * Created by Baobomb on 2015/8/27.
 */
public class PopupIconService extends Service {


    private WindowManager windowManager = null;
    private WindowManager.LayoutParams windowManagerParams = null;

    private WindowManager.LayoutParams listParams = null;
    private PopupView popupView = null;
    private int restoreX = 0;
    private int restoreY = 0;
    int i = 0;
    Context context;
    boolean isViewShowing = true;
    boolean isFirstIn = true;
    int statusBarHeight = 0;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        statusBarHeight = getStatusBarHeight(getApplicationContext());
        initWindowManager();
        createView();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 显示
        if (popupView != null) {
            popupView.bind(this, statusBarHeight);
        } else {
            createView();
            popupView.bind(this, statusBarHeight);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return new RecorderServiceBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void createView() {
        if (popupView == null) {
            popupView = new PopupView(getApplicationContext());
            popupView.initView();
        }
        listParams.gravity = Gravity.LEFT | Gravity.TOP;
        listParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        listParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        listParams.y = statusBarHeight;
        windowManager.addView(popupView, listParams);
        isViewShowing = true;
    }

    public void dismissView() {
        if (popupView != null & windowManager != null) {
            windowManager.removeView(popupView);
            popupView = null;
            isViewShowing = false;
        }
    }

    public void matchView() {
        listParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        windowManager.updateViewLayout(popupView, listParams);
    }

    public void unMatchView() {
        listParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        windowManager.updateViewLayout(popupView, listParams);
    }

    private void initWindowManager() {
        windowManager = (WindowManager) getSystemService(Context
                .WINDOW_SERVICE);
        windowManagerParams = ((Application) getApplication()).getWindowParams();
        windowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        windowManagerParams.format = PixelFormat.RGBA_8888;
        windowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams
                .FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        listParams = ((Application) getApplication()).getWindowParams();
        listParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        listParams.format = PixelFormat.RGBA_8888;
        listParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams
                .FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
    }

    public boolean isViewShowing() {
        return isViewShowing;
    }

    public class RecorderServiceBinder extends Binder {
        public PopupIconService getService() {
            return PopupIconService.this;
        }
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}