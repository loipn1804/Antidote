package com.antidote.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.antidote.R;
import com.antidote.daocontroller.ProductController;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectOrderHistory;

/**
 * Created by USER on 6/24/2015.
 */
public class OrderHistoryAdapter extends BaseAdapter {

    private List<ObjectOrderHistory> listOrderHistory;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    private Typeface typefaceMedium;
    private Typeface typefaceLight;

    public OrderHistoryAdapter(Activity activity, List<ObjectOrderHistory> listOrderHistory) {
        this.listOrderHistory = listOrderHistory;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;

        typefaceMedium = StaticFunction.medium(activity);
        typefaceLight = StaticFunction.light(activity);
    }

    @Override
    public int getCount() {
        int count = (listOrderHistory == null) ? 0 : listOrderHistory.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listOrderHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_order_history, null);
            holder = new ViewHolder();
            holder.lnlRoot = (LinearLayout) convertView.findViewById(R.id.lnlRoot);
            holder.txtYear = (TextView) convertView.findViewById(R.id.txtYear);
            holder.txtDay = (TextView) convertView.findViewById(R.id.txtDay);
            holder.txtMonth = (TextView) convertView.findViewById(R.id.txtMonth);
            holder.txtIdOrder = (TextView) convertView.findViewById(R.id.txtOrderNumber);
            holder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtIdOrder.setTypeface(typefaceMedium);
        holder.txtDay.setTypeface(typefaceMedium);
        holder.txtYear.setTypeface(typefaceLight);
        holder.txtMonth.setTypeface(typefaceLight);
        holder.txtTotal.setTypeface(typefaceLight);

        holder.lnlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(listOrderHistory.get(position).getCreateDate());
        }
        catch (java.text.ParseException e) {

        }

        if (date != null) {
            DateFormat fmYear = new SimpleDateFormat("yyyy");
            DateFormat fmDay = new SimpleDateFormat("dd");
            DateFormat fmMonth = new SimpleDateFormat("MMM");
            holder.txtYear.setText(fmYear.format(date));
            holder.txtDay.setText(fmDay.format(date));
            holder.txtMonth.setText(fmMonth.format(date));
        }

        holder.txtIdOrder.setText("Reservation No.: " + listOrderHistory.get(position).getId());
        BigDecimal bigTotal = new BigDecimal(listOrderHistory.get(position).getTotal()).setScale(2,BigDecimal.ROUND_HALF_UP);
        holder.txtTotal.setText("Total Amount: S$" + bigTotal);

        return convertView;
    }

    public class ViewHolder {
        LinearLayout lnlRoot;
        TextView txtYear;
        TextView txtDay;
        TextView txtMonth;
        TextView txtIdOrder;
        TextView txtTotal;
    }
}
