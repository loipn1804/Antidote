package com.antidote.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import sg.antidote.R;
import com.antidote.adapters.OrderHistoryAdapter;
import com.antidote.daocontroller.BannerController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.OrderHistoryController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectBanner;
import antidote.ObjectCart;
import antidote.ObjectCategory;
import antidote.ObjectOrderHistory;
import antidote.ObjectUser;

/**
 * Created by USER on 6/24/2015.
 */
public class OrderHistoryActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private ListView lvOrderHistory;
    private List<ObjectOrderHistory> listOrderHistory;
    private OrderHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_ORDER_HISTORY);
            registerReceiver(activityReceiver, intentFilter);
        }

        listOrderHistory = new ArrayList<ObjectOrderHistory>();

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        lvOrderHistory = (ListView) findViewById(R.id.lvOrderHistory);

        rltLeft.setOnClickListener(this);
    }

    private void initData() {
        List<ObjectOrderHistory> list = OrderHistoryController.getAllOrderHistories(OrderHistoryActivity.this);
        for (ObjectOrderHistory orderHistory : list) {
            listOrderHistory.add(orderHistory);
        }

        historyAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, listOrderHistory);
        lvOrderHistory.setAdapter(historyAdapter);

        ObjectUser user = UserController.getCurrentUser(OrderHistoryActivity.this);
        if (user != null) {
            Intent intentGetOrderHistory = new Intent(AntidoteService.ACTION_GET_ORDER_HISTORY, null, OrderHistoryActivity.this, AntidoteService.class);
            intentGetOrderHistory.putExtra(AntidoteService.EXTRA_ORD_HIS_USER_ID, user.getId() + "");
            startService(intentGetOrderHistory);
            if (list.size() == 0) {
                showProgressDialog();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_ORDER_HISTORY)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listOrderHistory.clear();
                    List<ObjectOrderHistory> list = OrderHistoryController.getAllOrderHistories(OrderHistoryActivity.this);
                    for (ObjectOrderHistory orderHistory : list) {
                        listOrderHistory.add(orderHistory);
                    }
                    historyAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            }
        }
    };
}
