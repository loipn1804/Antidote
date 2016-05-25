package com.antidote.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.antidote.staticfunction.StaticFunction;

import sg.antidote.R;

/**
 * Created by USER on 5/13/2015.
 */
public class BaseActivity extends ActionBarActivity {

    private ProgressDialog progress_dialog;
    private Toast toast;

    public void showProgressDialog() {
        if (progress_dialog == null) {
            progress_dialog = new ProgressDialog(BaseActivity.this);
        }

        if (!progress_dialog.isShowing()) {
            progress_dialog.setMessage("Loading ... ");
            progress_dialog.setCancelable(true);
            progress_dialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progress_dialog != null && progress_dialog.isShowing()) {
            progress_dialog.dismiss();
        }
    }

    public void showPopupPrompt(String message) {
        // custom dialog
        final Dialog dialog = new Dialog(BaseActivity.this);

//        dialog.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_prompt);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(BaseActivity.this, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    public void overrideFontsHeavy(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsHeavy(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.heavy(this));
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsLight(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLight(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.light(this));
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsMedium(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsMedium(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.medium(this));
            }
        } catch (Exception e) {
        }
    }

    public void overrideFontsRegular(View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsRegular(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.regular(this));
            }
        } catch (Exception e) {
        }
    }
}
