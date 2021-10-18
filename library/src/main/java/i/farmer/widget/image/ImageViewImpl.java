package i.farmer.widget.image;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author guodx
 * @created-time 2021/10/17 12:41 下午
 * @description
 */
interface ImageViewImpl {
    void initialize(ImageViewDelegate delegate, float radius, float[] radiusArr, Paint borderPaint);

    boolean isCircle();

    /**
     * @param delegate
     * @param canvas
     * @return true 自己处理draw false不处理
     */
    boolean draw(ImageViewDelegate delegate, Canvas canvas);

    void onDraw(ImageViewDelegate delegate, Canvas canvas);

}
