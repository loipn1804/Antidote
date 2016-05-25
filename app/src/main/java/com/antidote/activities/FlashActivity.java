package com.antidote.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import sg.antidote.R;
import com.antidote.daocontroller.BannerController;
import com.antidote.daocontroller.CountryController;
import com.antidote.daocontroller.GroupTimerController;
import com.antidote.service.AntidoteService;
import com.antidote.service.AntidoteServiceParal;
import com.antidote.service.AntidoteTimerService;
import com.antidote.staticfunction.StaticFunction;

import java.util.List;

import antidote.ObjectGroupTimer;

/**
 * Created by USER on 6/26/2015.
 */
public class FlashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_activity);

        if (!StaticFunction.isNetworkAvailable(FlashActivity.this)) {
            showPopupConfirmExitWithoutConnection();
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentHome = new Intent(FlashActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CountryController.getAllCountries(FlashActivity.this).size() == 0) {
            Intent intentGetCountry = new Intent(AntidoteService.ACTION_GET_ALL_COUNTRY, null, FlashActivity.this, AntidoteService.class);
            startService(intentGetCountry);
        }

        if (BannerController.getAllBanners(FlashActivity.this).size() == 0) {
            Intent intentGetBanner = new Intent(AntidoteService.ACTION_GET_BANNER, null, FlashActivity.this, AntidoteService.class);
            startService(intentGetBanner);
        }

        Intent intentGetConfig = new Intent(AntidoteServiceParal.ACTION_GET_CONFIG, null, FlashActivity.this, AntidoteServiceParal.class);
        startService(intentGetConfig);

        Intent intentGetAllProduct = new Intent(AntidoteServiceParal.ACTION_GET_ALL_PRODUCT, null, FlashActivity.this, AntidoteServiceParal.class);
        startService(intentGetAllProduct);

        Intent intentGetOption = new Intent(AntidoteServiceParal.ACTION_GET_OPTION, null, FlashActivity.this, AntidoteServiceParal.class);
        startService(intentGetOption);

        Intent intentGetProductOption = new Intent(AntidoteServiceParal.ACTION_GET_PRODUCT_OPTION, null, FlashActivity.this, AntidoteServiceParal.class);
        startService(intentGetProductOption);

        List<ObjectGroupTimer> list = GroupTimerController.getAllGroupTimers(FlashActivity.this);
        for (ObjectGroupTimer groupTimer : list) {
            long current_time = System.currentTimeMillis();
            ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(FlashActivity.this, groupTimer.getGroupID(), current_time);
            if (groupTimerNext != null) {
                Intent intentStartAlarm = new Intent(FlashActivity.this, AntidoteTimerService.class);
                intentStartAlarm.setAction(AntidoteTimerService.ACTION_START_ALARM);
                intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, groupTimer.getGroupID());
                startService(intentStartAlarm);
            }
        }
    }

    private void showPopupConfirmExitWithoutConnection() {
        // custom dialog
        final Dialog dialog = new Dialog(FlashActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_prompt);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMsg.setText("Internet access is required to access app. Please enable data.");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
