package net.mitchtech.ioio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {

	private Bitmap mBitmap;
	private Paint mPaint = new Paint();
	private Canvas mCanvas = new Canvas();

	private float mSpeed = 1.0f;
	private float mLastX;
	private float mScale;
	private float mLastValue;
	private float mYOffset;
	private int mColor;
	private float mWidth;
	private float maxValue = 1024f;

	public GraphView(Context context) {
		super(context);
		init();
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mColor = Color.argb(192, 64, 128, 64);
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	public void addDataPoint(float value) {
		final Paint paint = mPaint;
		float newX = mLastX + mSpeed;
		final float v = mYOffset + value * mScale;

		paint.setColor(mColor);
		mCanvas.drawLine(mLastX, mLastValue, newX, v, paint);
		mLastValue = v;
		mLastX += mSpeed;

		invalidate();
	}

	public void setMaxValue(int max) {
		maxValue = max;
		mScale = -(mYOffset * (1.0f / maxValue));
	}

	public void setSpeed(float speed) {
		mSpeed = speed;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		mCanvas.setBitmap(mBitmap);
		mCanvas.drawColor(0xFFFFFFFF);
		mYOffset = h;
		mScale = -(mYOffset * (1.0f / maxValue));
		mWidth = w;
		mLastX = mWidth;
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		synchronized (this) {
			if (mBitmap != null) {
				if (mLastX >= mWidth) {
					mLastX = 0;
					final Canvas cavas = mCanvas;
					cavas.drawColor(0xFFFFFFFF);
					mPaint.setColor(0xFF777777);
					cavas.drawLine(0, mYOffset, mWidth, mYOffset, mPaint);
				}
				canvas.drawBitmap(mBitmap, 0, 0, null);
			}
		}
	}
}
