package com.antidote.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.antidote.R;

import com.antidote.activities.BaseActivity;
import com.antidote.activities.CategoryActivity;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import antidote.ObjectCategory;
import antidote.ObjectProduct;

/**
 * Created by USER on 5/14/2015.
 */
public class HomeContentAdapter extends BaseAdapter {

    private List<ObjectCategory> listCategory;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private Typeface typefaceMedium;
    private Typeface typefaceLight;

    public HomeContentAdapter(Activity activity, List<ObjectCategory> listCategory) {
        this.listCategory = listCategory;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();

        typefaceMedium = StaticFunction.medium(activity);
        typefaceLight = StaticFunction.light(activity);
    }

    @Override
    public int getCount() {
        int count = (listCategory == null) ? 0 : listCategory.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_home_content, null);
            holder = new ViewHolder();
            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setTypeface(typefaceMedium);
        holder.txtDesc.setTypeface(typefaceLight);

        holder.txtName.setText(listCategory.get(position).getName());
        holder.txtDesc.setText(listCategory.get(position).getShortDescription());

        int maxline = holder.txtDesc.getMeasuredHeight() / holder.txtDesc.getLineHeight();
//        Log.e("line_height", holder.txtDesc.getMeasuredHeight() + " " + holder.txtDesc.getLineHeight() + " " + maxline);
        if (maxline > 0) {
            holder.txtDesc.setMaxLines(maxline);
        } else {

        }

        imageLoader.displayImage(StaticFunction.URL + listCategory.get(position).getThumbnail(), holder.imvProduct, options);

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCate = new Intent(activity, CategoryActivity.class);
                intentCate.putExtra("id_cate", listCategory.get(position).getId());
                activity.startActivity(intentCate);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        ImageView imvProduct;
        TextView txtName;
        TextView txtDesc;
    }
}
