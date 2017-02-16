package com.baobomb.qrcodescanner.pop;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baobomb.qrcodescanner.Application;
import com.baobomb.qrcodescanner.R;


/**
 * Created by Baobomb on 2015/8/27.
 */
public class PopupView extends RelativeLayout {
    private Context context;
    ImageView controll;
    ImageView exit;
    LinearLayout innerLayout;
    LinearLayout controllLayout;
    WindowManager windowManager = (WindowManager) getContext().getApplicationContext()
            .getSystemService(Context.WINDOW_SERVICE);

    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup
            .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup
            .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(100, 100);
    private WindowManager.LayoutParams windowManagerParams = ((Application) getContext()
            .getApplicationContext()).getWindowParams();

    private float mTouchX;
    private float mTouchY;
    private float x;
    private float y;
    private float mStartX;
    private float mStartY;
    private int windowLeft;
    private int statusBarHeight = 0;
    PopupIconService popupIconService;

    public PopupView(Context context) {
        super(context);
        this.context = context;
        this.setLayoutParams(relativeParams);
    }

    public void bind(PopupIconService popupIconService, int statusBarHeight) {
        this.popupIconService = popupIconService;
        this.statusBarHeight = statusBarHeight;
    }

    public void initView() {
        int RGB = android.graphics.Color.rgb(200, 0, 0);
        ViewGroup.LayoutParams drawingParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        controll = new ImageView(context);
        controll.setImageResource(R.drawable.popicon);
        controll.setLayoutParams(imageParams);
        controll.setOnTouchListener(touchListener);

        innerLayout = new LinearLayout(context);
        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout.setBackgroundResource(R.drawable.floating_view_background);

        controllLayout = new LinearLayout(context);
        controllLayout.setOrientation(LinearLayout.HORIZONTAL);

        exit = new ImageView(context);
        exit.setImageResource(R.drawable.ic_highlight_off_white_48dp);
        exit.setLayoutParams(imageParams);
        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        innerLayout.addView(exit);

        controllLayout.addView(controll);
        controllLayout.addView(innerLayout);
        this.addView(controllLayout);
    }


    public OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Rect frame = new Rect();
            getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            // get the x and y, base on left-top point.
            x = motionEvent.getRawX();
            y = motionEvent.getRawY() - statusBarHeight; // statusBarHeight is the height of system status
            // bar.
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: // get the motion of finger
                    // get the view x&y, base on left-top point
                    mTouchX = motionEvent.getX();
                    mTouchY = motionEvent.getY();
                    mStartX = x;
                    mStartY = y;
                    break;
                case MotionEvent.ACTION_MOVE: // get the motion of finger.
                    updateViewPosition();
                    break;
                case MotionEvent.ACTION_UP: // get the motion of finger leave.
                    updateViewPosition();
                    mTouchX = mTouchY = 0;
                    if ((x - mStartX) < 5 && (y - mStartY) < 5) {
                        if (mClickListener != null) {
                            mClickListener.onClick(controll);
                        }
                    }
                    break;
            }
            return true;
        }
    };


    public OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (innerLayout.getVisibility() == VISIBLE) {
                innerLayout.setVisibility(GONE);
            } else {
                innerLayout.setVisibility(VISIBLE);
            }
        }
    };

    private void updateViewPosition() {
        // update the window position
        windowManagerParams.x = (int) (x - mTouchX);
        if ((int) (y - mTouchY) >= statusBarHeight) {
            windowManagerParams.y = (int) (y - mTouchY);
        } else {
            windowManagerParams.y = statusBarHeight;
        }
        windowManager.updateViewLayout(this, windowManagerParams); // refresh to show.
    }

    public void exit() {
        popupIconService.dismissView();
    }

}
