package com.antidote.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.antidote.fragments.ImageViewFragment;
import com.antidote.fragments.TimerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 6/18/2015.
 */
public class TimerFragmentAdapter extends FragmentPagerAdapter {

    Activity activity;
    List<Integer> listIdGroupProduct;

    public TimerFragmentAdapter(FragmentManager fragmentManager, Activity activity, List<Integer> listIdGroupProduct) {
        super(fragmentManager);
        this.listIdGroupProduct = new ArrayList<>();
        this.listIdGroupProduct.addAll(listIdGroupProduct);
        this.activity = activity;
    }

    public void setListData(List<Integer> listIdGroupProduct) {
        this.listIdGroupProduct.clear();
        this.listIdGroupProduct.addAll(listIdGroupProduct);
    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("id_group", listIdGroupProduct.get(i));
        bundle.putInt("size", listIdGroupProduct.size());
        bundle.putInt("position", i);
        TimerFragment timerFragment = new TimerFragment();
        timerFragment.setArguments(bundle);
        return timerFragment;
    }

    @Override
    public int getCount() {

        return listIdGroupProduct.size();
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
