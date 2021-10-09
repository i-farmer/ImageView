package i.farmer.widget.imageview;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.RequiresApi;

/**
 * Very simple drawable that draws a oval background and also
 * reports proper outline for Lollipop.
 * <p>
 * Simpler and uses less resources compared to GradientDrawable or ShapeDrawable.
 */
@RequiresApi(21)
class CircleDrawable extends Drawable {
    private final Rect mBoundsI;

    CircleDrawable() {
        mBoundsI = new Rect();
    }

    @Override
    public void draw(Canvas canvas) {

    }

    private void updateBounds(Rect bounds) {
        if (bounds == null) {
            bounds = getBounds();
        }
        mBoundsI.set(bounds);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    @Override
    public void getOutline(Outline outline) {
        outline.setOval(mBoundsI);
    }


    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
