package i.farmer.widget.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author guodx
 * @created-time 2021/10/17 12:50 下午
 * @description 部分圆角
 */
public class RoundImpl2 implements ImageViewImpl {
    private Paint mBorderPaint;
    private Paint mShaderPaint;
    private Paint mBitmapPaint;                  // 图片画笔
    private float[] radius;

    @Override
    public void initialize(ImageViewDelegate delegate, float radius, float[] radiusArr, Paint borderPaint) {
        this.radius = radiusArr;
        this.mBorderPaint = borderPaint;

        mShaderPaint = new Paint();
        mShaderPaint.setColor(Color.WHITE);
        mShaderPaint.setAntiAlias(true);
        mShaderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setXfermode(null);
    }

    @Override
    public boolean draw(ImageViewDelegate delegate, Canvas canvas) {
        int width = delegate.getView().getWidth();
        int height = delegate.getView().getHeight();
        if (width <= 0 || height <= 0) {
            return false;
        }
        Rect rect = canvas.getClipBounds();
        RectF f = new RectF();
        f.set(rect);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        delegate.superDraw(canvas2);
        for (int i = 0; i < radius.length; i++) {
            drawShader(canvas2, width, height, i);
        }
        canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return true;
    }

    @Override
    public void onDraw(ImageViewDelegate delegate, Canvas canvas) {
        if (null == mBorderPaint) {
            return;
        }

        Rect rect = canvas.getClipBounds();
        RectF f = new RectF();
        f.set(rect);
        // 边框
        float inset = mBorderPaint.getStrokeWidth() / 2.f;
        f.inset(inset, inset);  // 解决边框粗细的问题
        Path mRoundPath = new Path();
        float[] rr = new float[8];
        for (int i = 0; i < radius.length; i++) {
            float r = radius[i] - inset;
            if (r < 0) {
                r = 0;
            }
            rr[i * 2] = rr[i * 2 + 1] = r;
        }
        mRoundPath.addRoundRect(f, rr, Path.Direction.CW);
        canvas.drawPath(mRoundPath, mBorderPaint);
    }

    @Override
    public boolean isCircle() {
        return false;
    }


    private void drawShader(Canvas canvas, int width, int height, @Corner int corner) {
        final float r = radius[corner];
        if (r <= 0) {
            return;
        }
        Path path = new Path();
        if (corner == Corner.TOP_LEFT) {
            path.moveTo(0, 0);
            path.lineTo(r, 0);
            path.arcTo(new RectF(0, 0, r * 2, r * 2), -90, -90);
        } else if (corner == Corner.TOP_RIGHT) {
            path.moveTo(width, 0);
            path.lineTo(width, r);
            path.arcTo(new RectF(width - r * 2, 0, width, r * 2), 0, -90);
        } else if (corner == Corner.BOTTOM_RIGHT) {
            path.moveTo(width, height);
            path.lineTo(width - r, height);
            path.arcTo(new RectF(width - r * 2, height - r * 2, width, height), 90, -90);
        } else {
            path.moveTo(0, height);
            path.lineTo(0, height - r);
            path.arcTo(new RectF(0, height - r * 2, r * 2, height), 180, -90);
        }
        path.close();

        canvas.drawPath(path, mShaderPaint);
    }
}
