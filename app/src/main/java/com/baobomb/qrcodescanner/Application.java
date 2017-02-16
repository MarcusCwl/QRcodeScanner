package com.baobomb.qrcodescanner;

import android.view.WindowManager;

/**
 * Created by Baobomb on 2015/8/27.
 */
public class Application extends android.app.Application {

    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }

    public void onCreate() {
        super.onCreate();
    }

}
