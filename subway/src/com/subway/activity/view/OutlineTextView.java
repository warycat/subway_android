package com.subway.activity.view;

import com.subway.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Join;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class OutlineTextView extends TextView {
	
	private float borderSize;
	private int strokeColor;
	private int textColor;
	private int smallCharacters;

	private float defaultTextSize;

	private Paint mTextPaint;
	private TextPaint mTextPaintOutline;
	private String mText;

	private int mAscent = 0;

	public OutlineTextView(Context context) {
		super(context);
		initTextViewOutline();
	}

	public OutlineTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public OutlineTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	public void update() {
		invalidate();
	}

	private void setCustomFont(Context ctx, AttributeSet attr) {
		mText = getText().toString();

		TypedArray a = ctx.obtainStyledAttributes(attr,
				R.styleable.CustomTextView);
		String customFont = a.getString(R.styleable.CustomTextView_customFont);
		borderSize = a.getDimension(R.styleable.CustomTextView_strokeWidth, 2);
		strokeColor = a.getColor(R.styleable.CustomTextView_strokeColor,
				Color.WHITE);
		textColor = a.getColor(R.styleable.CustomTextView_fillColor,
				Color.WHITE);
		smallCharacters = a.getInt(R.styleable.CustomTextView_smallCharacters,
				4);

		defaultTextSize = getPaint().getTextSize();

		setCustomFont(ctx, customFont);

		a.recycle();
	}

	private boolean setCustomFont(Context ctx, String asset) {
		boolean result = false;

		if (asset != null && !"".equals(asset)) {
			Typeface tf = null;
			try {
				tf = Typeface.createFromAsset(ctx.getAssets(), asset);
			} catch (Exception e) {
				Log.i("CustomTextView", e.getMessage());
			}

			if (tf != null)
				setTypeface(tf);
		}

		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				initTextViewOutline();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		initTextViewOutline();

		return result;
	}

	private void initTextViewOutline() {
		mText = getText().toString();
		float newTextSize = defaultTextSize;
		float density = getResources().getDisplayMetrics().density;
		int textLength = mText.length();

		if (textLength > 0 && smallCharacters > 0
				&& textLength > smallCharacters) {
			float oldSize = newTextSize / density;
			oldSize -= 4 * (textLength - smallCharacters);
			newTextSize = oldSize * density;
		}

		mTextPaint = new TextPaint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(newTextSize);
		mTextPaint.setColor(textColor);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setStrokeJoin(Join.ROUND);
		mTextPaint.setTypeface(getTypeface());

		mTextPaintOutline = new TextPaint();
		mTextPaintOutline.setAntiAlias(true);
		mTextPaintOutline.setTextSize(newTextSize);
		mTextPaintOutline.setColor(strokeColor);
		mTextPaintOutline.setStyle(Paint.Style.FILL_AND_STROKE);
		mTextPaintOutline.setStrokeWidth(borderSize);
		mTextPaintOutline.setStrokeJoin(Join.ROUND);
		mTextPaintOutline.setTypeface(getTypeface());

		setPadding(3, 3, 3, 3);
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		mText = getText().toString();
		canvas.drawText(mText, getPaddingLeft() + 2, getPaddingTop() - mAscent,
				mTextPaintOutline);
		canvas.drawText(mText, getPaddingLeft() + 2, getPaddingTop() - mAscent,
				mTextPaint);
		// super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
					+ getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}

		return result + 4;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		mAscent = (int) mTextPaint.ascent();
		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
					+ getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result + 4;
	}

}
