package i.farmer.widget.imageview;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.RequiresApi;

/**
 * @author guodx
 * @created-time 2021/10/9 10:33 上午
 * @description 圆角边框
 */
@RequiresApi(21)
class RoundBorderDrawable extends Drawable {
    private final Paint mPaint;
    private final RectF mBoundsF;
    private float mRadius;

    RoundBorderDrawable(float radius, float width, int color) {
        this.mRadius = radius;
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
        float halfWidth = mPaint.getStrokeWidth() / 2.f;
        float inset = (int) halfWidth;
        canvas.drawRoundRect(mBoundsF, mRadius - inset, mRadius - inset, paint);
    }

    private void updateBounds(Rect bounds) {
        if (bounds == null) {
            bounds = getBounds();
        }
        float halfWidth = mPaint.getStrokeWidth() / 2.f;
        mBoundsF.set(bounds);
        mBoundsF.inset((int) halfWidth, (int) halfWidth);
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

    void setRadius(float radius) {
        if (radius == mRadius) {
            return;
        }
        mRadius = radius;
        updateBounds(null);
        invalidateSelf();
    }
}
