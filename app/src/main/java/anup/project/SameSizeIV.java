package anup.project;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SameSizeIV extends ImageView {
    public SameSizeIV(Context context) {
        super(context);
    }

    public SameSizeIV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SameSizeIV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}