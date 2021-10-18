package i.farmer.widget.image;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * @author guodx
 * @created-time 2021/10/17 12:50 下午
 * @description 圆形
 */
public class CircleImpl implements ImageViewImpl {
    private Paint mBorderPaint;

    @Override
    public void initialize(ImageViewDelegate delegate, float radius, float[] radiusArr, Paint borderPaint) {
        this.mBorderPaint = borderPaint;
        delegate.getView().setOutlineProvider(outlineProvider);
        delegate.getView().setClipToOutline(true);
    }

    private ViewOutlineProvider outlineProvider = new ViewOutlineProvider() {
        @Override
        public void getOutline(View view, Outline outline) {
            if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
                return;
            }
            outline.setOval(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
    };

    @Override
    public boolean draw(ImageViewDelegate delegate, Canvas canvas) {
        // NO-OP
        return false;
    }

    @Override
    public void onDraw(ImageViewDelegate delegate, Canvas canvas) {
        if (null == mBorderPaint) {
            return;
        }
        // 边框
        Rect rect = canvas.getClipBounds();
        RectF f = new RectF();
        f.set(rect);
        float inset = mBorderPaint.getStrokeWidth() / 2.f;
        f.inset(inset, inset);  // 解决边框粗细的问题
        canvas.drawOval(f, mBorderPaint);
    }

    @Override
    public boolean isCircle() {
        return true;
    }
}
