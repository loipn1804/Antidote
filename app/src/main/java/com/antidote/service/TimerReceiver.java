package com.antidote.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by USER on 6/19/2015.
 */
public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("timer", "receiver");
        Intent intentSendNoti = new Intent(context, AntidoteTimerService.class);
        intentSendNoti.setAction(AntidoteTimerService.ACTION_SEND_NOTIFICATION);
        intentSendNoti.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, intent.getIntExtra(AntidoteTimerService.EXTRA_GROUP_ID, 0));
        intentSendNoti.putExtra(AntidoteTimerService.EXTRA_PRODUCT_NAME, intent.getStringExtra(AntidoteTimerService.EXTRA_PRODUCT_NAME));
        context.startService(intentSendNoti);

        Intent intentStartAlarm = new Intent(context, AntidoteTimerService.class);
        intentStartAlarm.setAction(AntidoteTimerService.ACTION_START_ALARM);
        intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, intent.getIntExtra(AntidoteTimerService.EXTRA_GROUP_ID, 0));
        context.startService(intentStartAlarm);
    }
}
