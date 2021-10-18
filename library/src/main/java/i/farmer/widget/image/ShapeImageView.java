package i.farmer.widget.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import i.farmer.widget.imageview.R;

/**
 * @author guodx
 * @created-time 2021/10/15 10:04 上午
 * @description 带约束比例功能的圆角、圆形图片视图（支持部分圆角），可带over边框
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ShapeImageView extends AppCompatImageView {

    public static final int RECTANGLE = 0;      // 矩形
    public static final int CIRCLE = 1;         // 圆形

    private ImageViewImpl IMPL;
    // 约束比例
    private int ratioWidth = -1;
    private int ratioHeight = -1;
    private boolean ratioReferToWidth = true;   // 如果约束比例，参照宽 还是 高

    private ShapeImageView(@NonNull Context context,
                           int ratioWidth,
                           int ratioHeight,
                           boolean ratioReferToWidth,
                           @Shape int shape,
                           float radius,
                           float[] radiusArr,
                           Paint mBorderPaint) {
        super(context);
        this.ratioWidth = ratioWidth;
        this.ratioHeight = ratioHeight;
        this.ratioReferToWidth = ratioReferToWidth;

        init(shape, radius, radiusArr, mBorderPaint);
    }

    public ShapeImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int shape = RECTANGLE;
        float radius = 0.f;
        float[] radiusArr = null;
        Paint mBorderPaint = null;
        if (null != attrs) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
                // 约束比例
                ratioWidth = a.getInteger(R.styleable.ShapeImageView_ratioWidth, ratioWidth);
                ratioHeight = a.getInteger(R.styleable.ShapeImageView_ratioHeight, ratioHeight);
                ratioReferToWidth = a.getInt(R.styleable.ShapeImageView_ratioReferTo, 0) == 0;
                // 形状
                shape = a.getInteger(R.styleable.ShapeImageView_shape, shape);
                // 角度
                if (shape == RECTANGLE) {
                    radius = a.getDimension(R.styleable.ShapeImageView_android_radius, radius);
                    float topLeftRadius = a.getDimension(R.styleable.ShapeImageView_android_topLeftRadius, radius);
                    float topRightRadius = a.getDimension(R.styleable.ShapeImageView_android_topRightRadius, radius);
                    float bottomRightRadius = a.getDimension(R.styleable.ShapeImageView_android_bottomRightRadius, radius);
                    float bottomLeftRadius = a.getDimension(R.styleable.ShapeImageView_android_bottomLeftRadius, radius);
                    if (topLeftRadius == topRightRadius
                            && topLeftRadius == bottomRightRadius
                            && topLeftRadius == bottomLeftRadius
                    ) {
                        // 4个圆角一致
                        radius = topLeftRadius;
                    } else {
                        // 4个圆角不一致
                        radiusArr = new float[4];
                        radiusArr[Corner.TOP_LEFT] = topLeftRadius;
                        radiusArr[Corner.TOP_RIGHT] = topRightRadius;
                        radiusArr[Corner.BOTTOM_RIGHT] = bottomRightRadius;
                        radiusArr[Corner.BOTTOM_LEFT] = bottomLeftRadius;
                    }
                }
                // 边框
                float borderWidth = a.getDimension(R.styleable.ShapeImageView_borderWidth, 0.f);
                int borderColor = a.getColor(R.styleable.ShapeImageView_borderColor, 0);
                if (borderWidth > 0 && borderColor != 0) {
                    mBorderPaint = new Paint();
                    mBorderPaint.setAntiAlias(true);
                    mBorderPaint.setColor(borderColor);
                    mBorderPaint.setStyle(Paint.Style.STROKE);
                    mBorderPaint.setStrokeWidth(borderWidth);
                }
            } catch (Exception e) {

            } finally {
                if (null != a) {
                    a.recycle();
                }
            }
        }
        init(shape, radius, radiusArr, mBorderPaint);
    }

    /**
     * 初始化
     *
     * @param shape
     * @param radius
     * @param radiusArr
     * @param borderPaint
     */
    private void init(@Shape int shape, float radius, float[] radiusArr, Paint borderPaint) {
        if (shape == CIRCLE) {
            IMPL = new CircleImpl();
        } else {
            if (null == radiusArr) {
                IMPL = new RoundImpl();
            } else {
                IMPL = new RoundImpl2();
            }
        }
        IMPL.initialize(delegate, radius, radiusArr, borderPaint);
    }

    /**
     * 约束比例（圆形无效）
     *
     * @param width
     * @param height
     */
    public void setRatio(int width, int height) {
        this.ratioWidth = width;
        this.ratioHeight = height;
    }

    /**
     * 不做比例约束
     */
    public void clearRatio() {
        this.setRatio(-1, -1);
    }

    /**
     * 约束比例，参照标的
     *
     * @param referToWidth
     */
    public void setRatioReferTo(boolean referToWidth) {
        this.ratioReferToWidth = referToWidth;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!IMPL.draw(delegate, canvas)) {
            super.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        IMPL.onDraw(delegate, canvas);
    }

    private ImageViewDelegate delegate = new ImageViewDelegate() {
        @Override
        public ShapeImageView getView() {
            return ShapeImageView.this;
        }

        public void superDraw(Canvas canvas) {
            ShapeImageView.super.draw(canvas);
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (IMPL.isCircle()) {
            // 圆形
            ratioWidth = ratioHeight = 1;   // 约束比例
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratioWidth > 0 && ratioHeight > 0) {
            int width;
            int height;
            if (ratioReferToWidth) {
                // 固定宽
                width = getMeasuredWidth();
                height = (int) (width * ratioHeight * 1.f / ratioWidth);
            } else {
                // 固定高
                height = getMeasuredHeight();
                width = (int) (height * ratioWidth * 1.f / ratioHeight);
            }
            setMeasuredDimension(width, height);
        }
    }

    public static class Builder {
        private Context context;
        private int ratioWidth = -1;
        private int ratioHeight = -1;
        private boolean referToWidth = true;    // 如果约束比例，参照宽 还是 高
        private @Shape
        int shape = RECTANGLE;                  // 形状
        private float radius = 0.f;             // 圆角
        private float[] radiusArr = null;
        private Paint mBorderPaint = null;      // 边框

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 约束比例（圆形无效）
         *
         * @param width
         * @param height
         */
        public void setRatio(int width, int height) {
            this.ratioWidth = width;
            this.ratioHeight = height;
        }

        /**
         * 约束比例，参照标的
         *
         * @param referToWidth
         */
        public void setRatioReferTo(boolean referToWidth) {
            this.referToWidth = referToWidth;
        }

        /**
         * 形状
         *
         * @param shape
         * @return
         */
        public Builder setShape(int shape) {
            this.shape = shape;
            return this;
        }

        /**
         * 圆角矩形（圆形无效）
         *
         * @param radius
         * @return
         */
        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        /**
         * 圆角矩形（圆形无效）
         *
         * @param id
         * @return
         */
        public Builder setRadiusRes(@DimenRes int id) {
            this.radius = context.getResources().getDimension(id);
            return this;
        }

        /**
         * 不同的圆角
         *
         * @param topLeftRadius
         * @param topRightRadius
         * @param bottomRightRadius
         * @param bottomLeftRadius
         * @return
         */
        public Builder setRadius(float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
            this.radiusArr = new float[4];
            this.radiusArr[Corner.TOP_LEFT] = topLeftRadius;
            this.radiusArr[Corner.TOP_RIGHT] = topRightRadius;
            this.radiusArr[Corner.BOTTOM_RIGHT] = bottomRightRadius;
            this.radiusArr[Corner.BOTTOM_LEFT] = bottomLeftRadius;
            return this;
        }

        /**
         * 不同的圆角
         *
         * @param topLeftId
         * @param topRightId
         * @param bottomRightId
         * @param bottomLeftId
         * @return
         */
        public Builder setRadiusRes(@DimenRes int topLeftId,
                                    @DimenRes int topRightId,
                                    @DimenRes int bottomRightId,
                                    @DimenRes int bottomLeftId) {
            this.radiusArr = new float[4];
            this.radiusArr[Corner.TOP_LEFT] = context.getResources().getDimension(topLeftId);
            this.radiusArr[Corner.TOP_RIGHT] = context.getResources().getDimension(topRightId);
            this.radiusArr[Corner.BOTTOM_RIGHT] = context.getResources().getDimension(bottomRightId);
            this.radiusArr[Corner.BOTTOM_LEFT] = context.getResources().getDimension(bottomLeftId);
            return this;
        }

        /**
         * 边框
         *
         * @param width 宽度
         * @param color 颜色
         * @return
         */
        public Builder setBorder(float width, @ColorInt int color) {
            this.mBorderPaint = new Paint();
            this.mBorderPaint.setAntiAlias(true);
            this.mBorderPaint.setColor(color);
            this.mBorderPaint.setStyle(Paint.Style.STROKE);
            this.mBorderPaint.setStrokeWidth(width);
            return this;
        }

        /**
         * 边框
         *
         * @param width 宽度
         * @param color 颜色
         * @return
         */
        public Builder setBorderRes(@DimenRes int width, @ColorRes int color) {
            this.mBorderPaint = new Paint();
            this.mBorderPaint.setAntiAlias(true);
            this.mBorderPaint.setColor(context.getResources().getColor(color));
            this.mBorderPaint.setStyle(Paint.Style.STROKE);
            this.mBorderPaint.setStrokeWidth(context.getResources().getDimension(width));
            return this;
        }

        public ShapeImageView build() {
            return new ShapeImageView(context,
                    ratioWidth,
                    ratioHeight,
                    referToWidth,
                    shape,
                    radius,
                    radiusArr,
                    mBorderPaint);
        }
    }
}
