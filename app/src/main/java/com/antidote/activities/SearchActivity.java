package com.antidote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sg.antidote.R;
import com.antidote.adapters.CategoryAdapter;
import com.antidote.daocontroller.ProductController;

import java.util.ArrayList;
import java.util.List;

import antidote.ObjectProduct;

/**
 * Created by USER on 6/25/2015.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rltLeft;
    private ListView lvSearch;
    private EditText edtSearch;
    private ImageView imvSearch;
    private List<ObjectProduct> listProduct;
    private CategoryAdapter adapter;
    private TextView txtNumSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        listProduct = new ArrayList<ObjectProduct>();

        initView();
        initData();
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        lvSearch = (ListView) findViewById(R.id.lvSearch);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        imvSearch = (ImageView) findViewById(R.id.imvSearch);
        txtNumSearch = (TextView) findViewById(R.id.txtNumSearch);

        rltLeft.setOnClickListener(this);
        imvSearch.setOnClickListener(this);
    }

    private void initData() {
        adapter = new CategoryAdapter(SearchActivity.this, listProduct);
        lvSearch.setAdapter(adapter);

        txtNumSearch.setText("0 items found.");

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // done stuff
                        String strSearch = edtSearch.getText().toString().trim();
                        if (strSearch.length() == 0) {
                            showToast("Please enter search keyword.");
                        } else {
                            searchProduct(strSearch);
                        }
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        // next stuff
                        break;
                }
                return false;
            }
        });
        edtSearch.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.imvSearch:
                String strSearch = edtSearch.getText().toString().trim();
                if (strSearch.length() == 0) {
                    showToast("Please enter search keyword.");
                } else {
                    searchProduct(strSearch);
                }
                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.length() > 0) {
//                searchProduct(s.toString());
//            }
            searchProduct(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void searchProduct(String strSearch) {
        if (strSearch.length() == 0) {
            listProduct.clear();
            adapter.notifyDataSetChanged();
            txtNumSearch.setText("0 items found.");
        } else {
            List<ObjectProduct> list = ProductController.getProductBySearchName(SearchActivity.this, strSearch);
            listProduct.clear();
            for (ObjectProduct product : list) {
                listProduct.add(product);
            }
            adapter.notifyDataSetChanged();

            if (list.size() == 1) {
                txtNumSearch.setText(list.size() + " item found.");
            } else {
                txtNumSearch.setText(list.size() + " items found.");
            }
        }
    }
}
