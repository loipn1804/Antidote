package com.antidote.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.antidote.daocontroller.OptionController;
import com.antidote.daocontroller.ProductController;
import com.antidote.daocontroller.ProductOptionController;
import com.antidote.staticfunction.StaticFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import antidote.ObjectOption;
import antidote.ObjectProduct;
import antidote.ObjectProductOption;

/**
 * Created by USER on 6/17/2015.
 */
public class AntidoteServiceParal extends IntentService {

    // action
    public static final String ACTION_GET_OPTION = "ACTION_GET_OPTION";
    public static final String ACTION_GET_PRODUCT_OPTION = "ACTION_GET_PRODUCT_OPTION";
    public static final String ACTION_GET_ALL_PRODUCT = "ACTION_GET_ALL_PRODUCT";
    public static final String ACTION_GET_CONFIG = "ACTION_GET_CONFIG";

    // extra code and result value
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";
    public static final String RESULT_NO_INTERNET = "RESULT_NO_INTERNET";

    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";
    public static String RESULT_MESSAGE = "";

    public AntidoteServiceParal() {
        super(AntidoteService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_GET_OPTION)) {
            if (StaticFunction.isNetworkAvailable(AntidoteServiceParal.this)) {
                getOption();
            }
        } else if (action.equals(ACTION_GET_PRODUCT_OPTION)) {
            if (StaticFunction.isNetworkAvailable(AntidoteServiceParal.this)) {
                getProductOption();
            }
        } else if (action.equals(ACTION_GET_ALL_PRODUCT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteServiceParal.this)) {
                boolean result = getAllProduct();
            }
        } else if (action.equals(ACTION_GET_CONFIG)) {
            if (StaticFunction.isNetworkAvailable(AntidoteServiceParal.this)) {
                boolean result = getConfig();
            }
        }
    }

    private boolean getOption() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getOption();
        OptionController.clearAllOptions(AntidoteServiceParal.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray options = jsonResponse.getJSONArray("data");

                for (int i = 0; i < options.length(); i++) {
                    JSONObject option = options.getJSONObject(i);
                    Long id = option.getLong("id");
                    String name = option.optString("name", "");
                    if (name.equals("null")) name = "";
                    String slug = option.optString("slug", "");
                    if (slug.equals("null")) slug = "";

                    ObjectOption objectOption = new ObjectOption(id, name, slug);
                    OptionController.insertOrUpdate(AntidoteServiceParal.this, objectOption);
                }

                isSuccess = true;
            } else {
                String error_message = jsonResponse.getString("error_message");
                RESULT_MESSAGE = error_message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getProductOption() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getProductOption();
        ProductOptionController.clearAllProductOptions(AntidoteServiceParal.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray product_options = jsonResponse.getJSONArray("data");

                for (int i = 0; i < product_options.length(); i++) {
                    JSONObject product_option = product_options.getJSONObject(i);
                    Long id = product_option.getLong("id");
                    Long productID = product_option.getLong("productID");
                    Long optionID = product_option.getLong("optionID");
                    Integer option_index = product_option.getInt("option_index");
                    String value = product_option.optString("value", "");
                    String str_regularPrice = product_option.optString("regularPrice", "0");
                    if (str_regularPrice.equals("null")) str_regularPrice = "0";
                    Float regularPrice = Float.parseFloat(str_regularPrice);
                    String str_salePrice = product_option.optString("salePrice", "0");
                    if (str_salePrice.equals("null")) str_salePrice = "0";
                    Float salePrice = Float.parseFloat(str_salePrice);

                    ObjectProductOption objectProductOption = new ObjectProductOption(id, productID, optionID, option_index, value, regularPrice, salePrice);
                    ProductOptionController.insertOrUpdate(AntidoteServiceParal.this, objectProductOption);
                }

                isSuccess = true;
            } else {
                String error_message = jsonResponse.getString("error_message");
                RESULT_MESSAGE = error_message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private boolean getAllProduct() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllProduct();
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                ProductController.clearAllProducts(AntidoteServiceParal.this);

                JSONArray categories = jsonResponse.getJSONArray("data");

                for (int i = 0; i < categories.length(); i++) {
                    JSONObject category = categories.getJSONObject(i);
                    JSONArray products = category.getJSONArray("products");
                    insertProduct(products);
                }

                isSuccess = true;
            } else {
                String error_message = jsonResponse.getString("error_message");
                RESULT_MESSAGE = error_message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private void insertProduct(JSONArray products) {
        try {
            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                Long id = product.getLong("id");
                Long categoryID = product.getLong("categoryID");
                String name = product.optString("name", "");
                if (name.equals("null")) name = "";
                String slug = product.optString("slug", "");
                if (slug.equals("null")) slug = "";
                String description = product.optString("description", "");
                if (description.equals("null")) description = "";
                String str_regularPrice = product.optString("regularPrice", "0");
                if (str_regularPrice.equals("null")) str_regularPrice = "0";
                Float regularPrice = Float.parseFloat(str_regularPrice);
                String str_salePrice = product.optString("salePrice", "0");
                if (str_salePrice.equals("null")) str_salePrice = "0";
                Float salePrice = Float.parseFloat(str_salePrice);
                String priceRange = product.optString("priceRange", "");
                if (priceRange.equals("null")) priceRange = "";
                String isActive = product.optString("isActive", "");
                String image = product.optString("image", "");
                if (image.equals("null")) image = "";

                ObjectProduct objectProduct = new ObjectProduct(id, categoryID, name, slug, description, regularPrice, salePrice, priceRange, isActive, image);
                ProductController.insertOrUpdate(AntidoteServiceParal.this, objectProduct);
            }
        } catch (JSONException e) {

        }
    }

    private boolean getConfig() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getConfig();
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                String contactEmail = data.getString("contactEmail");
                String paypalAccount = data.getString("paypalAccount");
                String paypalClientID = data.getString("paypalClientID");
                String contactPhone = data.getString("contactPhone");
                int paypalMode = data.getInt("paypalMode");

                SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("contactEmail", contactEmail);
                editor.putString("paypalAccount", paypalAccount);
                editor.putString("paypalClientID", paypalClientID);
                editor.putString("contactPhone", contactPhone);
                editor.putInt("paypalMode", paypalMode);
                editor.commit();

                JSONArray arrShipType = data.getJSONArray("delivery_fee");
                if (arrShipType.length() >= 2) {
                    JSONObject job_1 = arrShipType.getJSONObject(0);
                    String id_1 = job_1.getString("id");
                    String name_1 = job_1.getString("name");
                    String value_1 = job_1.getString("value");

                    JSONObject job_2 = arrShipType.getJSONObject(1);
                    String id_2 = job_2.getString("id");
                    String name_2 = job_2.getString("name");
                    String value_2 = job_2.getString("value");

                    SharedPreferences preferencesShip = getSharedPreferences("shipping_type", MODE_PRIVATE);
                    SharedPreferences.Editor editorShip = preferencesShip.edit();
                    editorShip.clear();
                    editorShip.commit();
                    editorShip.putString("id_1", id_1);
                    editorShip.putString("name_1", name_1);
                    editorShip.putString("value_1", value_1);
                    editorShip.putString("id_2", id_2);
                    editorShip.putString("name_2", name_2);
                    editorShip.putString("value_2", value_2);
                    editorShip.commit();
                }

                isSuccess = true;
            } else {
                String error_message = jsonResponse.getString("error_message");
                RESULT_MESSAGE = error_message;
                isSuccess = false;
            }
        } catch (JSONException e) {
            isSuccess = false;
        }

        return isSuccess;
    }
}
