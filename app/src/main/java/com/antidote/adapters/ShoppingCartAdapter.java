package com.antidote.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sg.antidote.R;
import com.antidote.daocontroller.ProductController;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.List;

import antidote.ObjectCart;

/**
 * Created by USER on 5/20/2015.
 */
public class ShoppingCartAdapter extends BaseAdapter {

    public interface ShoppingCartAdapterCallback {
        public void deleteCartById(int position);
    }

    private ShoppingCartAdapterCallback adapterCallback;

    private List<ObjectCart> listCart;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private List<Boolean> isDelete;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private Typeface typefaceMedium;
    private Typeface typefaceLight;

    public ShoppingCartAdapter(Activity activity, List<ObjectCart> listCart, List<Boolean> isDelete, ShoppingCartAdapterCallback adapterCallback) {
        this.listCart = listCart;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.isDelete = isDelete;
        this.adapterCallback = adapterCallback;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();

        typefaceMedium = StaticFunction.medium(activity);
        typefaceLight = StaticFunction.light(activity);
    }

    @Override
    public int getCount() {
        int count = (listCart == null) ? 0 : listCart.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listCart.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_shopping_cart, null);
            holder = new ViewHolder();
            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            holder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
//            holder.imvDelete = (ImageView) convertView.findViewById(R.id.imvDelete);
            holder.rltDelete = (RelativeLayout) convertView.findViewById(R.id.rltDelete);
            holder.txtDelete = (TextView) convertView.findViewById(R.id.txtDelete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setTypeface(typefaceLight);
        holder.txtPrice.setTypeface(typefaceLight);
        holder.txtQty.setTypeface(typefaceLight);
        holder.txtTotal.setTypeface(typefaceMedium);
        holder.txtDelete.setTypeface(typefaceLight);

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adapterCallback.deleteCartById(position);
//            }
//        });

        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.deleteCartById(position);
            }
        });

        imageLoader.displayImage(StaticFunction.URL + ProductController.getImageProductById(activity, listCart.get(position).getProductID()), holder.imvProduct, options);
        holder.txtName.setText(listCart.get(position).getProductName());
        float price = 0f;
        if (listCart.get(position).getSalePrice() == 0) {
            price = listCart.get(position).getRegularPrice();
        } else {
            price = listCart.get(position).getSalePrice();
        }
        BigDecimal bigPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtPrice.setText("S$" + bigPrice);
        holder.txtQty.setText("Quantity: " + listCart.get(position).getQuantity());
        BigDecimal bigTotal = new BigDecimal(price * listCart.get(position).getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtTotal.setText("S$" + bigTotal);

        if (isDelete.get(position)) {
//            holder.imvDelete.setVisibility(View.VISIBLE);
            holder.rltDelete.setVisibility(View.VISIBLE);
        } else {
//            holder.imvDelete.setVisibility(View.GONE);
            holder.rltDelete.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        ImageView imvProduct;
        TextView txtName;
        TextView txtPrice;
        TextView txtQty;
        TextView txtTotal;
        //        ImageView imvDelete;
        RelativeLayout rltDelete;
        TextView txtDelete;
    }
}
