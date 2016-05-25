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
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.adapters.CommentAdapter;
import com.antidote.adapters.SpinnerAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CommentController;
import com.antidote.daocontroller.OptionController;
import com.antidote.daocontroller.ProductController;
import com.antidote.daocontroller.ProductOptionController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.KeyboardVisibilityListener;
import com.antidote.staticfunction.StaticFunction;
import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import antidote.ObjectCart;
import antidote.ObjectComment;
import antidote.ObjectProduct;
import antidote.ObjectProductOption;
import antidote.ObjectUser;

/**
 * Created by USER on 5/16/2015.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener, ParallaxScrollView.ScrollViewListener, KeyboardVisibilityListener.OnKeyboardVisibilityListener {

    private RelativeLayout rltLeft;
    private RelativeLayout rltActionbarSearch;
    private RelativeLayout rltActionbarCart;
    private RelativeLayout rltChoseInfo;
    private RelativeLayout rltChoseReview;
    private LinearLayout lnlInfo;
    private LinearLayout lnlReview;
    private TextView txtInfo;
    private TextView txtReview;
    private LinearLayout lnlListReview;
    private ScrollView parallaxScrollView;

    private ImageView imvProduct;
    private RelativeLayout rltImvProduct;
    private TextView txtProductName;
    private TextView txtProductDesc;
    private TextView txtNumComment;
    private TextView txtShowMore;
    private LinearLayout lnlSendComment;
    private EditText edtComment;
    private TextView txtNameActionBar;

    private LinearLayout lnlOption;
    private LinearLayout lnlPriceSale;
    private TextView txtOption;
    private Spinner spnOption;
    private TextView txtOptionPrice;
    private TextView txtPriceSale;
    private LinearLayout lnlDownQty;
    private LinearLayout lnlUpQty;
    private EditText edtQty;
    private TextView txtTotal;
    private LinearLayout lnlAddCart;
    private TextView txtNumCart;

    private Long id_product;
    private ObjectProduct objectProduct;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private List<ObjectComment> listComment;
    private ListView lvComment;
    private CommentAdapter commentAdapter;

    private int PAGE = 0;
    private int LIMIT = 10;

    private boolean isLoading = false;
    private boolean isReview = false;
    private boolean isShowFull = false;

    private List<ObjectProductOption> listProductOption;
    private List<String> listProductOptionValue;

    private Float localProductPrice;

    private KeyboardVisibilityListener keyboardVisibilityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        keyboardVisibilityListener = new KeyboardVisibilityListener();
        keyboardVisibilityListener.setKeyboardListener(this, DetailActivity.this,
                R.id.rltRootScrollView);

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_GET_COMMENT);
            intentFilter.addAction(AntidoteService.RECEIVER_ADD_COMMENT);
            registerReceiver(activityReceiver, intentFilter);
        }

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.tranparent)
                .cacheOnDisc(true).build();

        if (getIntent().getExtras() != null) {
            id_product = getIntent().getExtras().getLong("id_product", 0);
            objectProduct = ProductController.getObjectProductById(DetailActivity.this, id_product);
        }

        listComment = new ArrayList<ObjectComment>();
        listProductOption = new ArrayList<ObjectProductOption>();
        listProductOptionValue = new ArrayList<String>();
        localProductPrice = 0f;

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateNumCart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
        CommentController.clearAllComments(DetailActivity.this);
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltActionbarSearch = (RelativeLayout) findViewById(R.id.rltSearch);
        rltActionbarCart = (RelativeLayout) findViewById(R.id.rltCart);
        rltChoseInfo = (RelativeLayout) findViewById(R.id.rltChoseInfo);
        rltChoseReview = (RelativeLayout) findViewById(R.id.rltChoseReview);
        lnlInfo = (LinearLayout) findViewById(R.id.lnlInfo);
        lnlReview = (LinearLayout) findViewById(R.id.lnlReview);
        lnlInfo.setVisibility(View.VISIBLE);
        lnlReview.setVisibility(View.GONE);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtReview = (TextView) findViewById(R.id.txtReview);
        lnlListReview = (LinearLayout) findViewById(R.id.lnlListReview);
        parallaxScrollView = (ScrollView) findViewById(R.id.parallaxScrollView);
        lnlAddCart = (LinearLayout) findViewById(R.id.lnlAddCart);
        txtNumCart = (TextView) findViewById(R.id.txtNumCart);
        txtNameActionBar = (TextView) findViewById(R.id.txtNameActionBar);

        imvProduct = (ImageView) findViewById(R.id.imvProduct);
        rltImvProduct = (RelativeLayout) findViewById(R.id.rltImvProduct);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rltImvProduct.getLayoutParams();
        params.height = StaticFunction.getScreenWidth(this);
        rltImvProduct.setLayoutParams(params);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        overrideFontsMedium(txtProductName);
        txtProductDesc = (TextView) findViewById(R.id.txtProductDesc);
        txtNumComment = (TextView) findViewById(R.id.txtNumComment);
        lvComment = (ListView) findViewById(R.id.lvComment);
        txtShowMore = (TextView) findViewById(R.id.txtShowMore);
        lnlSendComment = (LinearLayout) findViewById(R.id.lnlSendComment);
        edtComment = (EditText) findViewById(R.id.edtComment);

        lnlOption = (LinearLayout) findViewById(R.id.lnlOption);
        lnlPriceSale = (LinearLayout) findViewById(R.id.lnlPriceSale);
        txtOption = (TextView) findViewById(R.id.txtOption);
        spnOption = (Spinner) findViewById(R.id.spnOption);
        txtOptionPrice = (TextView) findViewById(R.id.txtOptionPrice);
        txtPriceSale = (TextView) findViewById(R.id.txtPriceSale);
        lnlDownQty = (LinearLayout) findViewById(R.id.lnlDownQty);
        lnlUpQty = (LinearLayout) findViewById(R.id.lnlUpQty);
        edtQty = (EditText) findViewById(R.id.edtQty);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        txtProductDesc.setMaxLines(5);

        rltLeft.setOnClickListener(this);
        rltActionbarSearch.setOnClickListener(this);
        rltActionbarCart.setOnClickListener(this);
        rltChoseInfo.setOnClickListener(this);
        rltChoseReview.setOnClickListener(this);
        txtShowMore.setOnClickListener(this);
        lnlSendComment.setOnClickListener(this);
        lnlDownQty.setOnClickListener(this);
        lnlUpQty.setOnClickListener(this);
        lnlAddCart.setOnClickListener(this);

        SpannableString content = new SpannableString(txtShowMore.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        txtShowMore.setText(content);
    }

    private void initData() {
        Intent intent = new Intent(DetailActivity.this, AntidoteService.class);
        intent.setAction(AntidoteService.ACTION_GET_COMMENT);
        intent.putExtra(AntidoteService.EXTRA_ID_PRODUCT, id_product + "");
        intent.putExtra(AntidoteService.EXTRA_COMMENT_PAGER, PAGE + "");
        intent.putExtra(AntidoteService.EXTRA_COMMENT_LIMIT, LIMIT + "");
        startService(intent);

        if (objectProduct != null) {
            imageLoader.displayImage(StaticFunction.URL + objectProduct.getImage(), imvProduct, options);
            txtProductName.setText(objectProduct.getName());
            txtProductDesc.setText(objectProduct.getDescription());
            txtNameActionBar.setText(objectProduct.getName());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (txtProductDesc.getLineCount() <= 5) {
                        txtShowMore.setVisibility(View.GONE);
                    }
                }
            }, 100);
//            setListReview();
        }

        List<ObjectComment> list = CommentController.getCommentByIdProduct(DetailActivity.this, id_product);
        for (ObjectComment comment : list) {
            listComment.add(comment);
        }
        commentAdapter = new CommentAdapter(DetailActivity.this, listComment);
        lvComment.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(lvComment);

        if (listComment.size() == 1) {
            txtNumComment.setText(listComment.size() + " COMMENT");
        } else {
            txtNumComment.setText(listComment.size() + " COMMENTS");
        }

//        lvComment.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        parallaxScrollView.setScrollViewListener(this);

        List<ObjectProductOption> productOptionList = ProductOptionController.getProductOptionByIdProduct(DetailActivity.this, id_product);
        if (productOptionList.size() > 0) {
            lnlOption.setVisibility(View.VISIBLE);
            lnlPriceSale.setVisibility(View.GONE);
            for (ObjectProductOption productOption : productOptionList) {
                listProductOption.add(productOption);
                listProductOptionValue.add(productOption.getValue());
            }
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listProductOptionValue);
//            dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_item, listProductOptionValue);
            spnOption.setAdapter(spinnerAdapter);
            txtOption.setText(OptionController.getNameOptionById(DetailActivity.this, listProductOption.get(0).getOptionID()));

            spnOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (listProductOption.get(position).getSalePrice() == 0) {
                        BigDecimal bigOptionPrice = new BigDecimal(listProductOption.get(position).getRegularPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtOptionPrice.setText("S$" + bigOptionPrice);
                        localProductPrice = bigOptionPrice.floatValue();
                    } else {
                        BigDecimal bigOptionPrice = new BigDecimal(listProductOption.get(position).getSalePrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtOptionPrice.setText("S$" + bigOptionPrice);
                        localProductPrice = bigOptionPrice.floatValue();
                    }
                    BigDecimal bigTotal = new BigDecimal(localProductPrice * Integer.parseInt(edtQty.getText().toString().trim())).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtTotal.setText("S$" + bigTotal);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spnOption.setSelection(0);
        } else {
            lnlOption.setVisibility(View.GONE);
            lnlPriceSale.setVisibility(View.VISIBLE);
            if (objectProduct != null) {
                if (objectProduct.getSalePrice() == 0) {
                    BigDecimal bigPriceSale = new BigDecimal(objectProduct.getRegularPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtPriceSale.setText("S$" + bigPriceSale);
                    localProductPrice = bigPriceSale.floatValue();
                } else {
                    BigDecimal bigPriceSale = new BigDecimal(objectProduct.getSalePrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtPriceSale.setText("S$" + bigPriceSale);
                    localProductPrice = bigPriceSale.floatValue();
                }
            }
        }

        edtQty.setText("1");
        BigDecimal bigTotal = new BigDecimal(localProductPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
        txtTotal.setText("S$" + bigTotal);
        edtQty.addTextChangedListener(textWatcherEdtQty);
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

    @Override
    public void onScrollChanged(ParallaxScrollView scrollView, int x, int y, int oldx, int oldy) {
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            if (!isLoading && isReview) {
                isLoading = true;
                PAGE++;
                Intent intent = new Intent(DetailActivity.this, AntidoteService.class);
                intent.setAction(AntidoteService.ACTION_GET_COMMENT);
                intent.putExtra(AntidoteService.EXTRA_ID_PRODUCT, id_product + "");
                intent.putExtra(AntidoteService.EXTRA_COMMENT_PAGER, PAGE + "");
                intent.putExtra(AntidoteService.EXTRA_COMMENT_LIMIT, LIMIT + "");
                startService(intent);
                showToast("Loading more review...");
//                showProgressDialog();
            }
//            showToast("bottom of page");
        }
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
//        if (visible) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    parallaxScrollView.scrollTo(0, parallaxScrollView.getScrollY() + (int) (10 * StaticFunction.getDensity(DetailActivity.this)));
//                }
//            }, 100);
//        }
    }

    private class EndScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount >= visibleItemCount + 2) {
                if (firstVisibleItem + 3 >= totalItemCount - visibleItemCount + 2) {
//                    if (!isLoading) {
//                        isLoading = true;
//                        Intent intent = new Intent(DetailActivity.this, AntidoteService.class);
//                        intent.setAction(AntidoteService.ACTION_GET_COMMENT);
//                        intent.putExtra(AntidoteService.EXTRA_ID_PRODUCT, id_product + "");
//                        intent.putExtra(AntidoteService.EXTRA_COMMENT_PAGER, PAGE + "");
//                        intent.putExtra(AntidoteService.EXTRA_COMMENT_LIMIT, LIMIT + "");
//                        startService(intent);
//                        //Toast.makeText(SearchActivity.this, "Load more product", Toast.LENGTH_SHORT).show();
//                    }
//                    showToast("bottom");
                }
            }
        }
    }

    private void setListReview() {
        listComment.clear();
        List<ObjectComment> list = CommentController.getCommentByIdProduct(DetailActivity.this, id_product);
        for (ObjectComment comment : list) {
            listComment.add(comment);
        }

        if (listComment.size() == 1) {
            txtNumComment.setText(listComment.size() + " COMMENT");
        } else {
            txtNumComment.setText(listComment.size() + " COMMENTS");
        }

        lnlListReview.removeAllViews();
        int i = 0;
        for (ObjectComment comment : listComment) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewRow = inflater.inflate(R.layout.row_detail_review, null);
            ImageView imvAvatar = (ImageView) viewRow.findViewById(R.id.imvAvatar);
            TextView txtUsername = (TextView) viewRow.findViewById(R.id.txtUsername);
            TextView txtComment = (TextView) viewRow.findViewById(R.id.txtComment);
            TextView txtDate = (TextView) viewRow.findViewById(R.id.txtDate);

            imageLoader.displayImage(StaticFunction.URL + comment.getImage(), imvAvatar, options);
            txtUsername.setText(comment.getFirstName() + " " + comment.getLastName());
            txtComment.setText(comment.getComment());
            txtDate.setText(comment.getCreateDate().substring(0, 11));

            lnlListReview.addView(viewRow);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltSearch:
                Intent intentSearch = new Intent(DetailActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.rltCart:
//                Toast.makeText(DetailActivity.this, "cart", Toast.LENGTH_SHORT).show();
//                if (UserController.isLogin(DetailActivity.this)) {
                Intent intentShoppingCart = new Intent(DetailActivity.this, ShoppingCartActivity.class);
                startActivity(intentShoppingCart);
//                } else {
//                    showPopupConfirmLogin();
//                }
                break;
            case R.id.rltChoseInfo:
                rltChoseInfo.setBackgroundResource(R.drawable.btn_detail_black_left);
                rltChoseReview.setBackgroundResource(R.drawable.btn_detail_white_right);
                txtInfo.setTextColor(getResources().getColor(R.color.white));
                txtReview.setTextColor(getResources().getColor(R.color.black));
                lnlInfo.setVisibility(View.VISIBLE);
                lnlReview.setVisibility(View.GONE);
                parallaxScrollView.scrollTo(0, 0);
                isReview = false;
                lnlAddCart.setVisibility(View.VISIBLE);
                StaticFunction.hideKeyboard(DetailActivity.this);
                break;
            case R.id.rltChoseReview:
                rltChoseInfo.setBackgroundResource(R.drawable.btn_detail_white_left);
                rltChoseReview.setBackgroundResource(R.drawable.btn_detail_black_right);
                txtInfo.setTextColor(getResources().getColor(R.color.black));
                txtReview.setTextColor(getResources().getColor(R.color.white));
                lnlInfo.setVisibility(View.GONE);
                lnlReview.setVisibility(View.VISIBLE);
                parallaxScrollView.scrollTo(0, 0);
                isReview = true;
                lnlAddCart.setVisibility(View.GONE);
                StaticFunction.hideKeyboard(DetailActivity.this);
                break;
            case R.id.txtShowMore:
                if (!isShowFull) {
                    txtProductDesc.setMaxLines(100);
                    txtShowMore.setText("Show less");
                    SpannableString content = new SpannableString(txtShowMore.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    txtShowMore.setText(content);
                    isShowFull = true;
                } else {
                    txtProductDesc.setMaxLines(5);
                    txtShowMore.setText("Show more");
                    SpannableString content = new SpannableString(txtShowMore.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    txtShowMore.setText(content);
                    isShowFull = false;
                }
                break;
            case R.id.lnlSendComment:
                if (UserController.isLogin(DetailActivity.this)) {
                    String comment = edtComment.getText().toString().trim();
                    if (comment.length() == 0) {
                        showToast("Please enter review.");
                    } else {
                        ObjectUser user = UserController.getCurrentUser(DetailActivity.this);
                        if (user != null) {
                            Intent intent = new Intent(DetailActivity.this, AntidoteService.class);
                            intent.setAction(AntidoteService.ACTION_ADD_COMMENT);
                            intent.putExtra(AntidoteService.EXTRA_COMMENT_USER_ID, user.getId() + "");
                            intent.putExtra(AntidoteService.EXTRA_COMMENT_PRODUCT_ID, id_product + "");
                            intent.putExtra(AntidoteService.EXTRA_COMMENT_COMMENT, comment + "");
                            startService(intent);
                            showProgressDialog();
                        }
                    }
                } else {
                    showPopupConfirmLogin();
                }
                break;
            case R.id.lnlDownQty:
                if (edtQty.getText().toString().trim().length() > 0) {
                    int qty = Integer.parseInt(edtQty.getText().toString().trim());
                    if (qty > 1) {
                        qty = qty - 1;
                        edtQty.setText(qty + "");
                        BigDecimal bigTotal = new BigDecimal(localProductPrice * qty).setScale(2, BigDecimal.ROUND_HALF_UP);
                        txtTotal.setText("S$" + bigTotal);
                    }
                }
                break;
            case R.id.lnlUpQty:
                if (edtQty.getText().toString().trim().length() > 0) {
                    int qty = Integer.parseInt(edtQty.getText().toString().trim());
                    qty = qty + 1;
                    edtQty.setText(qty + "");
                    BigDecimal bigTotal = new BigDecimal(localProductPrice * qty).setScale(2, BigDecimal.ROUND_HALF_UP);
                    txtTotal.setText("S$" + bigTotal);
                }
                break;
            case R.id.lnlAddCart:
//                if (UserController.isLogin(DetailActivity.this)) {
                if (edtQty.getText().toString().trim().length() > 0) {
                    if (Integer.parseInt(edtQty.getText().toString().trim()) > 0) {
                        addCart();
                    } else {
                        showToast("Please enter quantity to add cart.");
                    }
                } else {
                    showToast("Please enter quantity to add cart.");
                }
//                } else {
//                    showPopupConfirmLogin();
//                }
                break;
        }
    }

    private void addCart() {
        int quantity = Integer.parseInt(edtQty.getText().toString().trim());
        float regularPrice = 0f;
        float salePrice = 0f;
        if (listProductOption.size() > 0) {
            if (listProductOption.get(spnOption.getSelectedItemPosition()).getSalePrice() > 0) {
                BigDecimal bigOptionPrice = new BigDecimal(listProductOption.get(spnOption.getSelectedItemPosition()).getSalePrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                salePrice = bigOptionPrice.floatValue();
                BigDecimal bigPriceRegular = new BigDecimal(objectProduct.getRegularPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                regularPrice = bigPriceRegular.floatValue();
            } else {
                BigDecimal bigOptionPrice = new BigDecimal(listProductOption.get(spnOption.getSelectedItemPosition()).getRegularPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                regularPrice = bigOptionPrice.floatValue();
            }
            ObjectCart cart = CartController.getObjectCartByIdProductAndIdProductOption(DetailActivity.this, id_product, listProductOption.get(spnOption.getSelectedItemPosition()).getId());
            if (cart != null) {
                cart.setQuantity(cart.getQuantity() + quantity);
                showToast("Add cart successful.");
            } else {
                if (objectProduct != null) {
                    ObjectCart objectCart = new ObjectCart();
                    objectCart.setProductID(id_product);
                    objectCart.setProductName(objectProduct.getName());
                    objectCart.setQuantity(quantity);
                    objectCart.setRegularPrice(regularPrice);
                    objectCart.setSalePrice(salePrice);
                    objectCart.setIdProductOption(listProductOption.get(spnOption.getSelectedItemPosition()).getId());
                    objectCart.setNameProductOption(listProductOption.get(spnOption.getSelectedItemPosition()).getValue());
                    CartController.insert(DetailActivity.this, objectCart);
                    showToast("Add cart successful.");
                } else {
                    showToast("Can not add cart, please try again.");
                }
            }
        } else {
            if (objectProduct != null) {
                if (listProductOption.get(spnOption.getSelectedItemPosition()).getSalePrice() > 0) {
                    BigDecimal bigOptionPrice = new BigDecimal(listProductOption.get(spnOption.getSelectedItemPosition()).getSalePrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    salePrice = bigOptionPrice.floatValue();
                    BigDecimal bigPriceRegular = new BigDecimal(objectProduct.getRegularPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    regularPrice = bigPriceRegular.floatValue();
                } else {
                    BigDecimal bigOptionPrice = new BigDecimal(listProductOption.get(spnOption.getSelectedItemPosition()).getRegularPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
                    regularPrice = bigOptionPrice.floatValue();
                }
                ObjectCart cart = CartController.getObjectCartByIdProductAndIdProductOption(DetailActivity.this, id_product, 0l);
                if (cart != null) {
                    cart.setQuantity(cart.getQuantity() + quantity);
                    showToast("Add cart successful.");
                } else {
                    if (objectProduct != null) {
                        ObjectCart objectCart = new ObjectCart();
                        objectCart.setProductID(id_product);
                        objectCart.setProductName(objectProduct.getName());
                        objectCart.setQuantity(quantity);
                        objectCart.setRegularPrice(regularPrice);
                        objectCart.setSalePrice(salePrice);
                        objectCart.setIdProductOption(0l);
                        CartController.insert(DetailActivity.this, objectCart);
                        showToast("Add cart successful.");
                    } else {
                        showToast("Can not add cart, please try again.");
                    }
                }
            }
        }
        calculateNumCart();
    }

    private void showPopupConfirmLogin() {
        // custom dialog
        final Dialog dialog = new Dialog(DetailActivity.this);

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
                Intent intentLogin = new Intent(DetailActivity.this, LoginActivity.class);
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
//        if (UserController.isLogin(DetailActivity.this)) {
        List<ObjectCart> list = CartController.getAllCarts(DetailActivity.this);
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

    private TextWatcher textWatcherEdtQty = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (edtQty.getText().toString().trim().length() > 0) {
                int qty = Integer.parseInt(edtQty.getText().toString().trim());
                BigDecimal bigTotal = new BigDecimal(localProductPrice * qty).setScale(2, BigDecimal.ROUND_HALF_UP);
                txtTotal.setText("S$" + bigTotal);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_GET_COMMENT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
//                    setListReview();
                    int old_size = listComment.size();
                    listComment.clear();
                    List<ObjectComment> list = CommentController.getCommentByIdProduct(DetailActivity.this, id_product);
                    for (ObjectComment comment : list) {
                        listComment.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lvComment);
                    if (listComment.size() == 1) {
                        txtNumComment.setText(listComment.size() + " COMMENT");
                    } else {
                        txtNumComment.setText(listComment.size() + " COMMENTS");
                    }
                    hideProgressDialog();

                    int new_size = listComment.size();
                    if (old_size < new_size) {
                        isLoading = false;
                    } else {
                        isLoading = true;
                        if (new_size > 0) {
                            showToast("No more review.");
                        }
                    }
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_ADD_COMMENT)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    listComment.clear();
                    List<ObjectComment> list = CommentController.getCommentByIdProduct(DetailActivity.this, id_product);
                    for (ObjectComment comment : list) {
                        listComment.add(comment);
                    }
                    commentAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lvComment);
                    if (listComment.size() == 1) {
                        txtNumComment.setText(listComment.size() + " COMMENT");
                    } else {
                        txtNumComment.setText(listComment.size() + " COMMENTS");
                    }
                    edtComment.setText("");
                    hideProgressDialog();
//                    showToast("Add comment successful.");
                    StaticFunction.hideKeyboard(DetailActivity.this);
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
