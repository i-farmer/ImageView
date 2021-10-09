package i.farmer.widget.imageview;

import android.graphics.drawable.Drawable;

/**
 * @author i-farmer
 * @created-time 2021/9/30 5:05 下午
 * @description
 */
interface ImageViewDelegate {
    /**
     * 设置背景
     *
     * @param drawable
     */
    void setBackground(Drawable drawable);

    /**
     * 获取背景
     *
     * @return
     */
    Drawable getBackground();

    /**
     * 获取当前外部容器
     *
     * @return
     */
    ImageLayout getImageLayout();

    /**
     * 设置边框
     *
     * @param drawable
     */
    void setBorder(Drawable drawable);

    /**
     * 获取边框
     *
     * @return
     */
    Drawable getBorder();

    /**
     * 设置尺寸
     *
     * @param measuredWidth
     * @param measuredHeight
     */
    void setMeasuredDimension(int measuredWidth, int measuredHeight);
}
