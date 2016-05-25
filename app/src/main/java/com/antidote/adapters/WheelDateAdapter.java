package com.antidote.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antidote.staticfunction.StaticFunction;

import java.util.List;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import sg.antidote.R;

/**
 * Created by USER on 4/17/2015.
 */
public class WheelDateAdapter extends AbstractWheelTextAdapter {

    private List<String> listDay;
    private List<String> listDateMonth;
    private Typeface typefaceMedium;

    public WheelDateAdapter(Context context, List<String> listDay, List<String> listDateMonth) {
        super(context, R.layout.row_wheel_date, NO_RESOURCE);

        this.listDay = listDay;
        this.listDateMonth = listDateMonth;

        typefaceMedium = StaticFunction.medium(context);

        setItemTextResource(R.id.txtWheelDay);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {

        View view = super.getItem(index, cachedView, parent);
        TextView txtDay = (TextView) view.findViewById(R.id.txtWheelDay);
        TextView txtDate = (TextView) view.findViewById(R.id.txtWheelDate);

        txtDay.setTypeface(typefaceMedium);
        txtDate.setTypeface(typefaceMedium);

        txtDay.setText(listDay.get(index));
        txtDate.setText(listDateMonth.get(index));

        return view;
    }

    @Override
    public int getItemsCount() {
        return listDay.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return "";
    }

}
