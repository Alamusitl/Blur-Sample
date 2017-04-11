package com.owl.blur;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alamusi on 2017/4/11.
 */

public class FastBlurTest extends Activity {

    private static final String TAG = FastBlurTest.class.getSimpleName();

    @BindView(R.id.id_fast_img)
    protected ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast);
        ButterKnife.bind(this);
        applyBlur();
    }

    private void applyBlur() {
        mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                mImageView.buildDrawingCache();
                Bitmap bitmap = mImageView.getDrawingCache();
                blur(bitmap, mImageView);
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
