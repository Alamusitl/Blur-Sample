package com.owl.blur;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alamusi on 2017/4/11.
 */

public class RealBlurTest extends Activity {

    private static final String TAG = RealBlurTest.class.getSimpleName();

    @BindView(R.id.id_real_top)
    public ImageView mOriginalView;

    @BindView(R.id.id_real_bg)
    public ImageView mBlurView;

    private float mStartY = 0;
    private int mScreenHeight = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real);
        ButterKnife.bind(this);
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        applyBlur();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                float offsetY = moveY - mStartY;
                if (offsetY == 0) {
                    break;
                }
                float movePercent = offsetY / mScreenHeight;
                Log.d(TAG, "onTouchEvent: " + movePercent);
                mOriginalView.setAlpha(movePercent);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void applyBlur() {
        mBlurView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mBlurView.getViewTreeObserver().removeOnPreDrawListener(this);
                mBlurView.buildDrawingCache();
                Bitmap bitmap = mBlurView.getDrawingCache();
                blur(bitmap, mBlurView);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bitmap, View view) {
        long startMs = System.currentTimeMillis();
        view.setBackground(new BitmapDrawable(getResources(), BlurUtil.blurWithFast(view, bitmap)));
        Toast.makeText(this, TAG + " : " + (System.currentTimeMillis() - startMs), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "blur: " + (System.currentTimeMillis() - startMs));
    }
}
