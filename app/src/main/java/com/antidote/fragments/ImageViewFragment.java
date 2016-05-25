package com.antidote.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.antidote.R;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageViewFragment extends Fragment {

    private String image = "";
    private String title = "";

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();

        image = getArguments().getString("image");
        title = getArguments().getString("title");
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        StaticFunction.overrideFontsMedium(getActivity(), view);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        LinearLayout lnlIndicator = (LinearLayout) view.findViewById(R.id.lnlIndicator);
        TextView txtTitleBanner = (TextView) view.findViewById(R.id.txtTitleBanner);
//        Log.e("image", StaticFunction.URL + image + " banner");
        imageLoader.displayImage(StaticFunction.URL + image, imageView, options);
        txtTitleBanner.setText(title);

        int size = getArguments().getInt("size");
        int position = getArguments().getInt("position");

        for (int i = 0; i < size; i++) {
            LinearLayout lnl = new LinearLayout(getActivity());
            if (i == position) {
                lnl.setBackgroundResource(R.drawable.circle_blue);
            } else {
                lnl.setBackgroundResource(R.drawable.circle_white);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(2, 2, 2, 2);
            lnl.setLayoutParams(layoutParams);
            lnlIndicator.addView(lnl);
        }

        return view;
    }
}
