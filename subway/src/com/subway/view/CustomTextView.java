package com.subway.view;

import com.subway.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextView extends TextView {
	private int smallCharacters; 
	private float defaultTextSize;
	
	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}


	public void updateTextSize()
	{
		String text = getText().toString();
		float newTextSize = defaultTextSize;
		float density = getResources().getDisplayMetrics().density;
		int textLength = text.length();
		float oldSize = newTextSize/density;
	
		if(textLength > 0 && smallCharacters > 0 && textLength > smallCharacters)
		{
			oldSize -= 4 *(textLength - smallCharacters);
			newTextSize = oldSize * density;
		}
		if(getPaint().getTextSize() != newTextSize)
		{
			setTextSize(oldSize);
		}
	}
	
	private void setCustomFont(Context ctx, AttributeSet attr) {
		TypedArray a = ctx.obtainStyledAttributes(attr,
				R.styleable.CustomTextView);
		
		String customFont = a.getString(R.styleable.CustomTextView_customFont);
		smallCharacters = a.getInt(R.styleable.CustomTextView_smallCharacters, 4);
		
		defaultTextSize = getPaint().getTextSize();
		setCustomFont(ctx, customFont);

		a.recycle();
	}

	private boolean setCustomFont(Context ctx, String asset) {
		boolean result = false;

		if (asset != null) {
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
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateTextSize();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		return result;
	}
	
}
