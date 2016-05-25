package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Html;

import sg.antidote.R;
import com.antidote.service.AntidoteService;
import com.facebook.Session;

/**
 * Created by USER on 5/13/2015.
 */
public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private int SIGNUP_REQUEST = 12345;

    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtFirst;
    private EditText edtLast;
    private TextView txtTerm;
    private TextView txtTerm1;
    private TextView txtSignup;
    private LinearLayout lnlSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_SIGNUP);
            intentFilter.addAction(AntidoteService.RECEIVER_LOGIN);
            registerReceiver(activityReceiver, intentFilter);
        }

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            unregisterReceiver(activityReceiver);
        }
    }

    private void initView() {
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        edtFirst = (EditText) findViewById(R.id.edt_firstname);
        edtLast = (EditText) findViewById(R.id.edt_lastname);
        txtTerm = (TextView) findViewById(R.id.txtTerm);
        txtTerm1 = (TextView) findViewById(R.id.txtTerm1);
        txtSignup = (TextView) findViewById(R.id.txtSignup);
        lnlSignup = (LinearLayout) findViewById(R.id.lnlSignup);

        lnlSignup.setOnClickListener(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Regular.otf");
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/DINNextLTPro-Heavy.otf");
        edtEmail.setTypeface(typeface);
        edtPass.setTypeface(typeface);
        edtFirst.setTypeface(typeface);
        edtLast.setTypeface(typeface);
        txtTerm.setTypeface(typefaceBold);
        txtTerm.setText(Html.fromHtml("<a href=\"http://antidote.sg/terms/\"> Terms of use</a>"));
        txtTerm.setOnClickListener(this);
        txtTerm1.setTypeface(typeface);
        txtSignup.setTypeface(typeface);

        edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        // done stuff
                        signup();
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnlSignup:
                signup();
                break;
            case R.id.txtTerm:
                String url = "http://antidote.sg/terms/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }

    private void signup() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String first = edtFirst.getText().toString().trim();
        String last = edtLast.getText().toString().trim();
        if (email.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.blank_email), Toast.LENGTH_LONG).show();
        } else if (pass.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.blank_pass), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(SignupActivity.this, AntidoteService.class);
            intent.setAction(AntidoteService.ACTION_SIGNUP);
            intent.putExtra(AntidoteService.EXTRA_EMAIL, email);
            intent.putExtra(AntidoteService.EXTRA_PASS, pass);
            intent.putExtra(AntidoteService.EXTRA_FIRSTNAME, first);
            intent.putExtra(AntidoteService.EXTRA_LASTNAME, last);
            startService(intent);
            showProgressDialog();
        }
    }

    public void showPopupPromptSignUpSuccess(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(SignupActivity.this);

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
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("email", edtEmail.getText().toString().trim());
//                SignupActivity.this.setResult(RESULT_OK, returnIntent);
//                finish();

                Intent intent = new Intent(SignupActivity.this, AntidoteService.class);
                intent.setAction(AntidoteService.ACTION_LOGIN);
                intent.putExtra(AntidoteService.EXTRA_EMAIL, edtEmail.getText().toString().trim());
                intent.putExtra(AntidoteService.EXTRA_PASS, edtPass.getText().toString().trim());
                startService(intent);
                showProgressDialog();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_SIGNUP)) {
            String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
            if (result.equals(AntidoteService.RESULT_OK)) {
                hideProgressDialog();
                showPopupPromptSignUpSuccess("Account created successfully");
            } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                hideProgressDialog();
            } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_LOGIN)) {
            String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
            if (result.equals(AntidoteService.RESULT_OK)) {
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
        }
        }
    };
}
