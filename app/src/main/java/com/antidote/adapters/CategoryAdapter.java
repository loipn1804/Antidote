package com.antidote.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.math.BigDecimal;
import java.util.List;

import antidote.ObjectCategory;
import antidote.ObjectProduct;

/**
 * Created by user on 5/30/2015.
 */
public class CategoryAdapter extends BaseAdapter {

    private List<ObjectProduct> listProduct;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private Typeface typefaceMedium;

    private int height;

    public CategoryAdapter(Activity activity, List<ObjectProduct> listProduct) {
        this.listProduct = listProduct;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();

        typefaceMedium = StaticFunction.medium(activity);
        height = (StaticFunction.getScreenWidth(activity) - (int) (30 * StaticFunction.getDensity(activity))) / 2;
    }

    @Override
    public int getCount() {
        int size = listProduct.size();
        if (size % 2 == 0) {
            return size / 2;
        } else {
            return size / 2 + 1;
        }
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
        boolean existSize;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_shop_child, null);
            holder = new ViewHolder();
            holder.lnlRow1 = (LinearLayout) convertView.findViewById(R.id.lnlRow1);
            holder.lnlRow2 = (LinearLayout) convertView.findViewById(R.id.lnlRow2);
            holder.imvProduct1 = (ImageView) convertView.findViewById(R.id.imvProduct1);
            holder.imvProduct2 = (ImageView) convertView.findViewById(R.id.imvProduct2);
            holder.txtName1 = (TextView) convertView.findViewById(R.id.txtName1);
            holder.txtName2 = (TextView) convertView.findViewById(R.id.txtName2);
            holder.txtPrice1 = (TextView) convertView.findViewById(R.id.txtPrice1);
            holder.txtPrice2 = (TextView) convertView.findViewById(R.id.txtPrice2);
            holder.rltImv1 = (RelativeLayout) convertView.findViewById(R.id.rltImv1);
            holder.rltImv2 = (RelativeLayout) convertView.findViewById(R.id.rltImv2);
            existSize = false;

            convertView.setTag(holder);
        } else {
            existSize = false;
            holder = (ViewHolder) convertView.getTag();
        }
        if ((position * 2 + 1) >= listProduct.size()) {
            existSize = true;
        }

        holder.txtName1.setTypeface(typefaceMedium);
        holder.txtName2.setTypeface(typefaceMedium);
        holder.txtPrice1.setTypeface(typefaceMedium);
        holder.txtPrice2.setTypeface(typefaceMedium);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holder.rltImv1.getLayoutParams();
        params1.height = height;
        holder.rltImv1.setLayoutParams(params1);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder.rltImv2.getLayoutParams();
        params2.height = height;
        holder.rltImv2.setLayoutParams(params2);

        //// row 1
        holder.lnlRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(activity, DetailActivity.class);
                intentDetail.putExtra("id_product", listProduct.get(position * 2).getId());
                activity.startActivity(intentDetail);
            }
        });
//        Log.e("image", StaticFunction.URL + listProduct.get(position * 2).getImage() + " adapter");
        imageLoader.displayImage(StaticFunction.URL + listProduct.get(position * 2).getImage(), holder.imvProduct1, options);
        holder.txtName1.setText(listProduct.get(position * 2).getName());
        holder.txtPrice1.setText(listProduct.get(position * 2).getPriceRange());

        //// row 2
        if (!existSize) {
            holder.lnlRow2.setVisibility(View.VISIBLE);
            holder.lnlRow2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetail = new Intent(activity, DetailActivity.class);
                    intentDetail.putExtra("id_product", listProduct.get(position * 2 + 1).getId());
                    activity.startActivity(intentDetail);
                }
            });
//            Log.e("image", StaticFunction.URL + listProduct.get(position * 2 + 1).getImage() + " adapter");
            imageLoader.displayImage(StaticFunction.URL + listProduct.get(position * 2 + 1).getImage(), holder.imvProduct2, options);
            holder.txtName2.setText(listProduct.get(position * 2 + 1).getName());
            holder.txtPrice2.setText(listProduct.get(position * 2 + 1).getPriceRange());
        } else {
            holder.lnlRow2.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRow1;
        LinearLayout lnlRow2;
        ImageView imvProduct1;
        ImageView imvProduct2;
        TextView txtName1;
        TextView txtName2;
        TextView txtPrice1;
        TextView txtPrice2;
        RelativeLayout rltImv1;
        RelativeLayout rltImv2;
    }

}
