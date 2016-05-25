package com.antidote.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.antidote.staticfunction.StaticFunction;

/**
 * Created by USER on 10/8/2015.
 */
public class MyCheckedTextView extends CheckedTextView {

    public MyCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(StaticFunction.light(context));
    }
}
