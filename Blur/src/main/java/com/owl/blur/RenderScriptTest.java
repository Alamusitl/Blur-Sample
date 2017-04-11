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
 * RenderScript 是3.0之后引入的，可以使用兼容包
 * 模糊半径radius变大时出现计算性能问题，模糊半径不能大于25
 * 因此ScriptIntrinsicBlur不能得到模糊程度较高的图片
 * Created by Alamusi on 2017/4/11.
 */

public class RenderScriptTest extends Activity {

    private static final String TAG = RenderScriptTest.class.getSimpleName();

    @BindView(R.id.id_render_img)
    protected ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render);
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
        view.setBackground(new BitmapDrawable(getResources(), BlurUtil.blurWithRender(this, view, bitmap)));
        Toast.makeText(this, TAG + " : " + (System.currentTimeMillis() - startMs), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "blur: " + (System.currentTimeMillis() - startMs));
    }

}
