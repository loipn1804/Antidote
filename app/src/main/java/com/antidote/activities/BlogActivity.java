package com.antidote.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import sg.antidote.R;

/**
 * Created by USER on 7/16/2015.
 */
public class BlogActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private WebView wvBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        overrideFontsRegular(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        wvBlog = (WebView) findViewById(R.id.wvBlog);

        rltLeft.setOnClickListener(this);
    }

    private void initData() {
        wvBlog.getSettings().setJavaScriptEnabled(true);
        wvBlog.loadUrl("http://antidote.sg/blog/");
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
