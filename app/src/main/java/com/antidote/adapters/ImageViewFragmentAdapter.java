package com.antidote.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.antidote.fragments.ImageViewFragment;

import java.util.List;

import antidote.ObjectBanner;


public class ImageViewFragmentAdapter extends FragmentPagerAdapter {

    Activity activity;
    List<ObjectBanner> listBanner;

    public ImageViewFragmentAdapter(FragmentManager fragmentManager, Activity activity, List<ObjectBanner> listBanner) {
        super(fragmentManager);
        this.listBanner = listBanner;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putString("image", listBanner.get(i).getImage());
        bundle.putString("title", listBanner.get(i).getTitle());
        bundle.putInt("size", listBanner.size());
        bundle.putInt("position", i);
        ImageViewFragment imageViewFragment = new ImageViewFragment();
        imageViewFragment.setArguments(bundle);
        return imageViewFragment;
    }

    @Override
    public int getCount() {

        return listBanner.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {

        super.finishUpdate(container);
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return super.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {

        return super.saveState();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {

        super.startUpdate(container);
    }

}
