package com.antidote.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.antidote.daocontroller.GroupTimerController;

import java.util.List;

import antidote.ObjectGroupTimer;

/**
 * Created by USER on 6/23/2015.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Boot complete.", Toast.LENGTH_LONG).show();
        List<ObjectGroupTimer> list = GroupTimerController.getAllGroupTimers(context);
        for (ObjectGroupTimer groupTimer : list) {
            long current_time = System.currentTimeMillis();
            ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(context, groupTimer.getGroupID(), current_time);
            if (groupTimerNext != null) {
                Intent intentStartAlarm = new Intent(context, AntidoteTimerService.class);
                intentStartAlarm.setAction(AntidoteTimerService.ACTION_START_ALARM);
                intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, groupTimer.getGroupID());
                context.startService(intentStartAlarm);
            }
        }
    }
}