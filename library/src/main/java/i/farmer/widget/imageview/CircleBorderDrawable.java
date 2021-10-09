package i.farmer.widget.imageview;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

/**
 * @author guodx
 * @created-time 2021/10/9 10:34 上午
 * @description 圆形边框
 */
@RequiresApi(21)
class CircleBorderDrawable extends Drawable {
    private final Paint mPaint;
    private final RectF mBoundsF;

    CircleBorderDrawable(float width, int color) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(width);
        mPaint.clearShadowLayer();

        mBoundsF = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {
        final Paint paint = mPaint;
        canvas.drawOval(mBoundsF, paint);
    }

    private void updateBounds(Rect bounds) {
        if (bounds == null) {
            bounds = getBounds();
        }
        float halfWidth = mPaint.getStrokeWidth() / 2.f;
        mBoundsF.set(bounds.left + halfWidth,
                bounds.top + halfWidth,
                bounds.right - halfWidth,
                bounds.bottom - halfWidth);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setBorder(float width, @ColorInt int color) {
        mPaint.setStrokeWidth(width);
        mPaint.setColor(color);
        updateBounds(null);
        invalidateSelf();
    }
}

