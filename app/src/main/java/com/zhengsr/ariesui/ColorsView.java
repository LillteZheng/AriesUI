package com.zhengsr.ariesui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author by  zhengshaorui on 2019/10/8
 * Describe:
 */
public class ColorsView extends View {
    public ColorsView(Context context) {
        this(context,null);
    }

    public ColorsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
      //  int[] colors = new int[]
        /*if (BORDER_WIDTH_PX > 0) {
            borderPaint.setColor(borderColor);

            canvas.drawRect(rect.left - BORDER_WIDTH_PX,
                    rect.top - BORDER_WIDTH_PX,
                    rect.right + BORDER_WIDTH_PX,
                    rect.bottom + BORDER_WIDTH_PX,
                    borderPaint);
        }

        if (hueBackgroundCache == null) {
            hueBackgroundCache = new BitmapCache();
            hueBackgroundCache.bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Config.ARGB_8888);
            hueBackgroundCache.canvas = new Canvas(hueBackgroundCache.bitmap);

            int[] hueColors = new int[(int) (rect.height() + 0.5f)];

            // Generate array of all colors, will be drawn as individual lines.
            float h = 360f;
            for (int i = 0; i < hueColors.length; i++) {
                hueColors[i] = Color.HSVToColor(new float[]{h, 1f, 1f});
                h -= 360f / hueColors.length;
            }

            // Time to draw the hue color gradient,
            // its drawn as individual lines which
            // will be quite many when the resolution is high
            // and/or the panel is large.
            Paint linePaint = new Paint();
            linePaint.setStrokeWidth(0);
            for (int i = 0; i < hueColors.length; i++) {
                linePaint.setColor(hueColors[i]);
                hueBackgroundCache.canvas.drawLine(0, i, hueBackgroundCache.bitmap.getWidth(), i, linePaint);
            }
        }

        canvas.drawBitmap(hueBackgroundCache.bitmap, null, rect, null);

        Point p = hueToPoint(hue);

        RectF r = new RectF();
        r.left = rect.left - sliderTrackerOffsetPx;
        r.right = rect.right + sliderTrackerOffsetPx;
        r.top = p.y - (sliderTrackerSizePx / 2);
        r.bottom = p.y + (sliderTrackerSizePx / 2);

        canvas.drawRoundRect(r, 2, 2, hueAlphaTrackerPaint);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
