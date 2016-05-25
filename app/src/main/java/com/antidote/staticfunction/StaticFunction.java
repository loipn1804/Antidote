package com.antidote.staticfunction;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by USER on 5/23/2015.
 */
public class StaticFunction {

    //    public static final String URL = "http://api.antidote.cloudshop.vn/";
//    public static final String URL = "http://antidote.cloudshop.vn/";
    public static final String URL = "http://app.antidote.sg/";

    public static boolean isLogout = false;

    public static boolean isBackToHome = false;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static float getDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    public static void showKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        } catch (Exception e) {

        }
    }

    public static boolean checkFbInstalled(Activity activity) {
        PackageManager pm = activity.getPackageManager();
        boolean flag = false;
        try {
            pm.getPackageInfo("com.facebook.katana",
                    PackageManager.GET_ACTIVITIES);
            flag = true;
        } catch (PackageManager.NameNotFoundException e) {
            flag = false;
        }
        return flag;
    }

    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 6 || target.length() > 13) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    public static void overrideFontsHeavy(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsHeavy(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.heavy(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsLight(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsLight(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.light(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsMedium(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsMedium(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.medium(context));
            }
        } catch (Exception e) {
        }
    }

    public static void overrideFontsRegular(Context context, View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFontsRegular(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(StaticFunction.regular(context));
            }
        } catch (Exception e) {
        }
    }

    public static Typeface heavy(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Heavy.otf");
        return typeface;
    }

    public static Typeface light(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Light.otf");
        return typeface;
    }

    public static Typeface medium(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Medium.otf");
        return typeface;
    }

    public static Typeface regular(Context activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/DINNextLTPro-Regular.otf");
        return typeface;
    }
}
