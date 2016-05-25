package com.antidote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import sg.antidote.R;
import com.antidote.adapters.WheelDateAdapter;
import com.antidote.adapters.WheelTimeAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 5/21/2015.
 */
public class DeliveryTimeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private LinearLayout lnlContinue;

    private WheelView wheelDate;
    private WheelDateAdapter dateAdapter;
    private WheelView wheelTime;
    private WheelTimeAdapter timeAdapter;

    private final int daysCount = 20;

    private List<String> listDay;
    private List<String> listDateMonth;
    private List<String> listDateSend;

    private final int timesCount = 24;
    private List<String> listTime;
    private List<String> listTimeSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_time);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        initView();

        listDay = new ArrayList<String>();
        listDateMonth = new ArrayList<String>();
        listDateSend = new ArrayList<String>();
        listTime = new ArrayList<String>();
        listTimeSend = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < daysCount; i++) {

            calendar.roll(Calendar.DAY_OF_YEAR, i);

            DateFormat fmDay = new SimpleDateFormat("EEE");
            listDay.add(fmDay.format(calendar.getTime()).toUpperCase());

            DateFormat fmDate = new SimpleDateFormat("dd MMM");
            listDateMonth.add(fmDate.format(calendar.getTime()).toUpperCase());

            DateFormat fmDateSend = new SimpleDateFormat("yyyy-MM-dd");
            listDateSend.add(fmDateSend.format(calendar.getTime()));
        }

        for (int i = 0; i < timesCount; i++) {
            listTime.add(i + " : 00");
            listTimeSend.add(i + ":00:00");
        }

        // set current time

        dateAdapter = new WheelDateAdapter(DeliveryTimeActivity.this, listDay, listDateMonth);
        wheelDate.setVisibleItems(5); // Number of items
        wheelDate.setWheelBackground(R.drawable.wheel_bg);
        wheelDate.setWheelForeground(R.drawable.wheel_fg);
        wheelDate.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheelDate.setViewAdapter(dateAdapter);
        wheelDate.setCurrentItem(0);

        timeAdapter = new WheelTimeAdapter(DeliveryTimeActivity.this, listTime);
        wheelTime.setVisibleItems(5); // Number of items
        wheelTime.setWheelBackground(R.drawable.wheel_bg);
        wheelTime.setWheelForeground(R.drawable.wheel_fg);
        wheelTime.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheelTime.setViewAdapter(timeAdapter);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        wheelTime.setCurrentItem(hour);
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        lnlContinue = (LinearLayout) findViewById(R.id.lnlContinue);
        wheelDate = (WheelView) findViewById(R.id.whlDate);
        wheelTime = (WheelView) findViewById(R.id.whlTime);

        rltLeft.setOnClickListener(this);
        lnlContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.lnlContinue:
                String s = listDateSend.get(wheelDate.getCurrentItem()) + " " + listTimeSend.get(wheelTime.getCurrentItem());
                Toast.makeText(DeliveryTimeActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intentSummary = new Intent(DeliveryTimeActivity.this, OrderSummaryActivity.class);
                startActivity(intentSummary);
                break;
        }
    }
}
