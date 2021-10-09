package i.farmer.widget.imageview;

import android.content.Context;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * @author guodx
 * @created-time 2021/10/9 9:25 上午
 * @description 圆形
 */
@RequiresApi(21)
public class CircleImageViewApi21Impl implements ImageViewImpl {
    private boolean referToWidth;

    @Override
    public void initialize(ImageViewDelegate imageView, Context context, float radius,
                           float borderWidth, int borderColor) {
        final CircleDrawable background = new CircleDrawable(null);
        imageView.setBackground(background);

        View view = imageView.getImageLayout();
        view.setClipToOutline(true);
        if (borderWidth > 0 && borderColor != 0) {
            // 边框
            final CircleBorderDrawable borderDrawable = new CircleBorderDrawable(borderWidth, borderColor);
            imageView.setBorder(borderDrawable);
        }
    }

    @Override
    public void setCornerRadius(ImageViewDelegate imageView, float radius) {
        // NO-OP
    }

    @Override
    public void setRatio(int width, int height, boolean referToWidth) {
        this.referToWidth = referToWidth;
    }

    @Override
    public void onMeasure(ImageViewDelegate imageView, int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        if (referToWidth) {
            width = height = imageView.getImageLayout().getMeasuredWidth();
        } else {
            width = height = imageView.getImageLayout().getMeasuredHeight();
        }
        imageView.setMeasuredDimension(width, height);
    }
}
