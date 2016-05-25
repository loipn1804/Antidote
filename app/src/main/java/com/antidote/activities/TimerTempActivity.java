package com.antidote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import sg.antidote.R;
import com.antidote.adapters.ImageViewFragmentAdapter;
import com.antidote.adapters.TimerAdapter;
import com.antidote.daocontroller.BannerController;
import com.antidote.service.AntidoteService;
import com.antidote.service.AntidoteTimerService;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectBanner;

/**
 * Created by USER on 5/20/2015.
 */
public class TimerTempActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_temp);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        Intent intentTimer = new Intent(TimerTempActivity.this, TimerActivity.class);
        intentTimer.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intentTimer.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, getIntent().getStringExtra(AntidoteTimerService.EXTRA_GROUP_ID));
        startActivity(intentTimer);
        finish();
    }
}
