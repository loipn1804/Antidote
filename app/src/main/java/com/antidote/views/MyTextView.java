package com.antidote.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.antidote.staticfunction.StaticFunction;

/**
 * Created by USER on 10/8/2015.
 */
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(StaticFunction.light(context));
    }
}
