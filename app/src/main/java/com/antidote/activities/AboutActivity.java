package com.antidote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import sg.antidote.R;

/**
 * Created by USER on 6/25/2015.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        initView();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);

        rltLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
        }
    }
}
