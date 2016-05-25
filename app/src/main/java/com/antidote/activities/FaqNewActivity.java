package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;
import com.antidote.adapters.FaqCommentAdapter;
import com.antidote.daocontroller.FaqCommentController;
import com.antidote.daocontroller.FaqController;
import com.antidote.daocontroller.FaqV2Controller;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectFaq;
import antidote.ObjectFaqComment;
import antidote.ObjectFaqV2;
import antidote.ObjectUser;

/**
 * Created by USER on 7/8/2015.
 */
public class FaqNewActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private LinearLayout lnlListFaq;
    private List<ObjectFaqV2> listFaq;
    private List<Boolean> listIsExpand;

    private List<ObjectFaqComment> listFaqComment;
    private FaqCommentAdapter faqCommentAdapter;

    private RelativeLayout rltAddNew;

    private Dialog dialogAddNew;
    private EditText edtReview;
    private TextView txtSendReview;
    private TextView txtNameActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_ALL_FAQ_V2);
//            intentFilter.addAction(AntidoteService.RECEIVER_GET_ALL_FAQ_COMMENT);
//            intentFilter.addAction(AntidoteService.RECEIVER_ADD_FAQ);
//            intentFilter.addAction(AntidoteService.RECEIVER_ADD_FAQ_COMMENT);
            registerReceiver(activityReceiver, intentFilter);
        }

        Intent intentGetFaq = new Intent(AntidoteService.ACTION_GET_ALL_FAQ_V2, null, FaqNewActivity.this, AntidoteService.class);
        startService(intentGetFaq);

        listFaq = new ArrayList<ObjectFaqV2>();
        listIsExpand = new ArrayList<Boolean>();
        listFaqComment = new ArrayList<ObjectFaqComment>();
        faqCommentAdapter = new FaqCommentAdapter(FaqNewActivity.this, listFaqComment);

        initView();
        setListFaq();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        lnlListFaq = (LinearLayout) findViewById(R.id.lnlListFaq);
        rltAddNew = (RelativeLayout) findViewById(R.id.rltAddNew);
        edtReview = (EditText) findViewById(R.id.edtReview);
        txtSendReview = (TextView) findViewById(R.id.txtSendReview);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);
        txtNameActionBar.setText(" FAQ ");

        rltLeft.setOnClickListener(this);
        rltAddNew.setOnClickListener(this);
        txtSendReview.setOnClickListener(this);
        rltAddNew.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltAddNew:
                if (UserController.isLogin(FaqNewActivity.this)) {
                    showPopupAddFaq();
                } else {
                    showPopupConfirmLogin();
                }
                break;
            case R.id.txtSendReview:
                if (UserController.isLogin(FaqNewActivity.this)) {
                    checkCanAddComment();
                } else {
                    showPopupConfirmLogin();
                }
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

    private void showPopupAddFaq() {
        // custom dialog
        dialogAddNew = new Dialog(FaqNewActivity.this);

        dialogAddNew.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddNew.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialogAddNew.setCanceledOnTouchOutside(true);
        dialogAddNew.setContentView(R.layout.popup_add_new_question);

        overrideFontsLight(dialogAddNew.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialogAddNew.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialogAddNew.findViewById(R.id.lnlCancel);
        final EditText edtQuestion = (EditText) dialogAddNew.findViewById(R.id.edtQuestion);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StaticFunction.showKeyboard(FaqNewActivity.this);
            }
        }, 100);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectUser user = UserController.getCurrentUser(FaqNewActivity.this);
                if (user != null) {
                    String question = edtQuestion.getText().toString().trim();
                    if (question.length() == 0) {
                        showToast("Please enter question.");
                    } else {
                        Intent intentAddNew = new Intent(FaqNewActivity.this, AntidoteService.class);
                        intentAddNew.setAction(AntidoteService.ACTION_ADD_FAQ);
                        intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_USER_ID, user.getId() + "");
                        intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_QUESTION, question);
                        startService(intentAddNew);
                        showProgressDialog();
                    }
                }
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddNew.dismiss();
            }
        });

        dialogAddNew.show();
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(FaqNewActivity.this);

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
                Intent intentLogin = new Intent(FaqNewActivity.this, LoginActivity.class);
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

    private void checkCanAddComment() {
        boolean b = false;
        for (int i = 0; i < listIsExpand.size(); i++) {
            if (listIsExpand.get(i)) {
                b = true;
                addFaqComment(listFaq.get(i).getId() + "");
                break;
            }
        }
        if (!b) {
            showPopupPrompt("Please choose a question to write comment.");
        }
    }

    private void addFaqComment(String faqid) {
        ObjectUser user = UserController.getCurrentUser(FaqNewActivity.this);
        if (user != null) {
            String comment = edtReview.getText().toString().trim();
            if (comment.length() == 0) {
                showToast("Please enter comment.");
            } else {
                Intent intentAddNew = new Intent(FaqNewActivity.this, AntidoteService.class);
                intentAddNew.setAction(AntidoteService.ACTION_ADD_FAQ_COMMENT);
                intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_COMMENT_USER_ID, user.getId() + "");
                intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_COMMENT_FAQ_ID, faqid);
                intentAddNew.putExtra(AntidoteService.EXTRA_FAQ_COMMENT, comment);
                startService(intentAddNew);
                showProgressDialog();
            }
        }
    }

    private void setListFaq() {
        listFaq.clear();
        listIsExpand.size();
        final List<ObjectFaqV2> list = FaqV2Controller.getAllFaqV2s(FaqNewActivity.this);
        for (ObjectFaqV2 faq : list) {
            listFaq.add(faq);
            listIsExpand.add(false);
        }

        if (list.size() == 0) {
            showProgressDialog();
        }

        lnlListFaq.removeAllViews();
        for (int i = 0; i < listFaq.size(); i++) {
            final ObjectFaqV2 faq = listFaq.get(i);
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_faq_new, null);
            LinearLayout lnlQuestion = (LinearLayout) viewRow.findViewById(R.id.lnlQuestion);
            TextView txtQuestion = (TextView) viewRow.findViewById(R.id.txtQuestion);
            ImageView imvExpand = (ImageView) viewRow.findViewById(R.id.imvExpand);
//            TextView txtQuestionFull = (TextView) viewRow.findViewById(R.id.txtQuestionFull);
            TextView txtAnswer = (TextView) viewRow.findViewById(R.id.txtAnswer);
//            TextView txtNumComment = (TextView) viewRow.findViewById(R.id.txtNumComment);
//            ListView lvContent = (ListView) viewRow.findViewById(R.id.lvContent);
            LinearLayout lnlShowmore = (LinearLayout) viewRow.findViewById(R.id.lnlShowmore);

            txtQuestion.setText(faq.getQuestion().trim());
//            txtQuestionFull.setText(faq.getQuestion());

            if (listIsExpand.get(i)) { // expanse
//                lnlQuestion.setBackgroundColor(getResources().getColor(R.color.blue_home));
                imvExpand.setImageResource(R.drawable.ic_arrow_blue_bottom);
                txtAnswer.setVisibility(View.VISIBLE);
//                txtQuestionFull.setVisibility(View.VISIBLE);
                lnlShowmore.setVisibility(View.INVISIBLE);
//                txtNumComment.setVisibility(View.VISIBLE);
//                lvContent.setVisibility(View.VISIBLE);

                if (faq.getAnswer().length() == 0) {
                    txtAnswer.setText("This question has no answer this moment.");
                } else {
                    txtAnswer.setText(faq.getAnswer().trim());
                }

//                Intent intentGetFaqComment = new Intent(AntidoteService.ACTION_GET_ALL_FAQ_COMMENT, null, FaqNewActivity.this, AntidoteService.class);
//                intentGetFaqComment.putExtra(AntidoteService.EXTRA_FAQ_ID, faq.getId() + "");
//                startService(intentGetFaqComment);

//                setListFaqComment(lvContent, txtNumComment, faq.getId());
            } else {
//                lnlQuestion.setBackgroundColor(getResources().getColor(R.color.bg_row_faq));
                imvExpand.setImageResource(R.drawable.ic_arrow_blue_right);
                txtAnswer.setVisibility(View.GONE);
//                txtQuestionFull.setVisibility(View.GONE);
                lnlShowmore.setVisibility(View.GONE);
//                txtNumComment.setVisibility(View.GONE);
//                lvContent.setVisibility(View.GONE);

                if (faq.getAnswer().length() == 0) {
                    txtAnswer.setText("This question has no answer this moment.");
                } else {
                    txtAnswer.setText(faq.getAnswer().trim());
                }
            }

            final int j = i;

            lnlQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int k = 0; k < listFaq.size(); k++) {
                        if (k != j) {
                            listIsExpand.set(k, false);
                        }
                    }
                    listIsExpand.set(j, !(listIsExpand.get(j)));
                    notifyDataListFaq(faq);
                }
            });

//            lnlShowmore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intentFaqComment = new Intent(FaqNewActivity.this, FaqCommentActivity.class);
//                    intentFaqComment.putExtra("faq_id", faq.getId());
//                    startActivity(intentFaqComment);
//                }
//            });

            lnlListFaq.addView(viewRow);
        }
    }

    private void notifyDataListFaq(ObjectFaqV2 faq) {
        for (int i = 0; i < lnlListFaq.getChildCount(); i++) {
            View viewRow = lnlListFaq.getChildAt(i);
            LinearLayout lnlQuestion = (LinearLayout) viewRow.findViewById(R.id.lnlQuestion);
            ImageView imvExpand = (ImageView) viewRow.findViewById(R.id.imvExpand);
            TextView txtAnswer = (TextView) viewRow.findViewById(R.id.txtAnswer);
//            TextView txtQuestionFull = (TextView) viewRow.findViewById(R.id.txtQuestionFull);
            LinearLayout lnlShowmore = (LinearLayout) viewRow.findViewById(R.id.lnlShowmore);
//            ListView lvContent = (ListView) viewRow.findViewById(R.id.lvContent);
//            TextView txtNumComment = (TextView) viewRow.findViewById(R.id.txtNumComment);

            if (listIsExpand.get(i)) { // expanse
//                lnlQuestion.setBackgroundColor(getResources().getColor(R.color.blue_home));
                imvExpand.setImageResource(R.drawable.ic_arrow_blue_bottom);
                txtAnswer.setVisibility(View.VISIBLE);
//                txtQuestionFull.setVisibility(View.VISIBLE);
                lnlShowmore.setVisibility(View.INVISIBLE);
//                txtNumComment.setVisibility(View.VISIBLE);
//                lvContent.setVisibility(View.VISIBLE);

                if (faq.getAnswer().length() == 0) {
                    txtAnswer.setText("This question has no answer this moment.");
                } else {
                    txtAnswer.setText(faq.getAnswer().trim());
                }

//                Intent intentGetFaqComment = new Intent(AntidoteService.ACTION_GET_ALL_FAQ_COMMENT, null, FaqNewActivity.this, AntidoteService.class);
//                intentGetFaqComment.putExtra(AntidoteService.EXTRA_FAQ_ID, faq.getId() + "");
//                startService(intentGetFaqComment);

//                setListFaqComment(lvContent, txtNumComment, faq.getId());
            } else {
//                lnlQuestion.setBackgroundColor(getResources().getColor(R.color.bg_row_faq));
                imvExpand.setImageResource(R.drawable.ic_arrow_blue_right);
                txtAnswer.setVisibility(View.GONE);
//                txtQuestionFull.setVisibility(View.GONE);
                lnlShowmore.setVisibility(View.GONE);
//                txtNumComment.setVisibility(View.GONE);
//                lvContent.setVisibility(View.GONE);

                if (faq.getAnswer().length() == 0) {
                    txtAnswer.setText("This question has no answer this moment.");
                } else {
                    txtAnswer.setText(faq.getAnswer().trim());
                }
            }
        }
    }

    private void setListFaqComment(ListView listView, TextView txtNumComment, Long faqid) {
        listView.setAdapter(faqCommentAdapter);
        listFaqComment.clear();
        List<ObjectFaqComment> list = FaqCommentController.getFaqCommentsByFaqId(FaqNewActivity.this, faqid);
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
        setListViewHeightBasedOnChildren(listView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void chooseListFaqComment() {
        for (int i = 0; i < lnlListFaq.getChildCount(); i++) {
            if (listIsExpand.get(i)) {
                View viewRow = lnlListFaq.getChildAt(i);
                ListView lvContent = (ListView) viewRow.findViewById(R.id.lvContent);
                TextView txtNumComment = (TextView) viewRow.findViewById(R.id.txtNumComment);
                setListFaqComment(lvContent, txtNumComment, listFaq.get(i).getId());
            }
        }
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_ALL_FAQ_V2)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    setListFaq();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_ALL_FAQ_COMMENT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    chooseListFaqComment();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_ADD_FAQ)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    setListFaq();
                    hideProgressDialog();
                    StaticFunction.hideKeyboard(FaqNewActivity.this);
                    dialogAddNew.dismiss();
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
                    chooseListFaqComment();
                    hideProgressDialog();
                    edtReview.setText("");
                    StaticFunction.hideKeyboard(FaqNewActivity.this);
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
