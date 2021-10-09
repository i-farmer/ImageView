package i.farmer.widget.imageview;

import android.content.Context;

/**
 * @author i-farmer
 * @created-time 2021/9/30 5:04 下午
 * @description
 */
interface ImageViewImpl {
    void initialize(ImageViewDelegate imageView, Context context, float radius,
                    float borderWidth, int borderColor);

    void setCornerRadius(ImageViewDelegate imageView, float radius);

    void setRatio(int width, int height, boolean referToWidth);

    void onMeasure(ImageViewDelegate imageView, int widthMeasureSpec, int heightMeasureSpec);
}
