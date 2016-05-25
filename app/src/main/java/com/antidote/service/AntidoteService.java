package com.antidote.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.antidote.daocontroller.BannerController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.CommentController;
import com.antidote.daocontroller.CountryController;
import com.antidote.daocontroller.CouponController;
import com.antidote.daocontroller.FaqCommentController;
import com.antidote.daocontroller.FaqController;
import com.antidote.daocontroller.FaqV2Controller;
import com.antidote.daocontroller.GroupProductController;
import com.antidote.daocontroller.GroupTimerController;
import com.antidote.daocontroller.OrderDeliveryController;
import com.antidote.daocontroller.OrderHistoryController;
import com.antidote.daocontroller.ProductController;
import com.antidote.daocontroller.UserController;
import com.antidote.staticfunction.StaticFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectBanner;
import antidote.ObjectCategory;
import antidote.ObjectComment;
import antidote.ObjectCountry;
import antidote.ObjectCoupon;
import antidote.ObjectFaq;
import antidote.ObjectFaqComment;
import antidote.ObjectFaqV2;
import antidote.ObjectGroupProduct;
import antidote.ObjectGroupTimer;
import antidote.ObjectOrderDelivery;
import antidote.ObjectOrderHistory;
import antidote.ObjectProduct;
import antidote.ObjectUser;

/**
 * Created by USER on 5/23/2015.
 */
public class AntidoteService extends IntentService {

    // action
    public static final String ACTION_LOGIN = "ACTION_LOGIN";
    public static final String ACTION_LOGIN_FB = "ACTION_LOGIN_FB";
    public static final String ACTION_SIGNUP = "ACTION_SIGNUP";
    public static final String ACTION_ADD_EMAIL = "ACTION_ADD_EMAIL";
    public static final String ACTION_CHANGE_PASS = "ACTION_CHANGE_PASS";
    public static final String ACTION_UPDATE_PROFILE = "ACTION_UPDATE_PROFILE";
    public static final String ACTION_UPLOAD_AVATAR = "ACTION_UPLOAD_AVATAR";
    public static final String ACTION_GET_ALL_PRODUCT = "ACTION_GET_ALL_PRODUCT";
    public static final String ACTION_GET_PRODUCT_BY_ID_CATE = "ACTION_GET_PRODUCT_BY_ID_CATE";
    public static final String ACTION_GET_ALL_COUNTRY = "ACTION_GET_ALL_COUNTRY";
    public static final String ACTION_GET_ALL_CATEGORY = "ACTION_GET_ALL_CATEGORY";
    public static final String ACTION_GET_COMMENT = "ACTION_GET_COMMENT";
    public static final String ACTION_ADD_COMMENT = "ACTION_ADD_COMMENT";
    public static final String ACTION_GET_GROUP_PRODUCT = "ACTION_GET_GROUP_PRODUCT";
    public static final String ACTION_ADD_ORDER = "ACTION_ADD_ORDER";
    public static final String ACTION_GET_BANNER = "ACTION_GET_BANNER";
    public static final String ACTION_GET_ORDER_HISTORY = "ACTION_GET_ORDER_HISTORY";
    public static final String ACTION_GET_COUPON_BY_CODE = "ACTION_GET_COUPON_BY_CODE";
    public static final String ACTION_GET_ALL_FAQ = "ACTION_GET_ALL_FAQ";
    public static final String ACTION_GET_ALL_FAQ_V2 = "ACTION_GET_ALL_FAQ_V2";
    public static final String ACTION_GET_ALL_FAQ_COMMENT = "ACTION_GET_ALL_FAQ_COMMENT";
    public static final String ACTION_ADD_FAQ = "ACTION_ADD_FAQ";
    public static final String ACTION_ADD_FAQ_COMMENT = "ACTION_ADD_FAQ_COMMENT";
    public static final String ACTION_FORGOT_PASS = "ACTION_FORGOT_PASS";

    // receiver
    public static final String RECEIVER_LOGIN = "RECEIVER_LOGIN";
    public static final String RECEIVER_LOGIN_FB = "RECEIVER_LOGIN_FB";
    public static final String RECEIVER_SIGNUP = "RECEIVER_SIGNUP";
    public static final String RECEIVER_ADD_EMAIL = "RECEIVER_ADD_EMAIL";
    public static final String RECEIVER_CHANGE_PASS = "RECEIVER_CHANGE_PASS";
    public static final String RECEIVER_UPDATE_PROFILE = "RECEIVER_UPDATE_PROFILE";
    public static final String RECEIVER_UPLOAD_AVATAR = "RECEIVER_UPLOAD_AVATAR";
    public static final String RECEIVER_GET_ALL_CATEGORY = "RECEIVER_GET_ALL_CATEGORY";
    public static final String RECEIVER_GET_ALL_PRODUCT = "RECEIVER_GET_ALL_PRODUCT";
    public static final String RECEIVER_GET_PRODUCT_BY_ID_CATE = "RECEIVER_GET_PRODUCT_BY_ID_CATE";
    public static final String RECEIVER_GET_COMMENT = "RECEIVER_GET_COMMENT";
    public static final String RECEIVER_ADD_COMMENT = "RECEIVER_ADD_COMMENT";
    public static final String RECEIVER_GET_GROUP_PRODUCT = "RECEIVER_GET_GROUP_PRODUCT";
    public static final String RECEIVER_ADD_ORDER = "RECEIVER_ADD_ORDER";
    public static final String RECEIVER_GET_BANNER = "RECEIVER_GET_BANNER";
    public static final String RECEIVER_GET_ORDER_HISTORY = "RECEIVER_GET_ORDER_HISTORY";
    public static final String RECEIVER_GET_COUPON_BY_CODE = "RECEIVER_GET_COUPON_BY_CODE";
    public static final String RECEIVER_GET_ALL_FAQ = "RECEIVER_GET_ALL_FAQ";
    public static final String RECEIVER_GET_ALL_FAQ_V2 = "RECEIVER_GET_ALL_FAQ_V2";
    public static final String RECEIVER_GET_ALL_FAQ_COMMENT = "RECEIVER_GET_ALL_FAQ_COMMENT";
    public static final String RECEIVER_ADD_FAQ = "RECEIVER_ADD_FAQ";
    public static final String RECEIVER_ADD_FAQ_COMMENT = "RECEIVER_ADD_FAQ_COMMENT";
    public static final String RECEIVER_FORGOT_PASS = "RECEIVER_FORGOT_PASS";

    // extra code and result value
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final String RESULT_OK = "RESULT_OK";
    public static final String RESULT_FAIL = "RESULT_FAIL";
    public static final String RESULT_NO_INTERNET = "RESULT_NO_INTERNET";

    public static final String EXTRA_RESULT_MESSAGE = "EXTRA_RESULT_MESSAGE";
    public static String RESULT_MESSAGE = "";

    // put extra
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static final String EXTRA_PASS = "EXTRA_PASS";
    public static final String EXTRA_ID_FB = "EXTRA_ID_FB";
    public static final String EXTRA_FIRSTNAME = "EXTRA_FIRSTNAME";
    public static final String EXTRA_LASTNAME = "EXTRA_LASTNAME";
    public static final String EXTRA_FIRSTNAME_FB = "EXTRA_FIRSTNAME_FB";
    public static final String EXTRA_LASTNAME_FB = "EXTRA_LASTNAME_FB";
    public static final String EXTRA_EMAIL_FB = "EXTRA_EMAIL_FB";
    public static final String EXTRA_ID_CATEGORY = "EXTRA_ID_CATEGORY";
    public static final String EXTRA_COMMENT_USER_ID = "EXTRA_COMMENT_USER_ID";
    public static final String EXTRA_COMMENT_PRODUCT_ID = "EXTRA_COMMENT_PRODUCT_ID";
    public static final String EXTRA_COMMENT_COMMENT = "EXTRA_COMMENT_COMMENT";
    public static final String EXTRA_COMMENT_PAGER = "EXTRA_COMMENT_PAGER";
    public static final String EXTRA_COMMENT_LIMIT = "EXTRA_COMMENT_LIMIT";
    public static final String EXTRA_ID_PRODUCT = "EXTRA_ID_PRODUCT";
    public static final String EXTRA_PROFILE_USER_ID = "EXTRA_PROFILE_USER_ID";
    public static final String EXTRA_PROFILE_FIRSTNAME = "EXTRA_PROFILE_FIRSTNAME";
    public static final String EXTRA_PROFILE_LASTNAME = "EXTRA_PROFILE_LASTNAME";
    public static final String EXTRA_PROFILE_PHONE = "EXTRA_PROFILE_PHONE";
    public static final String EXTRA_PROFILE_ADDRESS = "EXTRA_PROFILE_ADDRESS";
    public static final String EXTRA_PROFILE_CITY = "EXTRA_PROFILE_CITY";
    public static final String EXTRA_PROFILE_COUNTRY = "EXTRA_PROFILE_COUNTRY";
    public static final String EXTRA_PROFILE_POSTCODE = "EXTRA_PROFILE_POSTCODE";
    public static final String EXTRA_PROFILE_EMAIL = "EXTRA_PROFILE_EMAIL";
    public static final String EXTRA_PROFILE_PASS = "EXTRA_PROFILE_PASS";
    public static final String EXTRA_PROFILE_OLD_PASS = "EXTRA_PROFILE_OLD_PASS";
    public static final String EXTRA_PROFILE_NEW_PASS = "EXTRA_PROFILE_NEW_PASS";
    public static final String EXTRA_PROFILE_CONFIRM_PASS = "EXTRA_PROFILE_CONFIRM_PASS";
    public static final String EXTRA_PROFILE_AVATAR = "EXTRA_PROFILE_AVATAR";
    public static final String EXTRA_ORD_HIS_USER_ID = "EXTRA_ORD_HIS_USER_ID";
    public static final String EXTRA_COUPON_CODE = "EXTRA_COUPON_CODE";
    public static final String EXTRA_FAQ_ID = "EXTRA_FAQ_ID";
    public static final String EXTRA_FAQ_USER_ID = "EXTRA_FAQ_USER_ID";
    public static final String EXTRA_FAQ_QUESTION = "EXTRA_FAQ_QUESTION";
    public static final String EXTRA_FAQ_COMMENT_USER_ID = "EXTRA_FAQ_COMMENT_USER_ID";
    public static final String EXTRA_FAQ_COMMENT_FAQ_ID = "EXTRA_FAQ_COMMENT_FAQ_ID";
    public static final String EXTRA_FAQ_COMMENT = "EXTRA_FAQ_COMMENT";

    public AntidoteService() {
        super(AntidoteService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if (action.equals(ACTION_LOGIN)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = login(intent.getStringExtra(EXTRA_EMAIL), intent.getStringExtra(EXTRA_PASS));
                if (result) {
                    sendBroadCastReceiver(RECEIVER_LOGIN, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_LOGIN, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_LOGIN, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_SIGNUP)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = signup(intent.getStringExtra(EXTRA_EMAIL), intent.getStringExtra(EXTRA_PASS), intent.getStringExtra(EXTRA_FIRSTNAME), intent.getStringExtra(EXTRA_LASTNAME));
                if (result) {
                    sendBroadCastReceiver(RECEIVER_SIGNUP, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_SIGNUP, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_SIGNUP, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_ALL_CATEGORY)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getAllCategory();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_CATEGORY, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_CATEGORY, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_ALL_CATEGORY, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_ALL_COUNTRY)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                getAllCountry();
            }
        } else if (action.equals(ACTION_GET_ALL_PRODUCT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getAllProduct();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_PRODUCT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_PRODUCT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_ALL_PRODUCT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_PRODUCT_BY_ID_CATE)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getProductByIdCate(intent.getStringExtra(EXTRA_ID_CATEGORY));
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_PRODUCT_BY_ID_CATE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_PRODUCT_BY_ID_CATE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_PRODUCT_BY_ID_CATE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_COMMENT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getComment(intent.getStringExtra(EXTRA_ID_PRODUCT), intent.getStringExtra(EXTRA_COMMENT_PAGER), intent.getStringExtra(EXTRA_COMMENT_LIMIT));
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_COMMENT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_COMMENT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_COMMENT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_UPDATE_PROFILE)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String user_id = intent.getStringExtra(EXTRA_PROFILE_USER_ID);
                String firstname = intent.getStringExtra(EXTRA_PROFILE_FIRSTNAME);
                String lastname = intent.getStringExtra(EXTRA_PROFILE_LASTNAME);
                String phone = intent.getStringExtra(EXTRA_PROFILE_PHONE);
                String address = intent.getStringExtra(EXTRA_PROFILE_ADDRESS);
                String city = intent.getStringExtra(EXTRA_PROFILE_CITY);
                String country = intent.getStringExtra(EXTRA_PROFILE_COUNTRY);
                String postcode = intent.getStringExtra(EXTRA_PROFILE_POSTCODE);

                boolean result = updateProfile(user_id, firstname, lastname, phone, address, city, country, postcode);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_UPDATE_PROFILE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_UPDATE_PROFILE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_UPDATE_PROFILE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_LOGIN_FB)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = loginFb(intent.getStringExtra(EXTRA_ID_FB), intent.getStringExtra(EXTRA_EMAIL_FB), intent.getStringExtra(EXTRA_FIRSTNAME_FB), intent.getStringExtra(EXTRA_LASTNAME_FB));
                if (result) {
                    sendBroadCastReceiver(RECEIVER_LOGIN_FB, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_LOGIN_FB, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_LOGIN_FB, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ADD_EMAIL)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String user_id = intent.getStringExtra(EXTRA_PROFILE_USER_ID);
                String email = intent.getStringExtra(EXTRA_PROFILE_EMAIL);
                String pass = intent.getStringExtra(EXTRA_PROFILE_PASS);
                boolean result = addEmail(user_id, email, pass);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ADD_EMAIL, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ADD_EMAIL, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ADD_EMAIL, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_CHANGE_PASS)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String user_id = intent.getStringExtra(EXTRA_PROFILE_USER_ID);
                String old_pass = intent.getStringExtra(EXTRA_PROFILE_OLD_PASS);
                String new_pass = intent.getStringExtra(EXTRA_PROFILE_NEW_PASS);
                String confirm_pass = intent.getStringExtra(EXTRA_PROFILE_CONFIRM_PASS);
                boolean result = changePassword(user_id, old_pass, new_pass, confirm_pass);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_CHANGE_PASS, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_CHANGE_PASS, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_CHANGE_PASS, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_UPLOAD_AVATAR)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String user_id = intent.getStringExtra(EXTRA_PROFILE_USER_ID);
                String avatar = intent.getStringExtra(EXTRA_PROFILE_AVATAR);
                boolean result = uploadAvatar(user_id, avatar);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_UPLOAD_AVATAR, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_UPLOAD_AVATAR, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_UPLOAD_AVATAR, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ADD_COMMENT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String user_id = intent.getStringExtra(EXTRA_COMMENT_USER_ID);
                String product_id = intent.getStringExtra(EXTRA_COMMENT_PRODUCT_ID);
                String comment = intent.getStringExtra(EXTRA_COMMENT_COMMENT);
                boolean result = addComment(user_id, product_id, comment);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ADD_COMMENT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ADD_COMMENT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ADD_COMMENT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_GROUP_PRODUCT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getGroupProduct();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_GROUP_PRODUCT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_GROUP_PRODUCT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_GROUP_PRODUCT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ADD_ORDER)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = addOrder();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ADD_ORDER, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ADD_ORDER, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ADD_ORDER, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_BANNER)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getBanner();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_BANNER, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_BANNER, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_BANNER, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_ORDER_HISTORY)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String user_id = intent.getStringExtra(EXTRA_ORD_HIS_USER_ID);
                boolean result = getOrderHistoryByIdUser(user_id);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_ORDER_HISTORY, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_ORDER_HISTORY, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_ORDER_HISTORY, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_COUPON_BY_CODE)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String code = intent.getStringExtra(EXTRA_COUPON_CODE);
                boolean result = getCouponByCode(code);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_COUPON_BY_CODE, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_COUPON_BY_CODE, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_COUPON_BY_CODE, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_ALL_FAQ)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getAllFaq();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_ALL_FAQ_V2)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                boolean result = getAllFaqV2();
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ_V2, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ_V2, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ_V2, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_GET_ALL_FAQ_COMMENT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String faqid = intent.getStringExtra(EXTRA_FAQ_ID);
                boolean result = getAllFaqCommentByFaqId(faqid);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ_COMMENT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ_COMMENT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_GET_ALL_FAQ_COMMENT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ADD_FAQ)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String userid = intent.getStringExtra(EXTRA_FAQ_USER_ID);
                String question = intent.getStringExtra(EXTRA_FAQ_QUESTION);
                boolean result = addFaq(userid, question);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ADD_FAQ, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ADD_FAQ, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ADD_FAQ, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_ADD_FAQ_COMMENT)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String userid = intent.getStringExtra(EXTRA_FAQ_COMMENT_USER_ID);
                String faqsid = intent.getStringExtra(EXTRA_FAQ_COMMENT_FAQ_ID);
                String comment = intent.getStringExtra(EXTRA_FAQ_COMMENT);
                boolean result = addFaqComment(userid, faqsid, comment);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_ADD_FAQ_COMMENT, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_ADD_FAQ_COMMENT, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_ADD_FAQ_COMMENT, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        } else if (action.equals(ACTION_FORGOT_PASS)) {
            if (StaticFunction.isNetworkAvailable(AntidoteService.this)) {
                String email = intent.getStringExtra(EXTRA_EMAIL);
                boolean result = forgotPass(email);
                if (result) {
                    sendBroadCastReceiver(RECEIVER_FORGOT_PASS, RESULT_OK, RESULT_MESSAGE);
                } else {
                    sendBroadCastReceiver(RECEIVER_FORGOT_PASS, RESULT_FAIL, RESULT_MESSAGE);
                }
            } else {
                sendBroadCastReceiver(RECEIVER_FORGOT_PASS, RESULT_NO_INTERNET, RESULT_MESSAGE);
            }
        }
    }

    private void sendBroadCastReceiver(String action, String result, String message) {
        Intent i = new Intent();
        i.setAction(action);
        i.putExtra(AntidoteService.EXTRA_RESULT, result);
        i.putExtra(AntidoteService.EXTRA_RESULT_MESSAGE, message);
        RESULT_MESSAGE = "";
        sendBroadcast(i);
    }

    private boolean login(String email_input, String pass) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.login(email_input, pass);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                Long id = data.getLong("id");
                String email = data.optString("email", "");
                if (email.equals("null")) email = "";
                String idFacebook = data.optString("idFacebook", "");
                if (idFacebook.equals("null")) idFacebook = "";
                String firstName = data.optString("firstName", "");
                if (firstName.equals("null")) firstName = "";
                String lastName = data.optString("lastName", "");
                if (lastName.equals("null")) lastName = "";
                String phone = data.optString("phone", "");
                if (phone.equals("null")) phone = "";
                String address = data.optString("address", "");
                if (address.equals("null")) address = "";
                String city = data.optString("city", "");
                if (city.equals("null")) city = "";
                String country = data.optString("country", "");
                if (country.equals("null")) country = "";
                String postCode = data.optString("postCode", "");
                if (postCode.equals("null")) postCode = "";
                String isActive = data.getString("isActive");
                String image = data.optString("image", "");
                if (image.equals("null")) image = "";

                ObjectUser user = new ObjectUser(id, email, idFacebook, firstName, lastName, phone, address, city, country, postCode, isActive, image);
                UserController.insertOrUpdate(AntidoteService.this, user);

                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", user.getEmail());
                editor.commit();

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

    private boolean loginFb(String id_fb, String email_input, String firstname_input, String lastname_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.loginFb(id_fb, email_input, firstname_input, lastname_input);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                Long id = data.getLong("id");
                String email = data.optString("email", "");
                if (email.equals("null")) email = "";
                String idFacebook = data.optString("idFacebook", "");
                if (idFacebook.equals("null")) idFacebook = "";
                String firstName = data.optString("firstName", "");
                if (firstName.equals("null")) firstName = "";
                String lastName = data.optString("lastName", "");
                if (lastName.equals("null")) lastName = "";
                String phone = data.optString("phone", "");
                if (phone.equals("null")) phone = "";
                String address = data.optString("address", "");
                if (address.equals("null")) address = "";
                String city = data.optString("city", "");
                if (city.equals("null")) city = "";
                String country = data.optString("country", "");
                if (country.equals("null")) country = "";
                String postCode = data.optString("postCode", "");
                if (postCode.equals("null")) postCode = "";
                String isActive = data.getString("isActive");
                String image = data.optString("image", "");
                if (image.equals("null")) image = "";

                ObjectUser user = new ObjectUser(id, email, idFacebook, firstName, lastName, phone, address, city, country, postCode, isActive, image);
                UserController.insertOrUpdate(AntidoteService.this, user);

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

    private boolean addEmail(String user_id, String email_input, String pass) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.addEmail(user_id, email_input, pass);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                Long id = data.getLong("id");
                String email = data.optString("email", "");
                if (email.equals("null")) email = "";
                String idFacebook = data.optString("idFacebook", "");
                if (idFacebook.equals("null")) idFacebook = "";
                String firstName = data.optString("firstName", "");
                if (firstName.equals("null")) firstName = "";
                String lastName = data.optString("lastName", "");
                if (lastName.equals("null")) lastName = "";
                String phone = data.optString("phone", "");
                if (phone.equals("null")) phone = "";
                String address = data.optString("address", "");
                if (address.equals("null")) address = "";
                String city = data.optString("city", "");
                if (city.equals("null")) city = "";
                String country = data.optString("country", "");
                if (country.equals("null")) country = "";
                String postCode = data.optString("postCode", "");
                if (postCode.equals("null")) postCode = "";
                String isActive = data.getString("isActive");
                String image = data.optString("image", "");
                if (image.equals("null")) image = "";

                ObjectUser user = new ObjectUser(id, email, idFacebook, firstName, lastName, phone, address, city, country, postCode, isActive, image);
                UserController.insertOrUpdate(AntidoteService.this, user);

                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", user.getEmail());
                editor.commit();

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

    private boolean changePassword(String user_id, String old_pass, String new_pass, String confirm_pass) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.changePassword(user_id, old_pass, new_pass, confirm_pass);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
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

    private boolean signup(String email_input, String pass, String firstname, String lastname) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.signup(email_input, pass, firstname, lastname);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {

                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", email_input);
                editor.commit();

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

    private boolean forgotPass(String email) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.forgotPass(email);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                String message = jsonResponse.getString("msg");
                RESULT_MESSAGE = message;
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

    private boolean updateProfile(String user_id_input, String firstname_input, String lastname_input, String phone_input, String address_input, String city_input, String country_input, String postcode_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.updateProfile(user_id_input, firstname_input, lastname_input, phone_input, address_input, city_input, country_input, postcode_input);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                Long id = data.getLong("id");
                String email = data.optString("email", "");
                if (email.equals("null")) email = "";
                String idFacebook = data.optString("idFacebook", "");
                if (idFacebook.equals("null")) idFacebook = "";
                String firstName = data.optString("firstName", "");
                if (firstName.equals("null")) firstName = "";
                String lastName = data.optString("lastName", "");
                if (lastName.equals("null")) lastName = "";
                String phone = data.optString("phone", "");
                if (phone.equals("null")) phone = "";
                String address = data.optString("address", "");
                if (address.equals("null")) address = "";
                String city = data.optString("city", "");
                if (city.equals("null")) city = "";
                String country = data.optString("country", "");
                if (country.equals("null")) country = "";
                String postCode = data.optString("postCode", "");
                if (postCode.equals("null")) postCode = "";
                String isActive = data.getString("isActive");
                String image = data.optString("image", "");
                if (image.equals("null")) image = "";

                ObjectUser user = new ObjectUser(id, email, idFacebook, firstName, lastName, phone, address, city, country, postCode, isActive, image);
                UserController.insertOrUpdate(AntidoteService.this, user);

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

    private boolean uploadAvatar(String user_id, String image_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.uploadAvatar(user_id, image_input);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONObject data = jsonResponse.getJSONObject("data");

                Long id = data.getLong("id");
                String image = data.optString("image", "");
                if (image.equals("null")) image = "";

                ObjectUser user = UserController.getCurrentUser(AntidoteService.this);
                user.setImage(image);
                UserController.insertOrUpdate(AntidoteService.this, user);

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
                ProductController.insertOrUpdate(AntidoteService.this, objectProduct);
            }
        } catch (JSONException e) {

        }
    }

    private boolean getGroupProduct() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getGroupProduct();
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                List<ObjectGroupTimer> timerList = GroupTimerController.getAllGroupTimers(AntidoteService.this);
                GroupProductController.clearAllGroupProducts(AntidoteService.this);
                JSONArray groups = jsonResponse.getJSONArray("data");

                for (int i = 0; i < groups.length(); i++) {
                    JSONObject group = groups.getJSONObject(i);
//                    Long id = group.getLong("id");
//                    Integer groupID = group.getInt("groupID");
                    String name = group.getString("name");
//                    Long productID = group.getLong("productID");
//                    Integer timer = group.getInt("timer");
                    String image = group.getString("image");
                    Integer repeat_day = group.getInt("repeat_day");
                    String ingredient = group.getString("ingredient");
                    if (ingredient.equalsIgnoreCase("null")) ingredient = "";
                    String description = group.getString("description");
                    if (description.equalsIgnoreCase("null")) description = "";
                    JSONArray products = group.getJSONArray("products");

                    for (int j = 0; j < products.length(); j++) {
                        JSONObject product = products.getJSONObject(j);
                        Integer groupID = product.getInt("groupID");
                        Long productID = product.getLong("productID");
                        Integer timer = product.getInt("timer");

                        ObjectGroupProduct groupProduct = new ObjectGroupProduct();
                        groupProduct.setGroupID(groupID);
                        groupProduct.setName(name);
                        groupProduct.setProductID(productID);
                        groupProduct.setTimer(timer);
                        groupProduct.setImage(image);
                        groupProduct.setRepeat_day(repeat_day);
                        groupProduct.setIngredient(ingredient);
                        groupProduct.setDescription(description);
                        GroupProductController.insert(AntidoteService.this, groupProduct);
                    }

                    insertProduct(products);
                }

                isSuccess = true;

                List<ObjectGroupProduct> productListNew = GroupProductController.getAllGroupProducts(AntidoteService.this);
                for (ObjectGroupProduct objectNew : productListNew) {
                    boolean isHave = false;
                    for (ObjectGroupTimer timer : timerList) {
                        if (timer.getGroupID() == objectNew.getGroupID()) {
                            isHave = true;
                            break;
                        }
                    }
                    if (!isHave) {
                        GroupTimerController.clearByIdGroup(AntidoteService.this, objectNew.getGroupID());
                    }
                }
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

    private boolean getProductByIdCate(String id_cate) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getProductByIdCate(id_cate);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray products = jsonResponse.getJSONArray("data");
                insertProduct(products);

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

    private boolean getAllCategory() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllCategory();
        CategoryController.clearAllCategories(AntidoteService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray categories = jsonResponse.getJSONArray("data");

                for (int i = 0; i < categories.length(); i++) {
                    JSONObject category = categories.getJSONObject(i);
                    Long id = category.getLong("id");
                    String name = category.optString("name", "");
                    if (name.equals("null")) name = "";
                    String slug = category.optString("slug", "");
                    if (slug.equals("null")) slug = "";
                    String parentID = category.optString("parentID", "0");
                    if (parentID.equals("null")) parentID = "0";
                    String shortDescription = category.optString("shortDescription", "");
                    if (shortDescription.equals("null")) shortDescription = "";
                    String fullDescription = category.optString("fullDescription", "");
                    if (fullDescription.equals("null")) fullDescription = "";
                    String thumbnail = category.optString("thumbnail", "");
                    if (thumbnail.equals("null")) thumbnail = "";
                    String image = category.optString("image", "");
                    if (image.equals("null")) image = "";
                    String isActive = category.optString("isActive", "");

                    ObjectCategory objectCategory = new ObjectCategory(id, name, slug, parentID, shortDescription, fullDescription, thumbnail, image, isActive);
                    CategoryController.insertOrUpdate(AntidoteService.this, objectCategory);
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

    private boolean getAllCountry() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllCountry();
        CountryController.clearAllCountriess(AntidoteService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray categories = jsonResponse.getJSONArray("data");

                for (int i = 0; i < categories.length(); i++) {
                    JSONObject category = categories.getJSONObject(i);
                    String code = category.optString("code", "");
                    String slug = category.optString("slug", "");
                    String name = category.optString("name", "");

                    ObjectCountry objectCountry = new ObjectCountry();
                    objectCountry.setCode(code);
                    objectCountry.setSlug(slug);
                    objectCountry.setName(name);
                    CountryController.insert(AntidoteService.this, objectCountry);
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

    private boolean getComment(String id_product, String page, String limit) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getComment(id_product, page, limit);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray comments = jsonResponse.getJSONArray("data");

                for (int i = 0; i < comments.length(); i++) {
                    JSONObject commentObj = comments.getJSONObject(i);
                    Long id = commentObj.getLong("id");
                    Integer productID = commentObj.getInt("productID");
                    Integer userID = commentObj.getInt("userID");
                    String comment = commentObj.optString("comment", "");
                    String createDate = commentObj.optString("createDate", "0000-00-00 00:00:00");
                    String firstName = commentObj.optString("firstName", "");
                    if (firstName.equalsIgnoreCase("null")) firstName = "";
                    String lastName = commentObj.optString("lastName", "");
                    if (lastName.equalsIgnoreCase("null")) lastName = "";
                    String image = commentObj.optString("image", "");
                    String idFacebook = commentObj.optString("idFacebook", "");
                    if (idFacebook.equalsIgnoreCase("null")) idFacebook = "";

                    ObjectComment objectComment = new ObjectComment(id, productID, userID, comment, createDate, firstName, lastName, image, idFacebook);
                    CommentController.insertOrUpdate(AntidoteService.this, objectComment);
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

    private boolean addComment(String user_id, String product_id, String comment_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.addComment(user_id, product_id, comment_input);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray comments = jsonResponse.getJSONArray("data");

                for (int i = 0; i < comments.length(); i++) {
                    JSONObject commentObj = comments.getJSONObject(i);
                    Long id = commentObj.getLong("id");
                    Integer productID = commentObj.getInt("productID");
                    Integer userID = commentObj.getInt("userID");
                    String comment = commentObj.optString("comment", "");
                    String createDate = commentObj.optString("createDate", "0000-00-00 00:00:00");
                    String firstName = commentObj.optString("firstName", "");
                    String lastName = commentObj.optString("lastName", "");
                    String image = commentObj.optString("image", "");
                    String idFacebook = commentObj.optString("idFacebook", "");
                    if (idFacebook.equalsIgnoreCase("null")) idFacebook = "";

                    ObjectComment objectComment = new ObjectComment(id, productID, userID, comment, createDate, firstName, lastName, image, idFacebook);
                    CommentController.insertOrUpdate(AntidoteService.this, objectComment);
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

    private boolean addOrder() {
        boolean isSuccess = false;
        ObjectOrderDelivery orderDelivery = OrderDeliveryController.getCurrentOrderDeliveries(AntidoteService.this);
        API api = new API();
        String result = api.addOrder(orderDelivery);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
//                JSONArray comments = jsonResponse.getJSONArray("data");
//
//                for (int i = 0; i < comments.length(); i++) {
//                    JSONObject commentObj = comments.getJSONObject(i);
//                    Long id = commentObj.getLong("id");
//                    Integer productID = commentObj.getInt("productID");
//                    Integer userID = commentObj.getInt("userID");
//                    String comment = commentObj.optString("comment", "");
//                    String createDate = commentObj.optString("createDate", "0000-00-00 00:00:00");
//                    String firstName = commentObj.optString("firstName", "");
//                    String lastName = commentObj.optString("lastName", "");
//                    String image = commentObj.optString("image", "");
//
//                    ObjectComment objectComment = new ObjectComment(id, productID, userID, comment, createDate, firstName, lastName, image);
//                    CommentController.insertOrUpdate(AntidoteService.this, objectComment);
//                }

                String msg = jsonResponse.optString("msg", "");
                RESULT_MESSAGE = msg;
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

    private boolean getBanner() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getBanner();
        BannerController.clearAllBanners(AntidoteService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray groups = jsonResponse.getJSONArray("data");

                for (int i = 0; i < groups.length(); i++) {
                    JSONObject group = groups.getJSONObject(i);
                    Long id = group.getLong("id");
                    String image = group.getString("url");
                    String title = group.getString("image");

                    ObjectBanner objectBanner = new ObjectBanner(id, image, title);
                    BannerController.insert(AntidoteService.this, objectBanner);
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

    private boolean getOrderHistoryByIdUser(String user_id) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getOrderHistoryByIdUser(user_id);
//        OrderHistoryController.clearAllOrderHistories(AntidoteService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray order_histories = jsonResponse.getJSONArray("data");

                for (int i = 0; i < order_histories.length(); i++) {
                    JSONObject order_history = order_histories.getJSONObject(i);
                    Long id = order_history.getLong("id");
                    String str_total = order_history.optString("total", "0");
                    if (str_total.equals("null")) str_total = "0";
                    Float total = Float.parseFloat(str_total);
                    String createDate = order_history.optString("createDate", "0000-00-00 00:00:00");
                    if (createDate.equals("null")) createDate = "0000-00-00 00:00:00";

                    ObjectOrderHistory orderHistory = new ObjectOrderHistory(id, total, createDate);
                    OrderHistoryController.insertOrUpdate(AntidoteService.this, orderHistory);
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

    private boolean getCouponByCode(String code_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getCouponByCode(code_input);
        CouponController.clearAllCoupons(AntidoteService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray coupons = jsonResponse.getJSONArray("data");

                for (int i = 0; i < coupons.length(); i++) {
                    JSONObject coupon = coupons.getJSONObject(i);
                    Long id = coupon.getLong("id");
                    String code = coupon.getString("code");
                    Integer discount_type = coupon.getInt("discount_type");
                    Float discount_percent = Float.parseFloat(coupon.getString("discount_percent"));
                    Float discount_amount = Float.parseFloat(coupon.getString("discount_amount"));
                    Float minimum_amount = Float.parseFloat(coupon.getString("minimum_amount"));
                    Integer active = coupon.getInt("isActive");

                    ObjectCoupon objectCoupon = new ObjectCoupon(id, code, discount_type, discount_percent, discount_amount, minimum_amount, active);
                    CouponController.insert(AntidoteService.this, objectCoupon);
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

    private boolean getAllFaq() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllFaq();
        FaqController.clearAllFaqs(AntidoteService.this);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray faqs = jsonResponse.getJSONArray("data");

                for (int i = 0; i < faqs.length(); i++) {
                    JSONObject faq = faqs.getJSONObject(i);
                    Long id = faq.getLong("id");
                    Long userID = faq.getLong("userID");
                    String question = faq.getString("question");
                    String answer = faq.getString("answer");
                    if (answer.equalsIgnoreCase("null")) answer = "";

                    ObjectFaq objectFaq = new ObjectFaq(id, userID, question, answer);
                    FaqController.insert(AntidoteService.this, objectFaq);
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

    private boolean getAllFaqV2() {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllFaqV2();
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                FaqV2Controller.clearAllFaqV2s(AntidoteService.this);
                JSONArray faqs = jsonResponse.getJSONArray("data");

                for (int i = 0; i < faqs.length(); i++) {
                    JSONObject faq = faqs.getJSONObject(i);
                    Long id = faq.getLong("id");
                    String question = faq.getString("question");
                    String answer = faq.getString("answer");
                    if (answer.equalsIgnoreCase("null")) answer = "";
                    Integer sort = faq.getInt("sort");

                    ObjectFaqV2 objectFaqV2 = new ObjectFaqV2(id, question, answer, sort);
                    FaqV2Controller.insert(AntidoteService.this, objectFaqV2);
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

    private boolean addFaq(String userid, String question_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.addFaq(userid, question_input);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray faqs = jsonResponse.getJSONArray("data");

                for (int i = 0; i < faqs.length(); i++) {
                    JSONObject faq = faqs.getJSONObject(i);
                    Long id = faq.getLong("id");
                    Long userID = faq.getLong("userID");
                    String question = faq.getString("question");
                    String answer = faq.getString("answer");
                    if (answer.equalsIgnoreCase("null")) answer = "";

                    ObjectFaq objectFaq = new ObjectFaq(id, userID, question, answer);
                    FaqController.insert(AntidoteService.this, objectFaq);
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

    private boolean getAllFaqCommentByFaqId(String faqsid) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.getAllFaqCommentByFaqId(faqsid);
        if (faqsid.length() > 0) {
            FaqCommentController.clearAllFaqCommentsByFaqId(AntidoteService.this, Long.parseLong(faqsid));
        }
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray comments = jsonResponse.getJSONArray("data");

                for (int i = 0; i < comments.length(); i++) {
                    JSONObject commentObj = comments.getJSONObject(i);
                    Long id = commentObj.getLong("id");
                    Long faqsID = commentObj.getLong("faqsID");
                    Long userID = commentObj.getLong("userID");
                    String comment = commentObj.optString("comment", "");
                    String firstName = commentObj.optString("firstName", "");
                    if (firstName.equalsIgnoreCase("null")) firstName = "";
                    String lastName = commentObj.optString("lastName", "");
                    if (lastName.equalsIgnoreCase("null")) lastName = "";
                    String image = commentObj.optString("image", "");
                    String idFacebook = commentObj.optString("idFacebook", "");
                    if (idFacebook.equalsIgnoreCase("null")) idFacebook = "";
                    String createDate = commentObj.optString("createDate", "");

                    ObjectFaqComment objectFaqComment = new ObjectFaqComment(id, faqsID, userID, comment, firstName, lastName, image, idFacebook, createDate);
                    FaqCommentController.insert(AntidoteService.this, objectFaqComment);
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

    private boolean addFaqComment(String userid, String faqsid, String comment_input) {
        boolean isSuccess = false;
        API api = new API();
        String result = api.addFaqComment(userid, faqsid, comment_input);
        JSONObject jsonResponse;

        try {
            jsonResponse = new JSONObject(result);
            boolean success = jsonResponse.getBoolean("success");

            if (success) {
                JSONArray comments = jsonResponse.getJSONArray("data");

                for (int i = 0; i < comments.length(); i++) {
                    JSONObject commentObj = comments.getJSONObject(i);
                    Long id = commentObj.getLong("id");
                    Long faqsID = commentObj.getLong("faqsID");
                    Long userID = commentObj.getLong("userID");
                    String comment = commentObj.optString("comment", "");
                    String firstName = commentObj.optString("firstName", "");
                    if (firstName.equalsIgnoreCase("null")) firstName = "";
                    String lastName = commentObj.optString("lastName", "");
                    if (lastName.equalsIgnoreCase("null")) lastName = "";
                    String image = commentObj.optString("image", "");
                    String idFacebook = commentObj.optString("idFacebook", "");
                    if (idFacebook.equalsIgnoreCase("null")) idFacebook = "";
                    String createDate = commentObj.optString("createDate", "");

                    ObjectFaqComment objectFaqComment = new ObjectFaqComment(id, faqsID, userID, comment, firstName, lastName, image, idFacebook, createDate);
                    FaqCommentController.insert(AntidoteService.this, objectFaqComment);
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
