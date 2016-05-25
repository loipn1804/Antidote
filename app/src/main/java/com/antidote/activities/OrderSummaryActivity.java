package com.antidote.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.adapters.OrderSummaryAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CouponController;
import com.antidote.daocontroller.OrderDeliveryController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectCoupon;
import antidote.ObjectOrderDelivery;
import antidote.ObjectUser;

/**
 * Created by USER on 5/21/2015.
 */
public class OrderSummaryActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private RelativeLayout rltDelete;
    private ListView lvOrderSummary;
    private OrderSummaryAdapter orderSummaryAdapter;
    private LinearLayout lnlCOrder;
    private List<ObjectCart> listCart;
    private TextView txtDiscount;
    private TextView txtTotal;
    private LinearLayout lnlDeliveryAddress;
    private TextView txtDeliveryAddress;
    //    private float total = 0f;
    private RelativeLayout rltApplyCoupon;
    private EditText edtCodeCoupon;
    private LinearLayout lnlCoupon;
    private TextView txtDeliveryFee;
    private LinearLayout lnlShipppingAddress;

    private String PAY_ID = "";

    private float deliveryFee = 0;

    //    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static String CONFIG_ENVIRONMENT = "";

    // note that these credentials will differ between live & sandbox environments.
//    private static final String CONFIG_CLIENT_ID = "AWV3Vl3P5kdGORcQcHimVb44SYwhhUcAb3JraSaq2naXr0HG5ZeISEzKv9zQFT9V8P5Rjd8iuN-MFp6P";
    private static String CONFIG_CLIENT_ID = "";

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static PayPalConfiguration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        SharedPreferences preferencesDeli = getSharedPreferences("delivery", MODE_PRIVATE);
        deliveryFee = preferencesDeli.getFloat("deliveryFee", 0);

        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        String paypalClientID = preferences.getString("paypalClientID", "");
        int paypalMode = preferences.getInt("paypalMode", 0);

        CONFIG_CLIENT_ID = paypalClientID;
        if (paypalMode == 0) {
            CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
        } else {
            CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
        }

        config = new PayPalConfiguration()
//                .rememberUser(false)
                .acceptCreditCards(false)
                .environment(CONFIG_ENVIRONMENT)
                .clientId(CONFIG_CLIENT_ID)
                        // The following are only used in PayPalFuturePaymentActivity.
                .merchantName("Example Merchant")
                .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_ADD_ORDER);
            intentFilter.addAction(AntidoteService.RECEIVER_GET_COUPON_BY_CODE);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();

        listCart = new ArrayList<ObjectCart>();
        List<ObjectCart> list = CartController.getAllCarts(OrderSummaryActivity.this);
        for (ObjectCart objectCart : list) {
            listCart.add(objectCart);
        }

        orderSummaryAdapter = new OrderSummaryAdapter(OrderSummaryActivity.this, listCart);
        lvOrderSummary.setAdapter(orderSummaryAdapter);

        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltDelete = (RelativeLayout) findViewById(R.id.rltDelete);
        lvOrderSummary = (ListView) findViewById(R.id.lvOrderSummary);
        lnlCOrder = (LinearLayout) findViewById(R.id.lnlOrder);
        txtDiscount = (TextView) findViewById(R.id.txtDiscount);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        lnlDeliveryAddress = (LinearLayout) findViewById(R.id.lnlDeliveryAddress);
        txtDeliveryAddress = (TextView) findViewById(R.id.txtDeliveryAddress);
        rltApplyCoupon = (RelativeLayout) findViewById(R.id.rltApplyCoupon);
        edtCodeCoupon = (EditText) findViewById(R.id.edtCodeCoupon);
        lnlCoupon = (LinearLayout) findViewById(R.id.lnlCoupon);
        txtDeliveryFee = (TextView) findViewById(R.id.txtDeliveryFee);
        lnlShipppingAddress = (LinearLayout) findViewById(R.id.lnlShipppingAddress);

        rltLeft.setOnClickListener(this);
        rltDelete.setOnClickListener(this);
        lnlCOrder.setOnClickListener(this);
        rltApplyCoupon.setOnClickListener(this);
        lnlDeliveryAddress.setOnClickListener(this);
    }

    private void initData() {
        BigDecimal bigDeliveryFee = new BigDecimal(deliveryFee).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtDeliveryFee.setText("S$" + bigDeliveryFee);
        SharedPreferences preferencesDeli = getSharedPreferences("delivery", MODE_PRIVATE);
        boolean is_self = preferencesDeli.getBoolean("is_self", true);
        if (is_self) {
            lnlShipppingAddress.setVisibility(View.GONE);
        } else {
            lnlShipppingAddress.setVisibility(View.VISIBLE);
        }

        BigDecimal bigTotal = new BigDecimal(calculateTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtTotal.setText("S$" + bigTotal);

        ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(OrderSummaryActivity.this);
        if (orderDelivery != null) {
            txtDeliveryAddress.setText(orderDelivery.getShippingAddress());
            BigDecimal bigDiscount = new BigDecimal(Float.parseFloat(orderDelivery.getDiscount())).setScale(2, BigDecimal.ROUND_HALF_UP);
            txtDiscount.setText("S$" + bigDiscount);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltDelete:
                Toast.makeText(OrderSummaryActivity.this, "delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.lnlOrder:
//                Toast.makeText(OrderSummaryActivity.this, "order", Toast.LENGTH_SHORT).show();
                prepareCheckOut();
                checkoutByPaypal();
//                Intent intentPayment = new Intent(OrderSummaryActivity.this, PaymentActivity.class);
//                startActivity(intentPayment);
                break;
            case R.id.rltApplyCoupon:
                String code = edtCodeCoupon.getText().toString().trim();
                if (code.length() == 0) {
                    showToast("Please enter code.");
                } else {
                    Intent intentGetCoupon = new Intent(AntidoteService.ACTION_GET_COUPON_BY_CODE, null, OrderSummaryActivity.this, AntidoteService.class);
                    intentGetCoupon.putExtra(AntidoteService.EXTRA_COUPON_CODE, code);
                    startService(intentGetCoupon);
                    showProgressDialog();
                }
                break;
            case R.id.lnlDeliveryAddress:
                Intent intentShip = new Intent(OrderSummaryActivity.this, ShippingAddressActivity.class);
                intentShip.putExtra("is_summary", true);
                startActivityForResult(intentShip, ShippingAddressActivity.REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                boolean isSuccess = false;
                if (confirm != null) {
                    try {
                        JSONObject jsonObject = confirm.toJSONObject();
                        JSONObject response = jsonObject.getJSONObject("response");
                        PAY_ID = response.getString("id");
                        isSuccess = true;
                    } catch (JSONException e) {
                        isSuccess = false;
                    }
                }
                if (isSuccess) {
                    Intent intentAddOrder = new Intent(AntidoteService.ACTION_ADD_ORDER, null, OrderSummaryActivity.this, AntidoteService.class);
                    startService(intentAddOrder);
                    showProgressDialog();
//                    config.rememberUser(false);
                } else {
                    showToast("Check out unsuccessfully.");
                }
            } else {
                showToast("Check out unsuccessfully.");
            }
        } else if (requestCode == ShippingAddressActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            SharedPreferences preferencesDeli = getSharedPreferences("delivery", MODE_PRIVATE);
            deliveryFee = preferencesDeli.getFloat("deliveryFee", 0);
            initData();
        }
    }

    private float calculateTotal() {
        float total = 0;
        for (ObjectCart cart : listCart) {
            if (cart.getSalePrice() > 0) {
                total += (cart.getSalePrice() * cart.getQuantity());
            } else {
                total += (cart.getRegularPrice() * cart.getQuantity());
            }
        }
        total += deliveryFee;
        ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(OrderSummaryActivity.this);
        if (orderDelivery != null) {
            float discount = Float.parseFloat(orderDelivery.getDiscount());
            total -= discount;
        }
//        BigDecimal bigTotal = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
//        txtTotal.setText("S$" + bigTotal);
        return total;
    }

    private float calculateTotalNoDiscount() {
        float total = 0;
        for (ObjectCart cart : listCart) {
            if (cart.getSalePrice() > 0) {
                total += (cart.getSalePrice() * cart.getQuantity());
            } else {
                total += (cart.getRegularPrice() * cart.getQuantity());
            }
        }
        total += deliveryFee;
//        ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(OrderSummaryActivity.this);
//        if (orderDelivery != null) {
//            float discount = Float.parseFloat(orderDelivery.getDiscount());
//            total -= discount;
//        }
//        BigDecimal bigTotal = new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
//        txtTotal.setText("S$" + bigTotal);
        return total;
    }

    private void prepareCheckOut() {
        ObjectUser user = UserController.getCurrentUser(OrderSummaryActivity.this);
        if (user != null) {
            ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(OrderSummaryActivity.this);
            if (orderDelivery != null) {
                orderDelivery.setCustomerID(user.getId() + "");
                Calendar calendar = Calendar.getInstance();
                calendar.roll(Calendar.DAY_OF_YEAR, 3);
                DateFormat fmDateSend = new SimpleDateFormat("yyyy-MM-dd hh");
                orderDelivery.setOrderDate(fmDateSend.format(calendar.getTime()) + ":00:00");
                BigDecimal bigSubTotal = new BigDecimal(calculateTotalNoDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                orderDelivery.setSubTotal(bigSubTotal + "");
                BigDecimal bigTotal = new BigDecimal(calculateTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
                orderDelivery.setTotal(bigTotal + "");

                OrderDeliveryController.update(OrderSummaryActivity.this, orderDelivery);
            }
        }
    }

    private void checkoutByPaypal() {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(calculateTotal()), "SGD", "Antidote", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(OrderSummaryActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private void applyCoupon() {
        ObjectCoupon coupon = CouponController.getCurrentCoupon(OrderSummaryActivity.this);
        if (coupon != null) {
            if (coupon.getActive() == 1) {
                if (calculateTotalNoDiscount() >= coupon.getMinimum_amount()) {
                    float discount = 0f;
                    if (coupon.getDiscount_type() == 0) {
                        discount = calculateTotalNoDiscount() * coupon.getDiscount_percent() / 100;
                    } else {
                        discount = coupon.getDiscount_amount();
                    }
                    ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(OrderSummaryActivity.this);
                    if (orderDelivery != null) {
                        orderDelivery.setDiscount(discount + "");
                        orderDelivery.setCouponid(coupon.getId() + "");

                        OrderDeliveryController.update(OrderSummaryActivity.this, orderDelivery);
                        BigDecimal bigTotal = new BigDecimal(calculateTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtTotal.setText("S$" + bigTotal);
                        BigDecimal bigDiscount = new BigDecimal(Float.parseFloat(orderDelivery.getDiscount())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtDiscount.setText("S$" + bigDiscount);

                        showToast("Apply coupon successful.");
                        lnlCoupon.setVisibility(View.GONE);
                    }
                } else {
                    BigDecimal bigMiniAmount = new BigDecimal(coupon.getMinimum_amount()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    showPopupPrompt("Minimum mount to apply this code is S$" + bigMiniAmount);
                }
            } else {
                showPopupPrompt("Code is retired. Please enter other code.");
            }
        }
    }

    public void showPopupPromptAddOrderSuccess(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(OrderSummaryActivity.this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_prompt);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticFunction.isBackToHome = true;
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_ADD_ORDER)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    hideProgressDialog();
//                    showToast("Check out successful.");
                    OrderDeliveryController.clearAllOrderDeliveries(OrderSummaryActivity.this);
                    CartController.clearAllCarts(OrderSummaryActivity.this);
                    StaticFunction.isBackToHome = true;
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showPopupPromptAddOrderSuccess(message);
//                    finish();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_COUPON_BY_CODE)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    applyCoupon();
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
