package com.antidote.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by USER on 5/13/2015.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final List<String> fbPermissions = Arrays.asList("public_profile", "email");
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private GraphUser userFb;

    private int SIGNUP_REQUEST = 12345;

    private EditText edtEmail;
    private EditText edtPass;
    private TextView txtLogin;
    private TextView txtForgotPass;
    private TextView txtRegisterLater;
    private TextView txtNotAccount;
    private TextView txtSignup;
    private LinearLayout lnlLogin;
    private LinearLayout lnlLoginFb;
    private LinearLayout lnlSignup;

    private Dialog dialogForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

//        getKeHash(this);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_LOGIN);
            intentFilter.addAction(AntidoteService.RECEIVER_LOGIN_FB);
            intentFilter.addAction(AntidoteService.RECEIVER_FORGOT_PASS);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
    }

    private void initView() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        txtForgotPass = (TextView) findViewById(R.id.txtForgotPass);
        txtRegisterLater = (TextView) findViewById(R.id.txtRegisterLater);
        txtNotAccount = (TextView) findViewById(R.id.txtNotAccount);
        txtSignup = (TextView) findViewById(R.id.txtSignup);
        lnlLogin = (LinearLayout) findViewById(R.id.lnlLogin);
        lnlLoginFb = (LinearLayout) findViewById(R.id.lnlLoginfb);
        lnlSignup = (LinearLayout) findViewById(R.id.lnlSignup);

        txtForgotPass.setOnClickListener(this);
        txtRegisterLater.setOnClickListener(this);
        lnlLogin.setOnClickListener(this);
        lnlLoginFb.setOnClickListener(this);
        lnlSignup.setOnClickListener(this);
        txtNotAccount.setOnClickListener(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Regular.otf");
        edtEmail.setTypeface(typeface);
        edtPass.setTypeface(typeface);
        txtLogin.setTypeface(typeface);
        txtForgotPass.setTypeface(typeface);
        txtRegisterLater.setTypeface(typeface);
        txtNotAccount.setTypeface(typeface);
        txtSignup.setTypeface(typeface);

        edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        // done stuff
                        login();
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });

        SpannableString content = new SpannableString(txtNotAccount.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtNotAccount.setText(content);

        content = new SpannableString(txtRegisterLater.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtRegisterLater.setText(content);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (CountryController.getAllCountries(LoginActivity.this).size() == 0) {
//            Intent intentGetCountry = new Intent(AntidoteService.ACTION_GET_ALL_COUNTRY, null, LoginActivity.this, AntidoteService.class);
//            startService(intentGetCountry);
//        }
//
//        if (BannerController.getAllBanners(LoginActivity.this).size() == 0) {
//            Intent intentGetBanner = new Intent(AntidoteService.ACTION_GET_BANNER, null, LoginActivity.this, AntidoteService.class);
//            startService(intentGetBanner);
//        }
//
//        Intent intentGetAllProduct = new Intent(AntidoteServiceParal.ACTION_GET_ALL_PRODUCT, null, LoginActivity.this, AntidoteServiceParal.class);
//        startService(intentGetAllProduct);
//
//        Intent intentGetOption = new Intent(AntidoteServiceParal.ACTION_GET_OPTION, null, LoginActivity.this, AntidoteServiceParal.class);
//        startService(intentGetOption);
//
//        Intent intentGetProductOption = new Intent(AntidoteServiceParal.ACTION_GET_PRODUCT_OPTION, null, LoginActivity.this, AntidoteServiceParal.class);
//        startService(intentGetProductOption);
//
//        List<ObjectGroupTimer> list = GroupTimerController.getAllGroupTimers(LoginActivity.this);
//        for (ObjectGroupTimer groupTimer : list) {
//            long current_time = System.currentTimeMillis();
//            ObjectGroupTimer groupTimerNext = GroupTimerController.getNextGroupTimerByTime(LoginActivity.this, groupTimer.getGroupID(), current_time);
//            if (groupTimerNext != null) {
//                Intent intentStartAlarm = new Intent(LoginActivity.this, AntidoteTimerService.class);
//                intentStartAlarm.setAction(AntidoteTimerService.ACTION_START_ALARM);
//                intentStartAlarm.putExtra(AntidoteTimerService.EXTRA_GROUP_ID, groupTimer.getGroupID());
//                startService(intentStartAlarm);
//            }
//        }

        boolean isLogin = UserController.isLogin(LoginActivity.this);
        if (isLogin) {
            Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intentHome);
            finish();
        } else {
            SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
            String email = preferences.getString("email", "");
            if (email.length() > 0) {
                edtEmail.setText(email);
            }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtForgotPass:
                showPopupChangePassword();
                break;
            case R.id.txtRegisterLater:
                finish();
                break;
            case R.id.lnlLogin:
                login();
                break;
            case R.id.lnlLoginfb:
                if (StaticFunction.isNetworkAvailable(LoginActivity.this)) {
                    if (StaticFunction.checkFbInstalled(LoginActivity.this)) {
                        getFacebookAuth();
                    } else {
                        showPopupInstallFb();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtNotAccount:
                Intent intentSignup = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intentSignup, SIGNUP_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SIGNUP_REQUEST) {
            edtEmail.setText(data.getStringExtra("email"));
        }

        if (Session.getActiveSession() != null) {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
            //showProgressDialog();
        }
    }

    private void getFacebookAuth() {
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            session.openForRead(new Session.OpenRequest(this).setPermissions(fbPermissions).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, fbPermissions, statusCallback);
        }
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            // Respond to session state changes, ex: updating the view
            if (session != null) {
                if (session.isOpened()) {
//                    List<String> declinedPer = session.getDeclinedPermissions();
//                    if (declinedPer != null && !declinedPer.isEmpty()) {
//                        requestMissingPermissions(declinedPer);
//                    } else {
                    makeMeRequest(session);
//                    }
                } else if (state == SessionState.CLOSED_LOGIN_FAILED) {
                    session.closeAndClearTokenInformation();
                    Session.setActiveSession(null);
                }
            }
        }
    }

    private void requestMissingPermissions(final List<String> missingPer) {
        final Session session = Session.getActiveSession();
        if (session != null) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Errors")
                    .setMessage(
                            "Can not login due to some missing important informations, do you want to try again?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
                                    LoginActivity.this, missingPer);
                            session.requestNewReadPermissions(newPermissionsRequest);
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    session.closeAndClearTokenInformation();
                    Session.setActiveSession(null);
                    dialog.dismiss();
                }
            }).show();
        }
    }

    private void makeMeRequest(final Session session) {
//        showProgressDialog();
        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null && !user.getId().isEmpty()) {
                    userFb = user;
                    Intent intentLoginFb = new Intent(AntidoteService.ACTION_LOGIN_FB, null, LoginActivity.this, AntidoteService.class);
                    intentLoginFb.putExtra(AntidoteService.EXTRA_ID_FB, userFb.getId());
                    String email = userFb.getProperty("email") == null ? "" : String.valueOf(userFb.getProperty("email"));
                    intentLoginFb.putExtra(AntidoteService.EXTRA_EMAIL_FB, email);
                    intentLoginFb.putExtra(AntidoteService.EXTRA_FIRSTNAME_FB, userFb.getFirstName());
                    intentLoginFb.putExtra(AntidoteService.EXTRA_LASTNAME_FB, userFb.getLastName());
                    startService(intentLoginFb);
                    showProgressDialog();

//                    Toast.makeText(LoginActivity.this, userFb.getId() + " " + userFb.getFirstName(), Toast.LENGTH_LONG).show();
//                    showPopupPrompt(userFb.getId() + " " + userFb.getFirstName());
//                    String gender = userFb.getProperty("gender") == null ? "" : String.valueOf(userFb.getProperty("gender"));
//                    showPopupPrompt(userFb.getId() + " - " + userFb.getUsername() + " - " + userFb.getFirstName() + " - " + userFb.getLastName() + " - " + gender);

                } else if (response.getError() != null) {
//                    Toast.makeText(LoginActivity.this, " " + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void login() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        if (email.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.blank_email), Toast.LENGTH_LONG).show();
        } else if (pass.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.blank_pass), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(LoginActivity.this, AntidoteService.class);
            intent.setAction(AntidoteService.ACTION_LOGIN);
            intent.putExtra(AntidoteService.EXTRA_EMAIL, email);
            intent.putExtra(AntidoteService.EXTRA_PASS, pass);
            startService(intent);
            showProgressDialog();
        }
    }

    private void showPopupInstallFb() {
        // custom dialog
        final Dialog dialog = new Dialog(LoginActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMsg.setText("We need Facebook App installed to use this feature.");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.katana")));
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

    private void showPopupChangePassword() {
        // custom dialog
        dialogForgotPass = new Dialog(LoginActivity.this);

        dialogForgotPass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPass.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialogForgotPass.setCanceledOnTouchOutside(true);
        dialogForgotPass.setContentView(R.layout.popup_forgot_pass);

        overrideFontsLight(dialogForgotPass.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialogForgotPass.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialogForgotPass.findViewById(R.id.lnlCancel);
        final EditText edtEmail = (EditText) dialogForgotPass.findViewById(R.id.edtEmail);
        edtEmail.setText(this.edtEmail.getText().toString());

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (email.length() == 0) {
                    showToast("Please enter your email.");
                } else {
                    Intent intent = new Intent(LoginActivity.this, AntidoteService.class);
                    intent.setAction(AntidoteService.ACTION_FORGOT_PASS);
                    intent.putExtra(AntidoteService.EXTRA_EMAIL, email);
                    startService(intent);
                    showProgressDialog();
                }
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPass.dismiss();
            }
        });

        dialogForgotPass.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_LOGIN)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
//                    Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
//                    startActivity(intentHome);
                    hideProgressDialog();
                    finish();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                    Session session = Session.getActiveSession();
                    if (session != null) {
                        session.closeAndClearTokenInformation();
                        Session.setActiveSession(null);
                    }
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_LOGIN_FB)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
//                    Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
//                    startActivity(intentHome);
                    hideProgressDialog();
                    finish();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_FORGOT_PASS)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showPopupPrompt(message);
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                    hideProgressDialog();
                }
                dialogForgotPass.dismiss();
            }
        }
    };

    public static String getKeHash(Activity activity) {
        // Add code to print out the key hash
        String keyHash = "";
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo("sg.antidote",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", android.util.Base64.encodeToString(md.digest(),
                        android.util.Base64.DEFAULT));
                keyHash = android.util.Base64.encodeToString(md.digest(),
                        android.util.Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return keyHash;
    }
}
