package com.antidote.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antidote.staticfunction.StaticFunction;

import java.util.List;

import sg.antidote.R;

/**
 * Created by USER on 10/8/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private List<String> objects;
    private Context context;
    private ViewHolder holder;
    private ViewHolderDrop holderDrop;
    private LayoutInflater layoutInflater;
    private int resource;

    private Typeface typefaceLight;

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        typefaceLight = StaticFunction.light(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(resource, null);
            holder = new ViewHolder();
            holder.txt1 = (TextView) convertView.findViewById(R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt1.setTypeface(typefaceLight);
        holder.txt1.setText(objects.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.spinner_dropdown_item, null);
            holderDrop = new ViewHolderDrop();
            holderDrop.txt1 = (CheckedTextView) convertView.findViewById(R.id.text1);

            convertView.setTag(holderDrop);
        } else {
            holderDrop = (ViewHolderDrop) convertView.getTag();
        }

        holderDrop.txt1.setTypeface(typefaceLight);
        holderDrop.txt1.setText(objects.get(position));

        return convertView;
    }

    public class ViewHolder {
        TextView txt1;
    }

    public class ViewHolderDrop {
        CheckedTextView txt1;
    }
}
