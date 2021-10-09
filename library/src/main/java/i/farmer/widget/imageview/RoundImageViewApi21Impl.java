package i.farmer.widget.imageview;

import android.content.Context;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * @author i-farmer
 * @created-time 2021/9/30 5:06 下午
 * @description 圆角矩形
 */
@RequiresApi(21)
class RoundImageViewApi21Impl implements ImageViewImpl {
    @Override
    public void initialize(ImageViewDelegate imageView, Context context, float radius,
                           float borderWidth, int borderColor) {
        final RoundRectDrawable background = new RoundRectDrawable(null, radius);
        imageView.setBackground(background);

        View view = imageView.getImageLayout();
        view.setClipToOutline(true);
        if (borderWidth > 0 && borderColor != 0) {
            // 边框
            final RoundBorderDrawable borderDrawable = new RoundBorderDrawable(radius, borderWidth, borderColor);
            imageView.setBorder(borderDrawable);
        }
    }

    @Override
    public void setCornerRadius(ImageViewDelegate imageView, float radius) {
        getClipBackground(imageView).setRadius(radius);
        View view = imageView.getImageLayout();
        view.setClipToOutline(true);
        RoundBorderDrawable border = getBorder(imageView);
        if (null != border) {
            border.setRadius(radius);
        }
    }

    private RoundRectDrawable getClipBackground(ImageViewDelegate cardView) {
        return ((RoundRectDrawable) cardView.getBackground());
    }

    private RoundBorderDrawable getBorder(ImageViewDelegate cardView) {
        return ((RoundBorderDrawable) cardView.getBorder());
    }
}
