package com.antidote.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antidote.adapters.ShoppingCartAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.OrderDeliveryController;
import com.antidote.daocontroller.UserController;
import com.antidote.staticfunction.StaticFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectOrderDelivery;
import sg.antidote.R;

/**
 * Created by USER on 5/20/2015.
 */
public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener, ShoppingCartAdapter.ShoppingCartAdapterCallback {

    private RelativeLayout rltLeft;
    private RelativeLayout rltDelete;
    private ListView lvShoppingCart;
    private TextView txtTotal;
    private ShoppingCartAdapter shoppingCartAdapter;
    private LinearLayout lnlCheckout;
    private List<ObjectCart> listCart;
    private List<Boolean> isDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        initView();

        listCart = new ArrayList<ObjectCart>();
        isDelete = new ArrayList<Boolean>();

        List<ObjectCart> list = CartController.getAllCarts(ShoppingCartActivity.this);
        for (ObjectCart objectCart : list) {
            listCart.add(objectCart);
            isDelete.add(false);
        }

        shoppingCartAdapter = new ShoppingCartAdapter(ShoppingCartActivity.this, listCart, isDelete, this);
        lvShoppingCart.setAdapter(shoppingCartAdapter);
        calculateTotal();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltDelete = (RelativeLayout) findViewById(R.id.rltDelete);
        lvShoppingCart = (ListView) findViewById(R.id.lvShoppingCart);
        lnlCheckout = (LinearLayout) findViewById(R.id.lnlCheckout);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        rltLeft.setOnClickListener(this);
        rltDelete.setOnClickListener(this);
        lnlCheckout.setOnClickListener(this);
    }

    private void calculateTotal() {
        float total = 0f;
        for (ObjectCart cart : listCart) {
            if (cart.getSalePrice() > 0) {
                total += (cart.getSalePrice() * cart.getQuantity());
            } else {
                total += (cart.getRegularPrice() * cart.getQuantity());
            }
        }
        BigDecimal bigTotal = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtTotal.setText("S$" + bigTotal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltDelete:
//                Toast.makeText(ShoppingCartActivity.this, "delete", Toast.LENGTH_SHORT).show();
                boolean b = false;
                if (isDelete.size() > 0) {
                    b = isDelete.get(0);
                }
                isDelete.clear();
                for (ObjectCart objectCart : listCart) {
                    isDelete.add(!b);
                }
                shoppingCartAdapter.notifyDataSetChanged();
                break;
            case R.id.lnlCheckout:
                if (UserController.isLogin(ShoppingCartActivity.this)) {
                    if (listCart.size() > 0) {
                        prepareCheckOut();
                        Intent intentShip = new Intent(ShoppingCartActivity.this, ShippingAddressActivity.class);
                        startActivity(intentShip);
                    } else {
                        showToast("There is no cart to check out.");
                    }
                    break;
                } else {
                    showPopupConfirmLogin();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StaticFunction.isBackToHome) {
            StaticFunction.isBackToHome = false;
            Intent orderHistory = new Intent(ShoppingCartActivity.this, OrderHistoryActivity.class);
            startActivity(orderHistory);
            finish();
        }
    }

    @Override
    public void deleteCartById(int position) {
        CartController.deleteCartById(ShoppingCartActivity.this, listCart.get(position).getId());
        listCart.remove(position);
        isDelete.remove(position);
        shoppingCartAdapter.notifyDataSetChanged();
        calculateTotal();
        showToast("Delete cart successful.");
    }

    private void prepareCheckOut() {
        OrderDeliveryController.clearAllOrderDeliveries(ShoppingCartActivity.this);
        ObjectOrderDelivery orderDelivery = new ObjectOrderDelivery();
        orderDelivery.setOrder_product(convertCartToJsonString());
        OrderDeliveryController.insert(ShoppingCartActivity.this, orderDelivery);
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(ShoppingCartActivity.this);

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
                Intent intentLogin = new Intent(ShoppingCartActivity.this, LoginActivity.class);
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

    private String convertCartToJsonString() {
        JSONArray array = new JSONArray();
        try {
            for (ObjectCart cart : listCart) {
                JSONObject object = new JSONObject();
                object.put("productID", cart.getProductID() + "");
                object.put("productName", cart.getProductName());
                object.put("quantity", cart.getQuantity() + "");
                object.put("regularPrice", cart.getRegularPrice() + "");
                object.put("salePrice", cart.getSalePrice() + "");
                object.put("optionID", cart.getIdProductOption() + "");
                object.put("optionName", cart.getNameProductOption());

                array.put(object);
            }
        } catch (JSONException e) {

        }
        return array.toString();
    }
}
