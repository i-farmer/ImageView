package i.farmer.widget.image;

import android.graphics.Canvas;
import android.view.View;

/**
 * @author guodx
 * @created-time 2021/10/17 12:43 下午
 * @description
 */
interface ImageViewDelegate {
    View getView();

    void superDraw(Canvas canvas);
}
