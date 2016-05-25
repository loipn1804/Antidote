package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.antidote.adapters.FaqCommentAdapter;
import com.antidote.daocontroller.FaqCommentController;
import com.antidote.daocontroller.FaqController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectFaq;
import antidote.ObjectFaqComment;
import antidote.ObjectUser;

/**
 * Created by USER on 7/7/2015.
 */
public class FaqCommentActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private List<ObjectFaqComment> listFaqComment;
    private FaqCommentAdapter faqCommentAdapter;
    private ListView lvComment;
    private TextView txtQuestion;
    private TextView txtAnswer;
    private TextView txtNumComment;
    private EditText edtReview;
    private TextView txtSendReview;

    private ObjectFaq objectFaq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_comment);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_ALL_FAQ_COMMENT);
            intentFilter.addAction(AntidoteService.RECEIVER_ADD_FAQ_COMMENT);
            registerReceiver(activityReceiver, intentFilter);
        }

        listFaqComment = new ArrayList<ObjectFaqComment>();
        faqCommentAdapter = new FaqCommentAdapter(FaqCommentActivity.this, listFaqComment);

        if (getIntent().getExtras() != null) {
            Long idFaq = getIntent().getLongExtra("faq_id", 0);
            objectFaq = FaqController.getObjectFaqById(FaqCommentActivity.this, idFaq);

            Intent intentGetFaqComment = new Intent(AntidoteService.ACTION_GET_ALL_FAQ_COMMENT, null, FaqCommentActivity.this, AntidoteService.class);
            intentGetFaqComment.putExtra(AntidoteService.EXTRA_FAQ_ID, idFaq + "");
            startService(intentGetFaqComment);
        }

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        lvComment = (ListView) findViewById(R.id.lvComment);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        txtAnswer = (TextView) findViewById(R.id.txtAnswer);
        txtNumComment = (TextView) findViewById(R.id.txtNumComment);
        edtReview = (EditText) findViewById(R.id.edtReview);
        txtSendReview = (TextView) findViewById(R.id.txtSendReview);

        rltLeft.setOnClickListener(this);
        txtSendReview.setOnClickListener(this);

        lvComment.setAdapter(faqCommentAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private void initData() {
        if (objectFaq != null) {
            txtQuestion.setText(objectFaq.getQuestion());

            if (objectFaq.getAnswer().length() == 0) {
                txtAnswer.setText("This question has no answer this moment.");
            } else {
                txtAnswer.setText(objectFaq.getAnswer());
            }

            if (objectFaq != null) {
                setListFaqComment();
            }
        }
    }

    private void setListFaqComment() {
        listFaqComment.clear();
        List<ObjectFaqComment> list = FaqCommentController.getFaqCommentsByFaqId(FaqCommentActivity.this, objectFaq.getId());
        for (ObjectFaqComment faqComment : list) {
            listFaqComment.add(faqComment);
        }
        if (list.size() == 0) {
            showProgressDialog();
        }
        if (list.size() == 1) {
            txtNumComment.setText("1 COMMENT");
        } else {
            txtNumComment.setText(list.size() + " COMMENTS");
        }
        faqCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.txtSendReview:
                if (UserController.isLogin(FaqCommentActivity.this)) {
                    if (objectFaq != null) {
                        addFaqComment(objectFaq.getId() + "");
                    }
                } else {
                    showPopupConfirmLogin();
                }
                break;
        }
    }

    private void addFaqComment(String faqid) {
        ObjectUser user = UserController.getCurrentUser(FaqCommentActivity.this);
        if (user != null) {
            String comment = edtReview.getText().toString().trim();
            if (comment.length() == 0) {
                showToast("Please enter comment.");
            } else {
                Intent intentAddNew = new Intent(FaqCommentActivity.this, AntidoteService.class);
                intentAddNew.setAction(AntidoteService.ACTION_ADD_FAQ_COMMENT);
                intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_COMMENT_USER_ID, user.getId() + "");
                intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_COMMENT_FAQ_ID, faqid);
                intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_COMMENT, comment);
                startService(intentAddNew);
                showProgressDialog();
            }
        }
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(FaqCommentActivity.this);

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
                Intent intentLogin = new Intent(FaqCommentActivity.this, LoginActivity.class);
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

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_ALL_FAQ_COMMENT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    setListFaqComment();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_ADD_FAQ_COMMENT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    setListFaqComment();
                    hideProgressDialog();
                    edtReview.setText("");
                    StaticFunction.hideKeyboard(FaqCommentActivity.this);
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
