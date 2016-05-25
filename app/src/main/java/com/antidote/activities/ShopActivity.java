package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.adapters.ExpandableListAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.ProductController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectCategory;
import antidote.ObjectProduct;

/**
 * Created by USER on 5/15/2015.
 */
public class ShopActivity extends BaseActivity implements View.OnClickListener {

    private ExpandableListView exLv;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<ObjectProduct>> listDataChild;
    private RelativeLayout rltLeft;
    private RelativeLayout rltActionbarSearch;
    private RelativeLayout rltActionbarCart;
    private TextView txtNumCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_ALL_PRODUCT);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();

        prepareListData();

        listAdapter = new ExpandableListAdapter(ShopActivity.this, listDataHeader, listDataChild);
        exLv.setAdapter(listAdapter);

        for (int i = 0; i < listDataHeader.size(); i++) {
            exLv.expandGroup(i);
        }

        Intent intentGetProduct = new Intent(ShopActivity.this, AntidoteService.class);
        intentGetProduct.setAction(AntidoteService.ACTION_GET_ALL_PRODUCT);
        startService(intentGetProduct);
        List<ObjectProduct> listProduct = ProductController.getAllProducts(ShopActivity.this);
//        if (listProduct.size() == 0) {
//            showProgressDialog();
//        }
    }

    private void initView() {
        exLv = (ExpandableListView) findViewById(R.id.lvExpanList);
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltActionbarSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltActionbarCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtNumCart = (TextView) findViewById(R.id.txtNumCart);

        rltLeft.setOnClickListener(this);
        rltActionbarSearch.setOnClickListener(this);
        rltActionbarCart.setOnClickListener(this);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<ObjectProduct>>();

        List<ObjectCategory> listCategory = CategoryController.getAllCategories(ShopActivity.this);
        for (ObjectCategory category : listCategory) {
            listDataHeader.add(category.getName());
            List<ObjectProduct> listProduct = ProductController.getProductByIdCategory(ShopActivity.this, category.getId());
            listDataChild.put(category.getName(), listProduct);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(ShopActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
//                Toast.makeText(ShopActivity.this, "cart", Toast.LENGTH_SHORT).show();
//                if (UserController.isLogin(ShopActivity.this)) {
                Intent intentShoppingCart = new Intent(ShopActivity.this, ShoppingCartActivity.class);
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
        final Dialog dialog = new Dialog(ShopActivity.this);

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
                Intent intentLogin = new Intent(ShopActivity.this, LoginActivity.class);
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
//        if (UserController.isLogin(ShopActivity.this)) {
        List<ObjectCart> list = CartController.getAllCarts(ShopActivity.this);
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
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_ALL_PRODUCT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listDataHeader.clear();
                    listDataChild.clear();

                    List<ObjectCategory> listCategory = CategoryController.getAllCategories(ShopActivity.this);
                    for (ObjectCategory category : listCategory) {
                        listDataHeader.add(category.getName());
                        List<ObjectProduct> listProduct = ProductController.getProductByIdCategory(ShopActivity.this, category.getId());
                        listDataChild.put(category.getName(), listProduct);
                    }
                    listAdapter.notifyDataSetChanged();
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
