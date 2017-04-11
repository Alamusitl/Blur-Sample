package com.owl.blur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by Alamusi on 2017/4/11.
 */

public class BlurUtil {

    public static float sScaleFactor = 8;
    /**
     * 0 - 25之间
     */
    public static float sRadius = 8;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurWithRender(Context context, View view, Bitmap bitmap) {
        Bitmap overlay = getResizeBitmap(view, bitmap);
        RenderScript rs = RenderScript.create(context);

        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(BlurUtil.sRadius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        rs.destroy();
        return overlay;
    }

    public static Bitmap blurWithFast(View view, Bitmap bitmap) {
        return FastBlur.doBlur(getResizeBitmap(view, bitmap), (int) sRadius, true);
    }

    private static Bitmap getResizeBitmap(View view, Bitmap bitmap) {
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / sScaleFactor),
                (int) (view.getMeasuredHeight() / sScaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / sScaleFactor, -view.getTop() / sScaleFactor);
        canvas.scale(1 / sScaleFactor, 1 / sScaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return overlay;
    }
}
