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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;
import com.antidote.adapters.CategoryAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.ProductController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectCategory;
import antidote.ObjectProduct;

/**
 * Created by user on 5/30/2015.
 */
public class CategoryActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private ListView parallaxListView;
    private LayoutInflater layoutInflater;
    private View headerView;
    private List<ObjectProduct> listProduct;
    private ImageView imvCategory;
    private TextView txtCategoryName;
    private TextView txtCategoryDesc;
    private Long id_cate = 0l;
    private ObjectCategory category;
    private CategoryAdapter categoryAdapter;
    private TextView txtShowMore;

    private RelativeLayout rltActionbarSearch;
    private RelativeLayout rltActionbarCart;
    private TextView txtNumCart;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private boolean isShowFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_PRODUCT_BY_ID_CATE);
            registerReceiver(activityReceiver, intentFilter);
        }

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.white)
                .cacheOnDisc(true).build();

        if (getIntent().getExtras() != null) {
            id_cate = getIntent().getExtras().getLong("id_cate", 0);
        }

        initView();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        parallaxListView = (ListView) findViewById(R.id.parallaxProduct);
        rltActionbarSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltActionbarCart = (RelativeLayout) findViewById(R.id.rltCart);
        txtNumCart = (TextView) findViewById(R.id.txtNumCart);

        rltLeft.setOnClickListener(this);

        layoutInflater = LayoutInflater.from(this);

        headerView = layoutInflater.inflate(R.layout.header_category, null);
        overrideFontsLight(headerView);
        txtShowMore = (TextView) headerView.findViewById(R.id.txtShowMore);
        txtShowMore.setOnClickListener(this);

        initDataHeader();
        initDataList();

        SpannableString content = new SpannableString(txtShowMore.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtShowMore.setText(content);

        rltActionbarSearch.setOnClickListener(this);
        rltActionbarCart.setOnClickListener(this);
    }

    private void initDataHeader() {
        imvCategory = (ImageView) headerView.findViewById(R.id.imvCategory);
        txtCategoryName = (TextView) headerView.findViewById(R.id.txtCategoryName);
        overrideFontsMedium(txtCategoryName);
        txtCategoryDesc = (TextView) headerView.findViewById(R.id.txtCategoryDesc);
        txtCategoryDesc.setMaxLines(5);

        category = CategoryController.getObjectCategoryById(CategoryActivity.this, id_cate);
        if (category != null) {
//            Log.e("image", StaticFunction.URL + category.getImage() + " activity");
            imageLoader.displayImage(StaticFunction.URL + category.getImage(), imvCategory, options);
            txtCategoryName.setText(category.getName());
            txtCategoryDesc.setText(category.getFullDescription());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    showToast(txtCategoryDesc.getLineCount() + "");
                    if (txtCategoryDesc.getLineCount() <= 5) {
                        txtShowMore.setVisibility(View.GONE);
                    }
                }
            }, 100);
        }
    }

    private void initDataList() {
        listProduct = new ArrayList<ObjectProduct>();
        List<ObjectProduct> list = ProductController.getProductByIdCategory(CategoryActivity.this, id_cate);
        for (ObjectProduct product : list) {
            listProduct.add(product);
        }
        categoryAdapter = new CategoryAdapter(CategoryActivity.this, listProduct);
        parallaxListView.addHeaderView(headerView);
        parallaxListView.setAdapter(categoryAdapter);

        Intent intentGetProductByIdCate = new Intent(CategoryActivity.this, AntidoteService.class);
        intentGetProductByIdCate.setAction(AntidoteService.ACTION_GET_PRODUCT_BY_ID_CATE);
        intentGetProductByIdCate.putExtra(AntidoteService.EXTRA_ID_CATEGORY, id_cate + "");
        startService(intentGetProductByIdCate);
        if (list.size() == 0) {
            showProgressDialog();
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
    protected void onResume() {
        super.onResume();
        calculateNumCart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.txtShowMore:
                if (!isShowFull) {
                    txtCategoryDesc.setMaxLines(100);
                    txtShowMore.setText("Show less");
                    isShowFull = true;
                    SpannableString content = new SpannableString(txtShowMore.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    txtShowMore.setText(content);
                } else {
                    txtCategoryDesc.setMaxLines(5);
                    txtShowMore.setText("Show more");
                    isShowFull = false;
                    SpannableString content = new SpannableString(txtShowMore.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    txtShowMore.setText(content);
                }
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(CategoryActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
//                Toast.makeText(DetailActivity.this, "cart", Toast.LENGTH_SHORT).show();
//                if (UserController.isLogin(CategoryActivity.this)) {
                    Intent intentShoppingCart = new Intent(CategoryActivity.this, ShoppingCartActivity.class);
                    startActivity(intentShoppingCart);
//                } else {
//                    showPopupConfirmLogin();
//                }
                break;
        }
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(CategoryActivity.this);

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
                Intent intentLogin = new Intent(CategoryActivity.this, LoginActivity.class);
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
//        if (UserController.isLogin(CategoryActivity.this)) {
            List<ObjectCart> list = CartController.getAllCarts(CategoryActivity.this);
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

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_PRODUCT_BY_ID_CATE)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listProduct.clear();
                    List<ObjectProduct> list = ProductController.getProductByIdCategory(CategoryActivity.this, id_cate);
                    for (ObjectProduct product : list) {
                        listProduct.add(product);
                    }
                    categoryAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            }
        }
    };
}
