package com.antidote.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sg.antidote.R;

/**
 * Created by USER on 6/29/2015.
 */
public class ContactActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private WebView wvContact;
    private LinearLayout lnlFb;
    private LinearLayout lnlInstagram;
    private LinearLayout lnlPhone;
    private LinearLayout lnlEmail;
    private TextView txtEmail;
    private TextView txtPhone;
    private LinearLayout lnlAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        wvContact = (WebView) findViewById(R.id.wvContact);
        lnlFb = (LinearLayout) findViewById(R.id.lnlFb);
        lnlInstagram = (LinearLayout) findViewById(R.id.lnlInstagram);
        lnlPhone = (LinearLayout) findViewById(R.id.lnlPhone);
        lnlEmail = (LinearLayout) findViewById(R.id.lnlEmail);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        lnlAddress = (LinearLayout) findViewById(R.id.lnlAddress);

        rltLeft.setOnClickListener(this);
        lnlFb.setOnClickListener(this);
        lnlInstagram.setOnClickListener(this);
        lnlPhone.setOnClickListener(this);
        lnlEmail.setOnClickListener(this);
        lnlAddress.setOnClickListener(this);
    }

    private void initData() {
        wvContact.getSettings().setJavaScriptEnabled(true);
        wvContact.loadUrl("http://app.antidote.sg/contactus.html");

        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        String contactEmail = preferences.getString("contactEmail", "");
        String contactPhone = preferences.getString("contactPhone", "");
        txtEmail.setText(contactEmail);
        txtPhone.setText(contactPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.lnlFb:
                Uri uriFb = Uri.parse("https://www.facebook.com/antidotelife");
                Intent intentFb = new Intent(Intent.ACTION_VIEW, uriFb);
                startActivity(intentFb);
                break;
            case R.id.lnlInstagram:
                Uri uriIn = Uri.parse("https://instagram.com/antidote_sg/?hl=en");
                Intent intentIn = new Intent(Intent.ACTION_VIEW, uriIn);
                startActivity(intentIn);
                break;
            case R.id.lnlPhone:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + txtPhone.getText().toString()));
                startActivity(callIntent);
                break;
            case R.id.lnlEmail:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + txtEmail.getText().toString())); // only email apps should handle this
//                intent.putExtra(Intent.EXTRA_EMAIL, txtEmail.getText().toString());
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.lnlAddress:
                Uri uri = Uri.parse("geo:0,0?q=46, Siglap Drive, Singapore 456171");
                showMap(uri);
                break;
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
