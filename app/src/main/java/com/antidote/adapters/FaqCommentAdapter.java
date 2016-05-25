package com.antidote.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sg.antidote.R;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import antidote.ObjectComment;
import antidote.ObjectFaqComment;

/**
 * Created by USER on 7/1/2015.
 */
public class FaqCommentAdapter extends BaseAdapter {

    private List<ObjectFaqComment> listFaqComment;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private Typeface typefaceMedium;
    private Typeface typefaceLight;

    public FaqCommentAdapter(Activity activity, List<ObjectFaqComment> listFaqComment) {
        this.listFaqComment = listFaqComment;
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer((int) (5 * StaticFunction.getDensity(activity))))
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.white)
                .cacheOnDisk(true).build();

        typefaceMedium = StaticFunction.medium(activity);
        typefaceLight = StaticFunction.light(activity);
    }

    @Override
    public int getCount() {
        int count = (listFaqComment == null) ? 0 : listFaqComment.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listFaqComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_detail_review, null);
            holder = new ViewHolder();
            holder.imvAvatar = (ImageView) convertView.findViewById(R.id.imvAvatar);
            holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtUsername.setTypeface(typefaceMedium);
        holder.txtDate.setTypeface(typefaceLight);
        holder.txtComment.setTypeface(typefaceLight);

        if (listFaqComment.get(position).getIdFacebook().length() == 0) {
            imageLoader.displayImage(StaticFunction.URL + listFaqComment.get(position).getImage(), holder.imvAvatar, options);
        } else {
            imageLoader.displayImage("https://graph.facebook.com/" + listFaqComment.get(position).getIdFacebook() + "/picture?height=200&width=200", holder.imvAvatar, options);
        }
        holder.txtComment.setText(listFaqComment.get(position).getComment());
        if ((listFaqComment.get(position).getFirstName().length() + listFaqComment.get(position).getLastName().length()) > 0) {
            holder.txtUsername.setText(listFaqComment.get(position).getFirstName() + " " + listFaqComment.get(position).getLastName());
        } else {
            holder.txtUsername.setText("No username");
        }
        holder.txtDate.setText(listFaqComment.get(position).getCreateDate().substring(0, 11));

        return convertView;
    }

    public class ViewHolder {
        ImageView imvAvatar;
        TextView txtUsername;
        TextView txtComment;
        TextView txtDate;
    }
}
