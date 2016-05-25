package com.antidote.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.activities.BaseActivity;
import com.antidote.activities.DetailActivity;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import antidote.ObjectProduct;

/**
 * Created by USER on 5/15/2015.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private List<String> listDataHeader;
    private HashMap<String, List<ObjectProduct>> listDataChild;
    private ViewHolderHeader holderHeader;
    private ViewHolderChild holderChild;
    private LayoutInflater layoutInflaterHeader;
    private LayoutInflater layoutInflaterChild;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private Typeface typefaceMedium;

    private int height;

    public ExpandableListAdapter(Activity activity, List<String> listDataHeader,
                                 HashMap<String, List<ObjectProduct>> listDataChild) {
        this.activity = activity;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.layoutInflaterHeader = LayoutInflater.from(activity);
        this.layoutInflaterChild = LayoutInflater.from(activity);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();

        typefaceMedium = StaticFunction.medium(activity);
        height = (StaticFunction.getScreenWidth(activity) - (int) (30 * StaticFunction.getDensity(activity))) / 2;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = listDataChild.get(listDataHeader.get(groupPosition)).size();
        if (size % 2 == 0) {
            return size / 2;
        } else {
            return size / 2 + 1;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflaterHeader.inflate(R.layout.row_shop_expan, null);
            holderHeader = new ViewHolderHeader();
            holderHeader.txtHeader = (TextView) convertView.findViewById(R.id.txtShopExpan);
            holderHeader.txtNothing = (TextView) convertView.findViewById(R.id.txtNothingToShow);
            holderHeader.imvExpand = (ImageView) convertView.findViewById(R.id.imvExpand);

            convertView.setTag(holderHeader);
        } else {
            holderHeader = (ViewHolderHeader) convertView.getTag();
        }

        holderHeader.txtHeader.setTypeface(typefaceMedium);
        holderHeader.txtNothing.setTypeface(typefaceMedium);

        holderHeader.txtHeader.setText((String) getGroup(groupPosition));

        if (isExpanded) {
//            ((BaseActivity)activity).showToast("expand: " + groupPosition);
            holderHeader.imvExpand.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_shop_close));
            if (listDataChild.get(listDataHeader.get(groupPosition)).size() > 0) {
                holderHeader.txtNothing.setVisibility(View.GONE);
            } else {
                holderHeader.txtNothing.setVisibility(View.VISIBLE);
            }
        } else {
//            ((BaseActivity)activity).showToast("collapse: " + groupPosition);
            holderHeader.imvExpand.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_shop_expand));
            holderHeader.txtNothing.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        boolean existSize;
        if (convertView == null) {
            convertView = this.layoutInflaterChild.inflate(R.layout.row_shop_child, null);
            holderChild = new ViewHolderChild();
            holderChild.lnlRow1 = (LinearLayout) convertView.findViewById(R.id.lnlRow1);
            holderChild.lnlRow2 = (LinearLayout) convertView.findViewById(R.id.lnlRow2);
            holderChild.imvProduct1 = (ImageView) convertView.findViewById(R.id.imvProduct1);
            holderChild.imvProduct2 = (ImageView) convertView.findViewById(R.id.imvProduct2);
            holderChild.txtName1 = (TextView) convertView.findViewById(R.id.txtName1);
            holderChild.txtName2 = (TextView) convertView.findViewById(R.id.txtName2);
            holderChild.txtPrice1 = (TextView) convertView.findViewById(R.id.txtPrice1);
            holderChild.txtPrice2 = (TextView) convertView.findViewById(R.id.txtPrice2);
            holderChild.rltImv1 = (RelativeLayout) convertView.findViewById(R.id.rltImv1);
            holderChild.rltImv2 = (RelativeLayout) convertView.findViewById(R.id.rltImv2);
            existSize = false;
            convertView.setTag(holderChild);
        } else {
            existSize = false;
            holderChild = (ViewHolderChild) convertView.getTag();
        }

        if ((childPosition * 2 + 1) >= listDataChild.get(listDataHeader.get(groupPosition)).size()) {
            existSize = true;
        }

        holderChild.txtName1.setTypeface(typefaceMedium);
        holderChild.txtName2.setTypeface(typefaceMedium);
        holderChild.txtPrice1.setTypeface(typefaceMedium);
        holderChild.txtPrice2.setTypeface(typefaceMedium);

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) holderChild.rltImv1.getLayoutParams();
        params1.height = height;
        holderChild.rltImv1.setLayoutParams(params1);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holderChild.rltImv2.getLayoutParams();
        params2.height = height;
        holderChild.rltImv2.setLayoutParams(params2);

        //// row 1
        holderChild.lnlRow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(activity, DetailActivity.class);
                intentDetail.putExtra("id_product", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2).getId());
                activity.startActivity(intentDetail);
            }
        });
        imageLoader.displayImage(StaticFunction.URL + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2).getImage(), holderChild.imvProduct1, options);
        holderChild.txtName1.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2).getName());
        holderChild.txtPrice1.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2).getPriceRange());

        //// row 2
        if (!existSize) {
            holderChild.lnlRow2.setVisibility(View.VISIBLE);
            holderChild.lnlRow2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetail = new Intent(activity, DetailActivity.class);
                    intentDetail.putExtra("id_product", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2 + 1).getId());
                    activity.startActivity(intentDetail);
                }
            });
            imageLoader.displayImage(StaticFunction.URL + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2 + 1).getImage(), holderChild.imvProduct2, options);
            holderChild.txtName2.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2 + 1).getName());
            holderChild.txtPrice2.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition * 2 + 1).getPriceRange());
        } else {
            holderChild.lnlRow2.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolderHeader {
        TextView txtHeader;
        TextView txtNothing;
        ImageView imvExpand;
    }

    public class ViewHolderChild {
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
