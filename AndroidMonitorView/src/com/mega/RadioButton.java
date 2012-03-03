package com.mega;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class RadioButton extends android.widget.RadioButton 
{
	private String mValue;

	 public RadioButton(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	}

	public String getValue() {
	return this.mValue;
	}

	 public void setValue(String value) {
	this.mValue = value;
	if(value.toUpperCase()=="TRUE")
	{
		this.setChecked(true);
	}
	else
	{
		this.setChecked(false);		
	}
	}
	public RadioButton(Context context, AttributeSet attrs) {
	super(context, attrs);
	try {
	/**
	* ��values/attrs.xml���涨������԰�
	*/
	  TypedArray a = context.obtainStyledAttributes(attrs,
	R.styleable.RadioButton);
	this.mValue = a.getString(R.styleable.RadioButton_value);
	
	if(mValue.toUpperCase().equals("TRUE"))
	{
		this.setChecked(true);
	}
	else
	{
		this.setChecked(false);		
	}
	
	a.recycle();
	} catch (Exception e) {
	e.printStackTrace();
	}

	}

	 public RadioButton(Context context) {
	super(context);
	}

	 
}
