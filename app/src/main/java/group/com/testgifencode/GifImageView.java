package group.com.testgifencode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
* @author fanjh
* @date 2017/8/4 14:36
* @description 结合GifDecoder进行GIF的播放和暂停管理
**/
public class GifImageView extends ImageView {
    private GifDecoder gifDecoder;
    private Paint paint;
    private RectF rectF;
    private boolean isRunning;

    public GifImageView(Context context) {
        this(context, null);
    }

    public GifImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        rectF = new RectF();
    }

    public void start(GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
        isRunning = true;
        invalidate();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null != gifDecoder) {
            gifDecoder.advance();
            int delayTime = gifDecoder.getNextDelay();
            Bitmap bitmap = gifDecoder.getNextFrame();
            rectF.set(0, 0, getWidth(), getHeight());
            canvas.drawBitmap(bitmap, null, rectF, paint);
            if (isRunning) {
                postInvalidateDelayed(delayTime);
            }
        }
    }
}
