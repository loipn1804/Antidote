package com.antidote.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import antidote.ObjectGroupProduct;
import sg.antidote.R;
import com.antidote.activities.TimerActivity;
import com.antidote.activities.TimerTempActivity;
import com.antidote.daocontroller.GroupProductController;
import com.antidote.daocontroller.GroupTimerController;
import com.antidote.daocontroller.ProductController;

import java.util.List;

import antidote.ObjectGroupTimer;

/**
 * Created by USER on 6/19/2015.
 */
public class AntidoteTimerService extends IntentService {

    // action
    public static final String ACTION_START_ALARM = "ACTION_START_ALARM";
    public static final String ACTION_CANCEL_ALARM = "ACTION_CANCEL_ALARM";
    public static final String ACTION_SEND_NOTIFICATION = "ACTION_SEND_NOTIFICATION";
    public static final String ACTION_CANCEL_NOTIFICATION = "ACTION_CANCEL_NOTIFICATION";

    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";
    public static final String EXTRA_PRODUCT_NAME = "EXTRA_PRODUCT_NAME";

    private NotificationManager notificationManager;

    public AntidoteTimerService() {
        super(AntidoteTimerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (action.equals(ACTION_START_ALARM)) {
            startAlarm(intent.getIntExtra(EXTRA_GROUP_ID, 0));
        } else if (action.equals(ACTION_CANCEL_ALARM)) {
            cancelAlarm(intent.getIntExtra(EXTRA_GROUP_ID, 0));
        } else if (action.equals(ACTION_SEND_NOTIFICATION)) {
            sendPushNotification(intent.getIntExtra(EXTRA_GROUP_ID, 0), intent.getStringExtra(EXTRA_PRODUCT_NAME));
        } else if (action.equals(ACTION_CANCEL_NOTIFICATION)) {
            cancelNotification(intent.getIntExtra(EXTRA_GROUP_ID, 0));
        }
    }

    private void startAlarm(int alarmCode_groupid) {
        cancelAlarm(alarmCode_groupid);

        long current_time = System.currentTimeMillis();
        ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(AntidoteTimerService.this, alarmCode_groupid, current_time);
        if (groupTimerNext != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(AntidoteTimerService.this, TimerReceiver.class);
            intent.putExtra(EXTRA_GROUP_ID, alarmCode_groupid);
            intent.putExtra(EXTRA_PRODUCT_NAME, ProductController.getObjectProductById(AntidoteTimerService.this, groupTimerNext.getProductID()).getName());

            PendingIntent intentExecuted = PendingIntent.getBroadcast(AntidoteTimerService.this, alarmCode_groupid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String deviceMan = android.os.Build.MANUFACTURER;
            if (deviceMan.equalsIgnoreCase("Xiaomi")) {
                alarmManager.setRepeating(AlarmManager.RTC, groupTimerNext.getTime(), 0, intentExecuted);
            } else {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, groupTimerNext.getTime(), 0, intentExecuted);
            }
        } else {
            cancelAlarm(alarmCode_groupid);
        }
    }

    private void cancelAlarm(int alarmCode_groupid) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(AntidoteTimerService.this, TimerReceiver.class);

        PendingIntent intentExecuted = PendingIntent.getBroadcast(AntidoteTimerService.this, alarmCode_groupid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(intentExecuted);
    }

    private void sendPushNotification(int alarmCode_groupid, String product_name) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notification
        Intent intent = new Intent(AntidoteTimerService.this, TimerTempActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.setAction(alarmCode_groupid + "");
        intent.setAction(System.currentTimeMillis() + "");
        intent.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, alarmCode_groupid);

        // new
        PendingIntent contentIntent = PendingIntent.getActivity(AntidoteTimerService.this, alarmCode_groupid, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String group_name = "Antidote";
        List<ObjectGroupProduct> productList = GroupProductController.getGroupProductByIdGroup(AntidoteTimerService.this, alarmCode_groupid);
        if (productList.size() > 0) {
            group_name = "Time for " + productList.get(0).getName() + ":";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AntidoteTimerService.this).setSmallIcon(R.drawable.ic_a).setContentTitle(group_name).setContentText(product_name);

        mBuilder.setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mBuilder.setVibrate(new long[]{500, 1000});

        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(alarmCode_groupid, mBuilder.build());
    }

    private void cancelNotification(int alarmCode_groupid) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(alarmCode_groupid);
    }
}
