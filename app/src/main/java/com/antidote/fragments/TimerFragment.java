package com.antidote.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.activities.BaseActivity;
import com.antidote.adapters.TimerAdapter;
import com.antidote.adapters.WheelDateAdapter;
import com.antidote.adapters.WheelTimeAdapter;
import com.antidote.daocontroller.GroupProductController;
import com.antidote.daocontroller.GroupTimerController;
import com.antidote.daocontroller.ProductController;
import com.antidote.service.AntidoteTimerService;
import com.antidote.staticfunction.StaticFunction;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import antidote.ObjectGroupProduct;
import antidote.ObjectGroupTimer;
import antidote.ObjectProduct;
import kankan.wheel.widget.WheelView;

/**
 * Created by USER on 6/18/2015.
 */
public class TimerFragment extends Fragment implements View.OnClickListener {

    private ListView parallaxListView;

    private LayoutInflater layoutInflater;

    private View headerView;
    private LinearLayout lnlStartTime;
    private ImageView imvBannerTimer;
    private TextView txtNameGroup;
    private RelativeLayout rltBannerTimer;

    private View headerViewStartTime;
    private LinearLayout lnlCancelTime;
    private TextView txtDayOfTotal;
    private TextView txtCountDownTimer;
    private ImageView imvCurrent;
    private ImageView imvNext;
    private TextView txtNameGroupStartTime;
    private TextView txtCurrent;
    private TextView txtNext;
    private TextView txtIngredient;
    private TextView txtDesc;
    private TextView txtViewmore;
    private boolean isShowFull = false;

    private TimerAdapter timerAdapter;

    private Integer idGroupProduct;
    private List<ObjectGroupProduct> listGroupProduct;
    private List<ObjectProduct> listProduct;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private CountDownTimer timer;

    private boolean isHaveTimer;

    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.e("timer_fragment", System.currentTimeMillis() + "");
        idGroupProduct = getArguments().getInt("id_group");
        listGroupProduct = GroupProductController.getGroupProductByIdGroup(getActivity(), idGroupProduct);
        listProduct = new ArrayList<ObjectProduct>();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisk(true).build();

        isHaveTimer = false;

        isLoading = false;

        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        parallaxListView = (ListView) view.findViewById(R.id.parallaxTimer);

        layoutInflater = LayoutInflater.from(getActivity());

        headerView = layoutInflater.inflate(R.layout.header_timer, null);
        StaticFunction.overrideFontsLight(getActivity(), headerView);
        StaticFunction.overrideFontsMedium(getActivity(), headerView.findViewById(R.id.txtTitleIngredient));
        lnlStartTime = (LinearLayout) headerView.findViewById(R.id.lnlStartTime);
        imvBannerTimer = (ImageView) headerView.findViewById(R.id.imvBannerTimer);
        rltBannerTimer = (RelativeLayout) headerView.findViewById(R.id.rltBannerTimer);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltBannerTimer.getLayoutParams();
        params.height = StaticFunction.getScreenWidth(getActivity());
        rltBannerTimer.setLayoutParams(params);
        txtNameGroup = (TextView) headerView.findViewById(R.id.txtNameGroup);
        txtIngredient = (TextView) headerView.findViewById(R.id.txtIngredient);
        txtDesc = (TextView) headerView.findViewById(R.id.txtDesc);
        txtViewmore = (TextView) headerView.findViewById(R.id.txtViewmore);

        headerViewStartTime = layoutInflater.inflate(R.layout.header_timer_start_time, null);
        StaticFunction.overrideFontsLight(getActivity(), headerViewStartTime);
        lnlCancelTime = (LinearLayout) headerViewStartTime.findViewById(R.id.lnlCancelTime);
        txtDayOfTotal = (TextView) headerViewStartTime.findViewById(R.id.txtDayOfTotal);
        txtCountDownTimer = (TextView) headerViewStartTime.findViewById(R.id.txtCountDownTimer);
        imvCurrent = (ImageView) headerViewStartTime.findViewById(R.id.imvCurrent);
        imvNext = (ImageView) headerViewStartTime.findViewById(R.id.imvNext);
        txtNameGroupStartTime = (TextView) headerViewStartTime.findViewById(R.id.txtNameGroup);
        txtCurrent = (TextView) headerViewStartTime.findViewById(R.id.txtCurrent);
        txtNext = (TextView) headerViewStartTime.findViewById(R.id.txtNext);

        lnlStartTime.setOnClickListener(this);
        lnlCancelTime.setOnClickListener(this);

        LinearLayout lnlIndicator = (LinearLayout) headerView.findViewById(R.id.lnlIndicator);
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
            layoutParams.setMargins(4, 4, 4, 4);
            lnl.setLayoutParams(layoutParams);
            lnlIndicator.addView(lnl);
        }

        initDataHeader();
        initDataHeaderStartTimer();
        initDataList();

        if (isHaveTimer) {
            parallaxListView.removeHeaderView(headerView);
            parallaxListView.addHeaderView(headerViewStartTime);
        }
        parallaxListView.setOnScrollListener(new EndScrollListener());

        SpannableString content = new SpannableString(txtViewmore.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtViewmore.setText(content);
    }

    private void initDataHeader() {
        if (listGroupProduct.size() > 0) {
            Log.e("image", StaticFunction.URL + listGroupProduct.get(0).getImage() + " banner timer");
            imageLoader.displayImage(StaticFunction.URL + listGroupProduct.get(0).getImage(), imvBannerTimer, options);
            txtNameGroup.setText(listGroupProduct.get(0).getName());
            txtIngredient.setText(listGroupProduct.get(0).getIngredient());
            txtDesc.setText(listGroupProduct.get(0).getDescription());
            txtViewmore.setOnClickListener(this);
            if (listGroupProduct.get(0).getDescription().length() == 0) {
                txtViewmore.setVisibility(View.GONE);
            }
        }

        parallaxListView.addHeaderView(headerView);
    }

    private void initDataHeaderStartTimer() {
        if (listGroupProduct.size() > 0) {
            long current_time = System.currentTimeMillis();
            ObjectGroupTimer groupTimerCurrent = GroupTimerController.getCurrentGroupTimerByTime(getActivity(), listGroupProduct.get(0).getGroupID(), current_time);
            if (groupTimerCurrent != null) {
                imageLoader.displayImage(StaticFunction.URL + ProductController.getObjectProductById(getActivity(), groupTimerCurrent.getProductID()).getImage(), imvCurrent, options);
                txtCurrent.setVisibility(View.VISIBLE);
                imvCurrent.setVisibility(View.VISIBLE);
                isHaveTimer = true;
            } else {
                txtCurrent.setVisibility(View.INVISIBLE);
                imvCurrent.setVisibility(View.INVISIBLE);
                isHaveTimer = false;
            }
            ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(getActivity(), listGroupProduct.get(0).getGroupID(), current_time);
            if (groupTimerNext != null) {
                imageLoader.displayImage(StaticFunction.URL + ProductController.getObjectProductById(getActivity(), groupTimerNext.getProductID()).getImage(), imvNext, options);
                txtNext.setVisibility(View.VISIBLE);
                imvNext.setVisibility(View.VISIBLE);
                isHaveTimer = true;
                setTimerCountDown(groupTimerNext.getTime() - current_time);
            } else {
                txtNext.setVisibility(View.INVISIBLE);
                imvNext.setVisibility(View.INVISIBLE);
//                isHaveTimer = false;
                txtCountDownTimer.setText("00:00:00");
            }
            txtNameGroupStartTime.setText(listGroupProduct.get(0).getName());
            int num = GroupTimerController.getNumCurrentGroupTimerByTime(getActivity(), listGroupProduct.get(0).getGroupID(), current_time);
            num--;
            int day = (num / listGroupProduct.size()) + 1;
            txtDayOfTotal.setText("DAY " + day + " OF " + listGroupProduct.get(0).getRepeat_day() + " ");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (listGroupProduct.size() > 0) {
//            long current_time = System.currentTimeMillis();
//            ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(getActivity(), listGroupProduct.get(0).getGroupID(), current_time);
//            if (groupTimerNext == null) {
////                ((BaseActivity) getActivity()).showToast("no timer");
//                if (listGroupProduct.size() > 0) {
//                    GroupTimerController.clearByIdGroup(getActivity(), listGroupProduct.get(0).getGroupID());
////                    Intent intentStartAlarm = new Intent(getActivity(), AntidoteTimerService.class);
////                    intentStartAlarm.setAction(AntidoteTimerService.ACTION_CANCEL_ALARM);
////                    intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, listGroupProduct.get(0).getGroupID());
////                    getActivity().startService(intentStartAlarm);
//                }
////                parallaxListView.removeHeaderView(headerViewStartTime);
////                parallaxListView.addHeaderView(headerView);
////                initDataList();
//            }
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (listGroupProduct.size() > 0) {
            long current_time = System.currentTimeMillis();
            ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(getActivity(), listGroupProduct.get(0).getGroupID(), current_time);
            if (groupTimerNext == null) {
//                ((BaseActivity) getActivity()).showToast("no timer");
                if (listGroupProduct.size() > 0) {
                    GroupTimerController.clearByIdGroup(getActivity(), listGroupProduct.get(0).getGroupID());
//                    Intent intentStartAlarm = new Intent(getActivity(), AntidoteTimerService.class);
//                    intentStartAlarm.setAction(AntidoteTimerService.ACTION_CANCEL_ALARM);
//                    intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, listGroupProduct.get(0).getGroupID());
//                    getActivity().startService(intentStartAlarm);
                }
//                parallaxListView.removeHeaderView(headerViewStartTime);
//                parallaxListView.addHeaderView(headerView);
//                initDataList();
            }
        }
    }

    private void initDataList() {
        if (timerAdapter == null) {
            timerAdapter = new TimerAdapter(getActivity(), listProduct);
            parallaxListView.setAdapter(timerAdapter);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    timerAdapter.myNotifyDataSetChanged(listProduct);
                    timerAdapter.notifyDataSetChanged();
                }
            }, 500);
        }

        listProduct.clear();
        if (listGroupProduct.size() > 0) {
            for (ObjectGroupProduct groupProduct : listGroupProduct) {
                ObjectProduct objectProduct = ProductController.getObjectProductById(getActivity(), groupProduct.getProductID());
                if (objectProduct != null) {
                    listProduct.add(objectProduct);
                }
            }
        }
        long currentId = 0;
        if (listGroupProduct.size() > 0) {
            long current_time = System.currentTimeMillis();
            ObjectGroupTimer groupTimerCurrent = GroupTimerController.getCurrentGroupTimerByTime(getActivity(), listGroupProduct.get(0).getGroupID(), current_time);
            if (groupTimerCurrent != null) {
                currentId = groupTimerCurrent.getProductID();
            }
        }
        timerAdapter.setCurrentId(currentId);
        timerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnlStartTime:
//                parallaxListView.removeHeaderView(headerView);
//                parallaxListView.addParallaxedHeaderView(headerViewStartTime);
                showPopupChooseStartTimer();
                break;
            case R.id.lnlCancelTime:
                showPopupConfirmCancelTimer();
                break;
            case R.id.txtViewmore:
                if (!isShowFull) {
                    txtDesc.setMaxLines(100);
                    txtViewmore.setText("Show less");
                    isShowFull = true;
                    SpannableString content = new SpannableString(txtViewmore.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    txtViewmore.setText(content);
                } else {
                    txtDesc.setMaxLines(2);
                    txtViewmore.setText("Show more");
                    isShowFull = false;
                    SpannableString content = new SpannableString(txtViewmore.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    txtViewmore.setText(content);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void setTimerCountDown(long time) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Log.e("loi", (millisUntilFinished/1000) + "");
                int h = (int) millisUntilFinished / (3600000);
                h = h < 0 ? 0 : h;
                int m = (int) (millisUntilFinished - h * 3600000) / 60000;
                m = m < 0 ? 0 : m;
                int s = (int) (millisUntilFinished - h * 3600000 - m * 60000) / 1000;
                s = s < 0 ? 0 : s;

                String hour = h + "";
                if (h < 10) {
                    hour = "0" + hour;
                }
                String min = m + "";
                if (m < 10) {
                    min = "0" + min;
                }
                String sec = s + "";
                if (s < 10) {
                    sec = "0" + sec;
                }
                txtCountDownTimer.setText(hour + ":" + min + ":" + sec);
            }

            @Override
            public void onFinish() {
//                Log.e("loi", "Done");
                txtCountDownTimer.setText("00:00:00");
                initDataHeaderStartTimer();
                initDataList();
            }
        };
        timer.start();
    }

    public void showPopupChooseStartTimer() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_choose_start_timer);

        StaticFunction.overrideFontsLight(getActivity(), dialog.findViewById(R.id.root));

        final int daysCount = 20;
        List<String> listDay = new ArrayList<String>();
        List<String> listDateMonth = new ArrayList<String>();
        final List<String> listDateSend = new ArrayList<String>();

        final int timesCount = 24;
        List<String> listTime = new ArrayList<String>();
        final List<String> listTimeSend = new ArrayList<String>();

        WheelDateAdapter dateAdapter;
        WheelTimeAdapter timeAdapter;

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText("Please choose time to start timer.");
        final WheelView wheelDate = (WheelView) dialog.findViewById(R.id.whlDate);
        final WheelView wheelTime = (WheelView) dialog.findViewById(R.id.whlTime);

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

        dateAdapter = new WheelDateAdapter(getActivity(), listDay, listDateMonth);
        wheelDate.setVisibleItems(5); // Number of items
        wheelDate.setWheelBackground(R.drawable.wheel_bg);
        wheelDate.setWheelForeground(R.drawable.wheel_fg);
        wheelDate.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheelDate.setViewAdapter(dateAdapter);
        wheelDate.setCurrentItem(0);

        timeAdapter = new WheelTimeAdapter(getActivity(), listTime);
        wheelTime.setVisibleItems(5); // Number of items
        wheelTime.setWheelBackground(R.drawable.wheel_bg);
        wheelTime.setWheelForeground(R.drawable.wheel_fg);
        wheelTime.setShadowColor(0xFFffffff, 0x99ffffff, 0x33ffffff);
        wheelTime.setViewAdapter(timeAdapter);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        wheelTime.setCurrentItem(hour);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "";
                int timer_first = 0;
                if (listGroupProduct.size() > 0) {
                    timer_first = listGroupProduct.get(0).getTimer();
                }
                if ((wheelTime.getCurrentItem() + timer_first) > hour) {
                    s = listDateSend.get(wheelDate.getCurrentItem()) + " " + listTimeSend.get(wheelTime.getCurrentItem());
                } else {
                    s = listDateSend.get(wheelDate.getCurrentItem() + 1) + " " + listTimeSend.get(wheelTime.getCurrentItem());
                }
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Log.d("datetime", s);
                try {
                    Date mDate = sdf.parse(s);
                    long timeInMilliseconds = mDate.getTime();
//                    Toast.makeText(getActivity(), timeInMilliseconds + "", Toast.LENGTH_SHORT).show();
//                    Log.d("datetime", timeInMilliseconds + "");

                    startTimer(timeInMilliseconds);
                    initDataHeaderStartTimer();
                    parallaxListView.removeHeaderView(headerView);
                    parallaxListView.addHeaderView(headerViewStartTime);

                    if (listGroupProduct.size() > 0) {
                        Intent intentStartAlarm = new Intent(getActivity(), AntidoteTimerService.class);
                        intentStartAlarm.setAction(AntidoteTimerService.ACTION_START_ALARM);
                        intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, listGroupProduct.get(0).getGroupID());
                        getActivity().startService(intentStartAlarm);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void startTimer(long timeFirst) {
//        long current_time = System.currentTimeMillis();
        if (listGroupProduct.size() > 0) {
            GroupTimerController.clearByIdGroup(getActivity(), listGroupProduct.get(0).getGroupID());
            for (int i = 0; i < listGroupProduct.get(0).getRepeat_day(); i++) {
                for (ObjectGroupProduct groupProduct : listGroupProduct) {
                    ObjectGroupTimer objectGroupTimer = new ObjectGroupTimer();
                    objectGroupTimer.setGroupID(idGroupProduct);
                    objectGroupTimer.setProductID(groupProduct.getProductID());
                    objectGroupTimer.setTime(timeFirst + groupProduct.getTimer() * 3600000 + i * 24 * 3600000);
//                    objectGroupTimer.setTime(current_time + groupProduct.getTimer() * 5000 + i * 60000);
                    GroupTimerController.insert(getActivity(), objectGroupTimer);
                }
            }
        }
    }

    private void showPopupConfirmCancelTimer() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        StaticFunction.overrideFontsLight(getActivity(), dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMsg.setText("Are you sure?");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listGroupProduct.size() > 0) {
                    GroupTimerController.clearByIdGroup(getActivity(), listGroupProduct.get(0).getGroupID());
                    Intent intentStartAlarm = new Intent(getActivity(), AntidoteTimerService.class);
                    intentStartAlarm.setAction(AntidoteTimerService.ACTION_CANCEL_ALARM);
                    intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, listGroupProduct.get(0).getGroupID());
                    getActivity().startService(intentStartAlarm);

                    Intent intentCancelNoti = new Intent(getActivity(), AntidoteTimerService.class);
                    intentCancelNoti.setAction(AntidoteTimerService.ACTION_CANCEL_NOTIFICATION);
                    intentCancelNoti.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, listGroupProduct.get(0).getGroupID());
                    getActivity().startService(intentCancelNoti);
                }
                parallaxListView.removeHeaderView(headerViewStartTime);
                parallaxListView.addHeaderView(headerView);
                initDataList();
                dialog.dismiss();
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            ((BaseActivity) getActivity()).showToast("end");
            if (totalItemCount >= visibleItemCount) {
                if (firstVisibleItem + 2 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading) {
//                        Toast.makeText(getActivity(), "end scroll timer", Toast.LENGTH_SHORT).show();
                        isLoading = true;
                        parallaxListView.setOnScrollListener(null);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                timerAdapter.notifyDataSetChanged();
                            }
                        }, 100);
                    }
                }
            }
        }
    }
}
