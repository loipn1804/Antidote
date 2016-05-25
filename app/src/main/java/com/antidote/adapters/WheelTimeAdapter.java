package com.antidote.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antidote.staticfunction.StaticFunction;

import sg.antidote.R;

import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by USER on 4/17/2015.
 */
public class WheelTimeAdapter extends AbstractWheelTextAdapter {


    private List<String> listTime;
    private Typeface typefaceLight;

    public WheelTimeAdapter(Context context, List<String> listTime) {
        super(context, R.layout.row_wheel_time, NO_RESOURCE);
        this.listTime = listTime;
        typefaceLight = StaticFunction.light(context);
        setItemTextResource(R.id.txtTime);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        TextView txtTime = (TextView) view.findViewById(R.id.txtTime);
        txtTime.setTypeface(typefaceLight);
        txtTime.setText(listTime.get(index));

        return view;
    }

    @Override
    public int getItemsCount() {
        return listTime.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }

}
