package i.farmer.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author guodx
 * @created-time 2021/10/9 11:34 上午
 * @description 约束比例
 */
public class RatioImageView extends AppCompatImageView {
    private int ratioWidth = -1;
    private int ratioHeight = -1;
    private boolean referToWidth = true;      // 看是根据宽度为基准计算高度，还是以高度为基准计算宽度

    public RatioImageView(@NonNull Context context) {
        this(context, null);
    }

    public RatioImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (null != attrs) {
            TypedArray a = null;
            try {
                a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
                ratioWidth = a.getInteger(R.styleable.RatioImageView_ratioWidth, ratioWidth);
                ratioHeight = a.getInteger(R.styleable.RatioImageView_ratioHeight, ratioHeight);
                referToWidth = a.getInt(R.styleable.RatioImageView_referTo, 0) == 0;
            } catch (Exception e) {

            } finally {
                if (null != a) {
                    a.recycle();
                }
            }
        }
    }

    public void setRatio(int width, int height, boolean referToWidth) {
        this.ratioWidth = width;
        this.ratioHeight = height;
        this.referToWidth = referToWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratioWidth > 0 && ratioHeight > 0) {
            int width;
            int height;
            if (referToWidth) {
                width = getMeasuredWidth();
                height = (int) (width * ratioHeight * 1.f / ratioWidth);
            } else {
                height = getMeasuredHeight();
                width = (int) (height * ratioWidth * 1.f / ratioHeight);
            }
            setMeasuredDimension(width, height);
        }
    }
}