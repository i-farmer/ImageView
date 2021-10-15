package i.farmer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import i.farmer.widget.imageview.R;

/**
 * @author guodx
 * @created-time 2021/10/15 10:04 上午
 * @description 带约束比例功能的圆角、圆形图片视图（不支持部分圆角），可带over边框
 */
public class ShapeImageView extends AppCompatImageView {
    // 约束比例
    private int ratioWidth = -1;
    private int ratioHeight = -1;
    private boolean ratioReferToWidth = true;   // 如果约束比例，参照宽 还是 高
    // 形状
    public static final int RECTANGLE = 0;      // 矩形
    public static final int CIRCLE = 1;         // 圆形
    private @Shape
    int shape = RECTANGLE;                      // 形状
    private float radius = 0.f;                 // 圆角
    private @ColorInt
    int backgroundColor = 0;
    private @ColorInt
    int[] backgroundColors = null;
    private GradientDrawable.Orientation backgroundOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
    private Paint mBorderPaint = null;          // 边框
    private Drawable backgroundIcon = null;     // 图标

    /**
     * @hide
     */
    @IntDef({RECTANGLE, CIRCLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
    }

    private ShapeImageView(@NonNull Context context,
                           int ratioWidth,
                           int ratioHeight,
                           boolean ratioReferToWidth,
                           int shape,
                           float radius,
                           int backgroundColor,
                           int[] backgroundColors,
                           GradientDrawable.Orientation backgroundOrientation,
                           Paint mBorderPaint,
                           Drawable backgroundIcon) {
        super(context);
        this.ratioWidth = ratioWidth;
        this.ratioHeight = ratioHeight;
        this.ratioReferToWidth = ratioReferToWidth;
        this.shape = shape;
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        this.backgroundColors = backgroundColors;
        this.backgroundOrientation = backgroundOrientation;
        this.mBorderPaint = mBorderPaint;
        this.backgroundIcon = backgroundIcon;
    }

    public ShapeImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (null != attrs) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.ShapeImageView);
                // 约束比例
                ratioWidth = a.getInteger(R.styleable.ShapeImageView_ratioWidth, ratioWidth);
                ratioHeight = a.getInteger(R.styleable.ShapeImageView_ratioHeight, ratioHeight);
                ratioReferToWidth = a.getInt(R.styleable.ShapeImageView_ratioReferTo, 0) == 0;
                // 背景颜色
                backgroundColor = a.getColor(R.styleable.ShapeImageView_backgroundColor, backgroundColor);
                if (a.hasValue(R.styleable.ShapeImageView_backgroundStartColor)
                        && a.hasValue(R.styleable.ShapeImageView_backgroundEndColor)) {
                    int start = a.getColor(R.styleable.ShapeImageView_backgroundStartColor, 0);
                    int center = a.getColor(R.styleable.ShapeImageView_backgroundCenterColor, 0);
                    int end = a.getColor(R.styleable.ShapeImageView_backgroundEndColor, 0);
                    if (a.hasValue(R.styleable.ShapeImageView_backgroundCenterColor)) {
                        backgroundColors = new int[]{start, center, end};
                    } else {
                        backgroundColors = new int[]{start, end};
                    }
                }
                int type = a.getInt(R.styleable.ShapeImageView_backgroundOrientation, 6);
                backgroundOrientation = getBackgroundOrientation(type);
                // 形状
                shape = a.getInteger(R.styleable.ShapeImageView_shape, shape);
                // 角度
                radius = a.getDimension(R.styleable.ShapeImageView_android_radius, radius);
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
                // 背景图标
                backgroundIcon = a.getDrawable(R.styleable.ShapeImageView_backgroundIcon);
            } catch (Exception e) {

            } finally {
                if (null != a) {
                    a.recycle();
                }
            }
        }
        setBackground();
    }

    private GradientDrawable.Orientation getBackgroundOrientation(int type) {
        if (type == 0) {
            return GradientDrawable.Orientation.TOP_BOTTOM;
        }
        if (type == 1) {
            return GradientDrawable.Orientation.TR_BL;
        }
        if (type == 2) {
            return GradientDrawable.Orientation.RIGHT_LEFT;
        }
        if (type == 3) {
            return GradientDrawable.Orientation.BR_TL;
        }
        if (type == 4) {
            return GradientDrawable.Orientation.BOTTOM_TOP;
        }
        if (type == 5) {
            return GradientDrawable.Orientation.BL_TR;
        }
        if (type == 7) {
            return GradientDrawable.Orientation.TL_BR;
        }
        return GradientDrawable.Orientation.LEFT_RIGHT;
    }

    private void setBackground() {
        setClipToOutline(true);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(shape == CIRCLE ? GradientDrawable.OVAL : GradientDrawable.RECTANGLE);
        if (null != backgroundColors) {
            drawable.setColors(backgroundColors);
            drawable.setOrientation(backgroundOrientation);
        } else {
            drawable.setColor(backgroundColor);
        }
        if (shape == RECTANGLE && radius > 0) {
            // 圆角
            drawable.setCornerRadius(radius);
        }
        if (null == backgroundIcon) {
            setBackgroundDrawable(drawable);
        } else {
            // 增加icon
            LayerDrawable layer = new LayerDrawable(new Drawable[]{drawable, backgroundIcon});
            setBackground(layer);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != mBorderPaint) {
            // 边框
            Rect rect = canvas.getClipBounds();
            RectF f = new RectF();
            f.set(rect);
            float inset = mBorderPaint.getStrokeWidth() / 2.f;
            f.inset(inset, inset);  // 解决边框粗细的问题
            if (shape == CIRCLE) {
                canvas.drawOval(f, mBorderPaint);
            } else {
                canvas.drawRoundRect(f, radius - inset, radius - inset, mBorderPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (shape == CIRCLE) {
            // 圆形
            ratioWidth = ratioHeight = 1;   // 约束比例
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratioWidth > 0 && ratioHeight > 0) {
            int width;
            int height;
            if (ratioReferToWidth) {
                width = getMeasuredWidth();
                height = (int) (width * ratioHeight * 1.f / ratioWidth);
            } else {
                height = getMeasuredHeight();
                width = (int) (height * ratioWidth * 1.f / ratioHeight);
            }
            setMeasuredDimension(width, height);
        }
    }

    public class Builder {
        private Context context;
        private int ratioWidth = -1;
        private int ratioHeight = -1;
        private boolean referToWidth = true;      // 如果约束比例，参照宽 还是 高
        private @Shape
        int shape = RECTANGLE;                  // 形状
        private float radius = 0.f;             // 圆角
        private @ColorInt
        int backgroundColor = 0;
        private @ColorInt
        int[] backgroundColors = null;
        private GradientDrawable.Orientation backgroundOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
        private Paint mBorderPaint = null;      // 边框
        private Drawable backgroundIcon = null; // 图标

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
         * 背景色（单色）
         *
         * @param color
         * @return
         */
        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        /**
         * 背景色（单色）
         *
         * @param id
         * @return
         */
        public Builder setBackgroundColorRes(@ColorRes int id) {
            return setBackgroundColor(context.getResources().getColor(id));
        }

        /**
         * 背景色（渐变）
         *
         * @param colors
         * @return
         */
        public Builder setBackgroundColors(@ColorInt int[] colors) {
            this.backgroundColors = colors;
            return this;
        }

        /**
         * 背景色（渐变）
         *
         * @param ids
         * @return
         */
        public Builder setBackgroundColorsRes(@ColorRes int[] ids) {
            if (null != ids) {
                this.backgroundColors = new int[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    this.backgroundColors[i] = context.getResources().getColor(ids[i]);
                }
            }
            return this;
        }

        /**
         * 背景色 渐变方向
         *
         * @param backgroundOrientation
         * @return
         */
        public Builder setBackgroundOrientation(GradientDrawable.Orientation backgroundOrientation) {
            this.backgroundOrientation = backgroundOrientation;
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

        /**
         * 背景 图标
         *
         * @param backgroundIcon
         * @return
         */
        public Builder setBackgroundIcon(Drawable backgroundIcon) {
            this.backgroundIcon = backgroundIcon;
            return this;
        }

        public ShapeImageView build() {
            return new ShapeImageView(context,
                    ratioWidth,
                    ratioHeight,
                    referToWidth,
                    shape,
                    radius,
                    backgroundColor,
                    backgroundColors,
                    backgroundOrientation,
                    mBorderPaint,
                    backgroundIcon);
        }
    }
}
