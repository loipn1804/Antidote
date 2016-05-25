package com.antidote.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import sg.antidote.R;

import com.antidote.adapters.SpinnerAdapter;
import com.antidote.daocontroller.CountryController;
import com.antidote.daocontroller.OrderDeliveryController;
import com.antidote.daocontroller.UserController;
import com.antidote.staticfunction.StaticFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import antidote.ObjectCountry;
import antidote.ObjectOrderDelivery;
import antidote.ObjectUser;

/**
 * Created by USER on 5/20/2015.
 */
public class ShippingAddressActivity extends BaseActivity implements View.OnClickListener {

    public static int REQUEST_CODE = 121;

    private RelativeLayout rltLeft;
    private LinearLayout lnlContinue;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtCompany;
    private EditText edtEmail;
    private EditText edtPhone;
    private EditText edtAddress;
    private EditText edtCity;
    private EditText edtPostcode;
    private EditText edtNote;
    private Spinner spnCountry;
    private List<ObjectCountry> listCountry;
    private List<String> listNameCountry;
    private ToggleButton toggleSelf;
    private RelativeLayout rltCover;
    private TextView txtLabelDeliveryFee;

    private boolean isSummary;

    private float delivery_fee_self;
    private float delivery_fee_cost;
    private String id_self;
    private String id_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        isSummary = false;

        if (getIntent().getExtras() != null) {
            isSummary = getIntent().getExtras().getBoolean("is_summary", false);
        }

        SharedPreferences preferencesShip = getSharedPreferences("shipping_type", MODE_PRIVATE);
        delivery_fee_self = Float.parseFloat(preferencesShip.getString("value_1", "0"));
        delivery_fee_cost = Float.parseFloat(preferencesShip.getString("value_2", "0"));
        id_self = preferencesShip.getString("id_1", "1");
        id_cost = preferencesShip.getString("id_2", "2");

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        lnlContinue = (LinearLayout) findViewById(R.id.lnlContinue);
        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
        edtLastname = (EditText) findViewById(R.id.edtLastname);
        edtCompany = (EditText) findViewById(R.id.edtCompany);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtPostcode = (EditText) findViewById(R.id.edtPostcode);
        edtNote = (EditText) findViewById(R.id.edtNote);
        spnCountry = (Spinner) findViewById(R.id.spnCountry);
        toggleSelf = (ToggleButton) findViewById(R.id.toggleSelf);
        rltCover = (RelativeLayout) findViewById(R.id.rltCover);
        txtLabelDeliveryFee = (TextView) findViewById(R.id.txtLabelDeliveryFee);

        rltLeft.setOnClickListener(this);
        lnlContinue.setOnClickListener(this);
    }

    private void initData() {

        BigDecimal bigDeliveryFee = new BigDecimal(delivery_fee_cost).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtLabelDeliveryFee.setText("* Standard delivery fee of S$" + bigDeliveryFee);

        ObjectUser user = UserController.getCurrentUser(ShippingAddressActivity.this);
        if (user != null) {
            edtFirstname.setText(user.getFirstName());
            edtLastname.setText(user.getLastName());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            edtAddress.setText(user.getAddress());
            edtCity.setText(user.getCity());
            edtPostcode.setText(user.getPostCode());

            listCountry = CountryController.getAllCountries(ShippingAddressActivity.this);
            listNameCountry = new ArrayList<String>();
            for (ObjectCountry country : listCountry) {
                listNameCountry.add(country.getName());
            }

            String code = user.getCountry();
            int i = 0;
            if (code.length() > 0) {
                for (ObjectCountry country : listCountry) {
                    if (code.equals(country.getCode())) {
                        break;
                    }
                    i++;
                }
            } else {
                for (ObjectCountry country : listCountry) {
                    if (country.getCode().equals("SG")) {
                        break;
                    }
                    i++;
                }
            }

//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listNameCountry);
//            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_item, listNameCountry);
            spnCountry.setAdapter(spinnerAdapter);
            spnCountry.setSelection(i);
            spnCountry.setEnabled(false);
        }
        if (isSummary) {
            ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(ShippingAddressActivity.this);
            if (orderDelivery != null) {
                edtFirstname.setText(orderDelivery.getShippingFirstName());
                edtLastname.setText(orderDelivery.getShippingLastName());
                edtEmail.setText(orderDelivery.getShippingEmail());
                edtPhone.setText(orderDelivery.getShippingPhone());
                edtAddress.setText(orderDelivery.getShippingAddress());
                edtCity.setText(orderDelivery.getShippingCity());
                edtPostcode.setText(orderDelivery.getShippingPostCode());
            }
        }

        toggleSelf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences preferences = getSharedPreferences("delivery", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (b) {
                    rltCover.setBackgroundColor(getResources().getColor(R.color.gray_trans));
                    editor.putFloat("deliveryFee", delivery_fee_self);
                    editor.putBoolean("is_self", true);
                } else {
                    rltCover.setBackgroundColor(getResources().getColor(R.color.tranparent));
                    editor.putFloat("deliveryFee", delivery_fee_cost);
                    editor.putBoolean("is_self", false);
                }
                editor.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.lnlContinue:
//                Intent intentDeliveryTime = new Intent(ShippingAddressActivity.this, DeliveryTimeActivity.class);
//                startActivity(intentDeliveryTime);
                if (!toggleSelf.isChecked()) {
                    String firstname = edtFirstname.getText().toString().trim();
                    String lastname = edtLastname.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    String address = edtAddress.getText().toString().trim();
                    String postcode = edtPostcode.getText().toString().trim();
                    if (firstname.length() == 0) {
                        showToast("Please enter first name.");
                    } else if (lastname.length() == 0) {
                        showToast("Please enter last name.");
                    } else if (email.length() == 0) {
                        showToast("Please enter email.");
                    } else if (!StaticFunction.isValidPhoneNumber(phone)) {
                        showToast("Phone number is invalid.");
                    } else if (address.length() == 0) {
                        showToast("Please enter address.");
                    } else if (postcode.length() != 6) {
                        showToast("Postcode is invalid.");
                    } else {
                        prepareCheckOut();
                        if (!isSummary) {
                            Intent intentSummary = new Intent(ShippingAddressActivity.this, OrderSummaryActivity.class);
                            startActivity(intentSummary);
                            finish();
                        } else {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        break;
                    }
                } else {
                    prepareCheckOut();
                    if (!isSummary) {
                        Intent intentSummary = new Intent(ShippingAddressActivity.this, OrderSummaryActivity.class);
                        startActivity(intentSummary);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    break;
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StaticFunction.isBackToHome) {
            finish();
        }
        SharedPreferences preferences = getSharedPreferences("delivery", MODE_PRIVATE);
        boolean is_self = preferences.getBoolean("is_self", true);
        if (is_self) {
            toggleSelf.setChecked(true);
        } else {
            toggleSelf.setChecked(false);
        }
    }

    private void prepareCheckOut() {
        ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(ShippingAddressActivity.this);
        if (orderDelivery != null) {
            orderDelivery.setShippingFirstName(edtFirstname.getText().toString().trim());
            orderDelivery.setShippingLastName(edtLastname.getText().toString().trim());
            orderDelivery.setShippingCompany(edtCompany.getText().toString().trim());
            orderDelivery.setShippingEmail(edtEmail.getText().toString().trim());
            orderDelivery.setShippingPhone(edtPhone.getText().toString().trim());
            orderDelivery.setShippingAddress(edtAddress.getText().toString().trim());
            orderDelivery.setShippingApartment("");
            orderDelivery.setShippingCity(edtCity.getText().toString().trim());
            orderDelivery.setShippingPostCode(edtPostcode.getText().toString().trim());
            orderDelivery.setShippingNote(edtNote.getText().toString().trim());
            if (listCountry.size() > 0 && spnCountry.getSelectedItem() != null) {
                orderDelivery.setShippingCountry(listCountry.get(spnCountry.getSelectedItemPosition()).getCode());
            } else {
                orderDelivery.setShippingCountry("SG");
            }

            orderDelivery.setOrderStatus("0");
            orderDelivery.setDiscount("0.00");
            orderDelivery.setCouponid("0");
            if (toggleSelf.isChecked()) {
                orderDelivery.setShippingType(id_self);
            } else {
                orderDelivery.setShippingType(id_cost);
            }

            OrderDeliveryController.update(ShippingAddressActivity.this, orderDelivery);
        }
    }
}
