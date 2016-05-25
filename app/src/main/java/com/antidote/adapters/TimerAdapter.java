package com.antidote.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sg.antidote.R;

import com.antidote.activities.DetailActivity;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectProduct;

/**
 * Created by USER on 5/20/2015.
 */
public class TimerAdapter extends BaseAdapter {

    private List<ObjectProduct> listProduct;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private long currentId;
    private int nextPosition;

    private Typeface typefaceMedium;
    private Typeface typefaceLight;

    public TimerAdapter(Activity activity, List<ObjectProduct> listProduct) {
//        this.listProduct = new ArrayList<>();
//        this.listProduct.addAll(listProduct);
        this.listProduct = listProduct;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();
        currentId = 0;
        nextPosition = nextPosition();

        typefaceMedium = StaticFunction.medium(activity);
        typefaceLight = StaticFunction.light(activity);
    }

    public void myNotifyDataSetChanged(List<ObjectProduct> listProduct) {
        this.listProduct.clear();
        listProduct.addAll(listProduct);
        notifyDataSetChanged();
    }

    public void setCurrentId(long currentId) {
        this.currentId = currentId;
        nextPosition = nextPosition();
    }

    private int nextPosition() {
        int next = 0;
        for (int i = 0; i < listProduct.size(); i++) {
            if (currentId == listProduct.get(i).getId()) {
                next = i;
                break;
            }
        }
        return next;
    }

    @Override
    public int getCount() {
        int count = (listProduct == null) ? 0 : listProduct.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_timer, null);
            holder = new ViewHolder();
            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            holder.lnlContent = (LinearLayout) convertView.findViewById(R.id.lnlContent);
            holder.rltCover = (RelativeLayout) convertView.findViewById(R.id.rltCover);
            holder.imvAlarm = (ImageView) convertView.findViewById(R.id.imvAlarm);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setTypeface(typefaceMedium);
        holder.txtDesc.setTypeface(typefaceLight);

        holder.txtName.setText(listProduct.get(position).getName());
        holder.txtDesc.setText(listProduct.get(position).getDescription());

        int maxline = holder.txtDesc.getMeasuredHeight() / holder.txtDesc.getLineHeight();
//        Log.e("line_height", holder.txtDesc.getMeasuredHeight() + " " + holder.txtDesc.getLineHeight() + " " + maxline);
        if (maxline > 0) {
            holder.txtDesc.setMaxLines(maxline);
        } else {

        }

        imageLoader.displayImage(StaticFunction.URL + listProduct.get(position).getImage(), holder.imvProduct, options);

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(activity, DetailActivity.class);
                intentDetail.putExtra("id_product", listProduct.get(position).getId());
                activity.startActivity(intentDetail);
            }
        });

        if (position < nextPosition) {
            holder.rltCover.setBackgroundColor(activity.getResources().getColor(R.color.gray_trans));
        } else {
            holder.rltCover.setBackgroundColor(activity.getResources().getColor(R.color.tranparent));
        }

        if (currentId == listProduct.get(position).getId()) {
//            holder.lnlContent.setBackgroundColor(activity.getResources().getColor(R.color.gray_btn));
//            holder.imvProduct.setBackgroundColor(activity.getResources().getColor(R.color.gray_btn));
            holder.imvAlarm.setVisibility(View.VISIBLE);
            AnimationDrawable frameAnimation = (AnimationDrawable)holder.imvAlarm.getDrawable();
//            frameAnimation.setCallback(holder.imvAlarm);
//            frameAnimation.setVisible(true, true);
            frameAnimation.start();
        } else {
//            holder.lnlContent.setBackgroundColor(activity.getResources().getColor(R.color.white));
//            holder.imvProduct.setBackgroundColor(activity.getResources().getColor(R.color.white));
            holder.imvAlarm.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        ImageView imvProduct;
        TextView txtName;
        TextView txtDesc;
        LinearLayout lnlContent;
        RelativeLayout rltCover;
        ImageView imvAlarm;
    }
}
