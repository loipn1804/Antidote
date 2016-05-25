package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;
import com.antidote.adapters.ImageViewFragmentAdapter;
import com.antidote.adapters.TimerAdapter;
import com.antidote.adapters.TimerFragmentAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.GroupProductController;
import com.antidote.daocontroller.GroupTimerController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.service.AntidoteTimerService;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import antidote.ObjectCart;
import antidote.ObjectCategory;
import antidote.ObjectGroupProduct;
import antidote.ObjectGroupTimer;

/**
 * Created by USER on 6/18/2015.
 */
public class TimerActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private ViewPager pager;
    //    private CirclePageIndicator indicator;
    private TimerFragmentAdapter adapter;
    private List<Integer> listIdGroup;
    private RelativeLayout rltBtnLeft;
    private RelativeLayout rltBtnRight;
    private RelativeLayout rltActionbarSearch;
    private RelativeLayout rltActionbarCart;
    private TextView txtNumCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_GROUP_PRODUCT);
            registerReceiver(activityReceiver, intentFilter);
        }

        Intent intentGetGroup = new Intent(AntidoteService.ACTION_GET_GROUP_PRODUCT, null, TimerActivity.this, AntidoteService.class);
        startService(intentGetGroup);

        listIdGroup = new ArrayList<Integer>();

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltBtnLeft = (RelativeLayout) findViewById(R.id.rltBtnLeft);
        rltBtnRight = (RelativeLayout) findViewById(R.id.rltBtnRight);
        rltActionbarSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltActionbarCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtNumCart = (TextView) findViewById(R.id.txtNumCart);

        rltLeft.setOnClickListener(this);
        rltBtnLeft.setOnClickListener(this);
        rltBtnRight.setOnClickListener(this);
        rltActionbarSearch.setOnClickListener(this);
        rltActionbarCart.setOnClickListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
//        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
    }

    private void initData() {
        List<ObjectGroupProduct> list = GroupProductController.getAllGroupProducts(TimerActivity.this);
        if (list.size() == 0) {
            showProgressDialog();
        }
        for (ObjectGroupProduct groupProduct : list) {
            boolean b = false;
            for (Integer i : listIdGroup) {
                if (i.equals(groupProduct.getGroupID())) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                listIdGroup.add(groupProduct.getGroupID());
            }
        }
//        showProgressDialog();
        adapter = new TimerFragmentAdapter(getSupportFragmentManager(), this, listIdGroup);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(listIdGroup.size());
//        indicator.setViewPager(pager);
        gotoTimerRunning();
    }

    private void gotoTimerRunning() {
        int idGroup = getIntent().getIntExtra(AntidoteTimerService.EXTRA_GROUP_ID, 0);
        if (idGroup > 0) {
            for (int i = 0; i < listIdGroup.size(); i++) {
                if (listIdGroup.get(i) == idGroup) {
                    pager.setCurrentItem(i);
                    break;
                }
            }
        } else {
            List<ObjectGroupTimer> listTimer = GroupTimerController.getAllGroupTimers(TimerActivity.this);
            if (listTimer.size() > 0) {
                idGroup = listTimer.get(0).getGroupID();
                if (idGroup > 0) {
                    for (int i = 0; i < listIdGroup.size(); i++) {
                        if (listIdGroup.get(i) == idGroup) {
                            pager.setCurrentItem(i);
                            break;
                        }
                    }
                } else {
                    pager.setCurrentItem(0);
                }
            } else {
                pager.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltBtnLeft:
                if (pager.getCurrentItem() > 0) {
                    pager.setCurrentItem(pager.getCurrentItem() - 1);
                }
                break;
            case R.id.rltBtnRight:
                if (pager.getCurrentItem() < pager.getChildCount() - 1) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                }
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(TimerActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
//                Toast.makeText(ShopActivity.this, "cart", Toast.LENGTH_SHORT).show();
//                if (UserController.isLogin(TimerActivity.this)) {
                    Intent intentShoppingCart = new Intent(TimerActivity.this, ShoppingCartActivity.class);
                    startActivity(intentShoppingCart);
//                } else {
//                    showPopupConfirmLogin();
//                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateNumCart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(TimerActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_login);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(TimerActivity.this, LoginActivity.class);
                startActivity(intentLogin);
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

    private void calculateNumCart() {
//        if (UserController.isLogin(TimerActivity.this)) {
            List<ObjectCart> list = CartController.getAllCarts(TimerActivity.this);
            if (list.size() == 0) {
                txtNumCart.setText("");
                txtNumCart.setVisibility(View.GONE);
            } else {
                txtNumCart.setText(list.size() + "");
                txtNumCart.setVisibility(View.VISIBLE);
            }
//        } else {
//            txtNumCart.setText("");
//            txtNumCart.setVisibility(View.GONE);
//        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_GROUP_PRODUCT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listIdGroup.clear();
                    List<ObjectGroupProduct> list = GroupProductController.getAllGroupProducts(TimerActivity.this);
                    for (ObjectGroupProduct groupProduct : list) {
                        boolean b = false;
                        for (Integer i : listIdGroup) {
                            if (i.equals(groupProduct.getGroupID())) {
                                b = true;
                                break;
                            }
                        }
                        if (!b) {
                            listIdGroup.add(groupProduct.getGroupID());
                        }
                    }
                    pager.setOffscreenPageLimit(listIdGroup.size());

                    adapter.setListData(listIdGroup);
                    adapter.notifyDataSetChanged();
                    pager.setOffscreenPageLimit(listIdGroup.size());

//                    gotoTimerRunning();

//                    pager.setAdapter(null);
//                    adapter = null;
//                    adapter = new TimerFragmentAdapter(getSupportFragmentManager(), TimerActivity.this, listIdGroup);
//                    pager.setAdapter(adapter);
//                    pager.setOffscreenPageLimit(listIdGroup.size());

                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            }
        }
    };
}
