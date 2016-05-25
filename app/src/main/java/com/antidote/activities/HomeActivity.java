package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.adapters.HomeContentAdapter;
import com.antidote.adapters.ImageViewFragmentAdapter;
import com.antidote.daocontroller.BannerController;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;
import com.facebook.Session;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectBanner;
import antidote.ObjectCart;
import antidote.ObjectCategory;
import antidote.ObjectGroupProduct;
import antidote.ObjectUser;

/**
 * Created by USER on 5/14/2015.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private ViewPager pager;
    private CirclePageIndicator indicator;
    private ImageViewFragmentAdapter adapter;
    private CountDownTimer timer;
    private HomeContentAdapter contentAdapter;
    private List<ObjectCategory> listCategory;

    private RelativeLayout rltActionbarHome;
    private RelativeLayout rltActionbarSearch;
    private RelativeLayout rltActionbarCart;
    private LinearLayout lnlRoot;
    private LinearLayout lnlMenuShop;
    private LinearLayout lnlMenuProfile;
    private LinearLayout lnlMenuSetting;
    private LinearLayout lnlMenuAbout;
    private LinearLayout lnlMenuContact;
    private LinearLayout lnlMenuHome;
    private LinearLayout lnlMenuBlog;
    private LinearLayout lnlMenuDiscussion; //->faq
    private LinearLayout lnlMenuFaq; //->new
    private LinearLayout lnlMenuLogout;
    private LinearLayout lnlProfile;
    private ImageView imvAvatar;
    private TextView txtName;
    private LinearLayout lnlTimer;
    private LinearLayout lnlShop;
    private TextView txtNumCart;
    private TextView txtProfile;

    private ListView lvHomeContent;
    private View headerView;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isPull = false;
    private RelativeLayout rltPager;

    private List<ObjectBanner> listBanner;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));
        overrideFontsRegular(findViewById(R.id.lnlRoot));

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer((int) (5 * StaticFunction.getDensity(HomeActivity.this))))
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.white)
                .cacheOnDisc(true).build();

        isLoading = false;

//        String deviceName = android.os.Build.MODEL;
//        String deviceMan = android.os.Build.MANUFACTURER;
//        showToast(deviceName + "     " + deviceMan);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_ALL_CATEGORY);
            intentFilter.addAction(AntidoteService.RECEIVER_GET_BANNER);
            registerReceiver(activityReceiver, intentFilter);
        }

        listCategory = new ArrayList<ObjectCategory>();
        listBanner = new ArrayList<ObjectBanner>();

        initView();
        initData();
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        lvHomeContent = (ListView) findViewById(R.id.lvHomeMain);
        rltActionbarHome = (RelativeLayout) findViewById(R.id.rltLeft);
        rltActionbarSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltActionbarCart = (RelativeLayout) findViewById(R.id.rltCart);
        lnlRoot = (LinearLayout) findViewById(R.id.lnlRoot);
        lnlMenuShop = (LinearLayout) findViewById(R.id.lnlMenuShop);
        lnlMenuProfile = (LinearLayout) findViewById(R.id.lnlMenuProfile);
        lnlMenuSetting = (LinearLayout) findViewById(R.id.lnlMenuSetting);
        lnlMenuAbout = (LinearLayout) findViewById(R.id.lnlMenuAbout);
        lnlMenuContact = (LinearLayout) findViewById(R.id.lnlMenuContact);
        lnlMenuFaq = (LinearLayout) findViewById(R.id.lnlMenuFaq);
        lnlMenuHome = (LinearLayout) findViewById(R.id.lnlMenuHome);
        lnlMenuBlog = (LinearLayout) findViewById(R.id.lnlMenuBlog);
        lnlMenuDiscussion = (LinearLayout) findViewById(R.id.lnlMenuDiscussion);
        lnlMenuLogout = (LinearLayout) findViewById(R.id.lnlMenuLogout);
        txtProfile = (TextView) findViewById(R.id.txtProfile);
        lnlProfile = (LinearLayout) findViewById(R.id.lnlProfile);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        txtName = (TextView) findViewById(R.id.txtName);

        layoutInflater = LayoutInflater.from(this);
        headerView = layoutInflater.inflate(R.layout.header_home, null);
        overrideFontsLight(headerView);
        overrideFontsMedium(headerView.findViewById(R.id.txtActionBanner));
        overrideFontsMedium(headerView.findViewById(R.id.txtActionShop));
        pager = (ViewPager) headerView.findViewById(R.id.pager);
        rltPager = (RelativeLayout) headerView.findViewById(R.id.rltPager);
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        lnlTimer = (LinearLayout) headerView.findViewById(R.id.lnlTimer);
        lnlShop = (LinearLayout) headerView.findViewById(R.id.lnlShop);
        txtNumCart = (TextView) findViewById(R.id.txtNumCart);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltPager.getLayoutParams();
        params.height = StaticFunction.getScreenWidth(HomeActivity.this) * 2 / 3;
        rltPager.setLayoutParams(params);

        rltActionbarHome.setOnClickListener(this);
        rltActionbarSearch.setOnClickListener(this);
        rltActionbarCart.setOnClickListener(this);
        lnlRoot.setOnClickListener(this);
        lnlMenuShop.setOnClickListener(this);
        lnlMenuProfile.setOnClickListener(this);
        lnlProfile.setOnClickListener(this);
        lnlMenuSetting.setOnClickListener(this);
        lnlMenuAbout.setOnClickListener(this);
        lnlMenuContact.setOnClickListener(this);
        lnlMenuFaq.setOnClickListener(this);
        lnlMenuHome.setOnClickListener(this);
        lnlMenuBlog.setOnClickListener(this);
        lnlMenuDiscussion.setOnClickListener(this);
        lnlMenuLogout.setOnClickListener(this);
        lnlTimer.setOnClickListener(this);
        lnlShop.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_home);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isPull = true;
                Intent intent = new Intent(HomeActivity.this, AntidoteService.class);
                intent.setAction(AntidoteService.ACTION_GET_ALL_CATEGORY);
                startService(intent);
            }
        });
    }

    private void initData() {
        List<ObjectBanner> list_banner = BannerController.getAllBanners(HomeActivity.this);
        for (ObjectBanner objectBanner : list_banner) {
            listBanner.add(objectBanner);
        }
        adapter = new ImageViewFragmentAdapter(getSupportFragmentManager(), this, listBanner);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(listBanner.size());
        indicator.setViewPager(pager);
        pager.setCurrentItem(0);
        slideBannerPager(1);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position);
                if (position == 3) {
                    slideBannerPager(0);
                } else {
                    slideBannerPager(position + 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<ObjectCategory> list = CategoryController.getAllCategories(HomeActivity.this);
        for (ObjectCategory category : list) {
            listCategory.add(category);
        }

        contentAdapter = new HomeContentAdapter(HomeActivity.this, listCategory);
        lvHomeContent.addHeaderView(headerView);
        lvHomeContent.setAdapter(contentAdapter);
        lvHomeContent.setOnScrollListener(new EndScrollListener());

        Intent intent = new Intent(HomeActivity.this, AntidoteService.class);
        intent.setAction(AntidoteService.ACTION_GET_ALL_CATEGORY);
        startService(intent);
        if (listCategory.size() == 0) {
            showProgressDialog();
        }

        Intent intentGetBanner = new Intent(AntidoteService.ACTION_GET_BANNER, null, HomeActivity.this, AntidoteService.class);
        startService(intentGetBanner);

        if (!UserController.isLogin(HomeActivity.this)) {
            Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intentLogin);
        }
    }

    private void slideBannerPager(final int page) {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(60000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                Log.e("loi", (millisUntilFinished/1000) + "");
            }

            @Override
            public void onFinish() {
//                Log.e("loi", "Done");
                pager.setCurrentItem(page, true);
            }
        };
        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (timer != null) {
            timer.start();
        }
        calculateNumCart();
//        if (StaticFunction.isLogout) {
//            StaticFunction.isLogout = false;
//            Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
//            startActivity(intentLogin);
//            finish();
//        }
        if (UserController.isLogin(HomeActivity.this)) {
            txtProfile.setText("PROFILE");
            lnlMenuProfile.setVisibility(View.GONE);
            lnlProfile.setVisibility(View.VISIBLE);
            lnlMenuLogout.setVisibility(View.VISIBLE);
            ObjectUser user = UserController.getCurrentUser(HomeActivity.this);
            if (user.getIdFacebook().length() == 0) {
                imageLoader.displayImage(StaticFunction.URL + user.getImage(), imvAvatar, options);
            } else {
                imageLoader.displayImage("https://graph.facebook.com/" + user.getIdFacebook() + "/picture?height=200&width=200", imvAvatar, options);
            }
            txtName.setText(user.getFirstName() + " " + user.getLastName());
        } else {
            txtProfile.setText("LOG IN");
            lnlMenuProfile.setVisibility(View.VISIBLE);
            lnlProfile.setVisibility(View.GONE);
            lnlMenuLogout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer = null;
        }
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(HomeActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_login);

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
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

    private void calculateNumCart() {
//        if (UserController.isLogin(HomeActivity.this)) {
        List<ObjectCart> list = CartController.getAllCarts(HomeActivity.this);
        if (list.size() == 0) {
            txtNumCart.setText("");
            txtNumCart.setVisibility(View.GONE);
        } else {
            txtNumCart.setText(list.size() + "");
            txtNumCart.setVisibility(View.VISIBLE);
        }
//        } else {
//            txtNumCart.setText("");
//            txtNumCart.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
//                if (UserController.isLogin(HomeActivity.this)) {
                Intent intentShoppingCart = new Intent(HomeActivity.this, ShoppingCartActivity.class);
                startActivity(intentShoppingCart);
//                } else {
//                    showPopupConfirmLogin();
//                }
                break;
            case R.id.lnlMenuShop:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentShop_1 = new Intent(HomeActivity.this, ShopActivity.class);
                startActivity(intentShop_1);
                break;
            case R.id.lnlMenuProfile:
                drawerLayout.closeDrawer(Gravity.LEFT);
                if (UserController.isLogin(HomeActivity.this)) {
                    Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intentProfile);
                } else {
                    Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.lnlProfile:
                drawerLayout.closeDrawer(Gravity.LEFT);
                if (UserController.isLogin(HomeActivity.this)) {
                    Intent intentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intentProfile);
                } else {
                    Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                break;
            case R.id.lnlMenuSetting:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentSetting = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.lnlMenuAbout:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentAbout = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                break;
            case R.id.lnlMenuContact:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentContact = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(intentContact);
                break;
            case R.id.lnlMenuFaq:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentNewFaq = new Intent(HomeActivity.this, FaqNewActivity.class);
                startActivity(intentNewFaq);
                break;
            case R.id.lnlMenuHome:
                drawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.lnlMenuBlog:
                drawerLayout.closeDrawer(Gravity.LEFT);
//                Uri uriBlog = Uri.parse("http://antidote.sg/blog/");
//                Intent intentBlog = new Intent(Intent.ACTION_VIEW, uriBlog);
//                startActivity(intentBlog);
//                Intent intentBlog = new Intent(HomeActivity.this, BlogActivity.class);
//                startActivity(intentBlog);
                Uri uriBlog = Uri.parse("http://antidote.sg/blog/");
                showBlog(uriBlog);
                break;
            case R.id.lnlMenuDiscussion:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Intent intentFaq = new Intent(HomeActivity.this, FaqActivity.class);
                startActivity(intentFaq);
                break;
            case R.id.lnlMenuLogout:
                showPopupConfirmLogout();
                break;
            case R.id.lnlTimer:
                Intent intentTimer = new Intent(HomeActivity.this, TimerActivity.class);
                startActivity(intentTimer);
                break;
            case R.id.lnlShop:
                Intent intentShop = new Intent(HomeActivity.this, ShopActivity.class);
                startActivity(intentShop);
                break;
        }
    }

    public void showBlog(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showPopupConfirmLogout() {
        // custom dialog
        final Dialog dialog = new Dialog(HomeActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);



        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMsg.setText("Do you wish to log out?");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                dialog.dismiss();
                drawerLayout.closeDrawer(Gravity.LEFT);
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

    private void logout() {
        List<ObjectUser> users = UserController.getAllUsers(HomeActivity.this);
        if (users.size() > 0) {
            ObjectUser user = users.get(0);
            if (user.getIdFacebook().length() == 0) {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", user.getEmail());
                editor.commit();
            }
        }
        UserController.clearAllUsers(HomeActivity.this);
        CartController.clearAllCarts(HomeActivity.this);
//        CategoryController.clearAllCategories(ProfileActivity.this);
//        ProductController.clearAllProducts(ProfileActivity.this);
//        StaticFunction.isLogout = true;

        Session session = Session.getActiveSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
            Session.setActiveSession(null);
        }

//        finish();

        onResume();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_ALL_CATEGORY)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listCategory.clear();
                    List<ObjectCategory> list = CategoryController.getAllCategories(HomeActivity.this);
                    for (ObjectCategory category : list) {
                        listCategory.add(category);
                    }
                    contentAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_BANNER)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listBanner.clear();
                    List<ObjectBanner> list_banner = BannerController.getAllBanners(HomeActivity.this);
                    for (ObjectBanner objectBanner : list_banner) {
                        listBanner.add(objectBanner);
                    }
                    adapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            }
            if (isPull) {
                isPull = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
                    if (!isLoading) {
//                        showToast("end scroll");
                        isLoading = true;
                        lvHomeContent.setOnScrollListener(null);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                contentAdapter.notifyDataSetChanged();
                            }
                        }, 100);
                    }
                }
            }
        }
    }
}
