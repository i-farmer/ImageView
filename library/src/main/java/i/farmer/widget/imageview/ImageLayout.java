package i.farmer.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author i-farmer
 * @created-time 2021/9/30 4:52 下午
 * @description ImageView 圆角矩形 或 圆形
 */
public class ImageLayout extends FrameLayout {
    private final ImageViewImpl IMPL;
    private View borderViewAt;

    public ImageLayout(@NonNull Context context) {
        this(context, null);
    }

    public ImageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int imageId = 0;
        float radius = 0.f;
        boolean isCircle = false;       // 是否圆形
        float borderWidth = 0.f;
        int borderColor = 0;
        int ratioWidth = -1;
        int ratioHeight = -1;
        boolean referToWidth = true;    // 如果约束比例，参照宽 还是 高

        if (null != attrs) {
            TypedArray tr = null;
            try {
                tr = context.obtainStyledAttributes(attrs, R.styleable.ImageLayout);
                imageId = tr.getResourceId(R.styleable.ImageLayout_imageId, imageId);
                radius = tr.getDimension(R.styleable.ImageLayout_android_radius, radius);
                isCircle = tr.getInt(R.styleable.ImageLayout_shape, 0) == 1;
                borderWidth = tr.getDimension(R.styleable.ImageLayout_borderWidth, borderWidth);
                borderColor = tr.getColor(R.styleable.ImageLayout_borderColor, borderColor);
                ratioWidth = tr.getInteger(R.styleable.ImageLayout_ratioWidth, ratioWidth);
                ratioHeight = tr.getInteger(R.styleable.ImageLayout_ratioHeight, ratioHeight);
                referToWidth = tr.getInt(R.styleable.ImageLayout_referTo, 0) == 0;
            } catch (Exception ex) {

            } finally {
                if (null != tr) {
                    tr.recycle();
                    tr = null;
                }
            }
        }
        AppCompatImageView targetViewAt = new ImageView(context, attrs, viewDelegate);
        // 设置id
        if (imageId != 0) {
            targetViewAt.setId(imageId);
        } else {
            int id = getId();
            setId(0);
            targetViewAt.setId(id);
        }
        // 添加图片视图
        addView(targetViewAt);
        // 添加边框线视图
        borderViewAt = new View(context);
        addView(borderViewAt, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // 初始化圆角 或 圆形
        if (isCircle) {
            IMPL = new CircleImageViewApi21Impl();
        } else {
            IMPL = new RoundImageViewApi21Impl();
        }
        IMPL.initialize(viewDelegate, context, radius, borderWidth, borderColor);
        IMPL.setRatio(ratioWidth, ratioHeight, referToWidth);
    }

    public void setRatio(int width, int height, boolean referToWidth) {
        IMPL.setRatio(width, height, referToWidth);
    }

    public void setRadius(float radius) {
        IMPL.setCornerRadius(viewDelegate, radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        IMPL.onMeasure(viewDelegate, widthMeasureSpec, heightMeasureSpec);
    }

    private ImageViewDelegate viewDelegate = new ImageViewDelegate() {
        private Drawable mBackground;
        private Drawable mBorder;

        @Override
        public void setBackground(Drawable drawable) {
            mBackground = drawable;
            ImageLayout.this.setBackground(drawable);
        }

        @Override
        public Drawable getBackground() {
            return mBackground;
        }

        @Override
        public ImageLayout getImageLayout() {
            return ImageLayout.this;
        }

        @Override
        public void setBorder(Drawable drawable) {
            mBorder = drawable;
            ImageLayout.this.borderViewAt.setBackground(drawable);
        }

        @Override
        public Drawable getBorder() {
            return mBorder;
        }

        @Override
        public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
            ImageLayout.this.setMeasuredDimension(measuredWidth, measuredHeight);
            // 重新计算子视图
            int widthMeasureSpec2 = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
            int heightMeasureSpec2 = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
            measureChildren(widthMeasureSpec2, heightMeasureSpec2);
        }
    };

    /**
     * 外部调用
     */
    public static class ImageView extends AppCompatImageView {
        private ImageViewDelegate viewDelegate;

        public ImageView(@NonNull Context context, @Nullable AttributeSet attrs,
                         ImageViewDelegate delegate) {
            super(context, attrs);
            this.viewDelegate = delegate;
        }

        @Override
        public void setVisibility(int visibility) {
            viewDelegate.getImageLayout().setVisibility(visibility);
        }

        public void setRadius(float radius) {
            viewDelegate.getImageLayout().setRadius(radius);
        }

        public void setRatio(int width, int height, boolean referToWidth) {
            viewDelegate.getImageLayout().setRatio(width, height, referToWidth);
        }
    }

}
