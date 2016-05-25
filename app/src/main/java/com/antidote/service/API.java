package com.antidote.service;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import antidote.ObjectOrderDelivery;

/**
 * Created by USER on 5/23/2015.
 */
public class API {

    //    public static String URL = "http://api.antidote.cloudshop.vn/";
//    public static String URL = "http://antidote.cloudshop.vn/api/";
    public static String URL = "http://app.antidote.sg/api/";

    private HttpClient client = new DefaultHttpClient();
    private HttpParams httpParams = client.getParams();
    private int TIME_OUT = 30 * 1000;

    public String login(String email, String pass) {
        String url = URL + "account/signin";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pass));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String loginFb(String id_fb, String email, String firstname, String lastname) {
        String url = URL + "account/fblogin";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("idFacebook", id_fb));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("firstname", firstname));
        params.add(new BasicNameValuePair("lastname", lastname));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String addEmail(String user_id, String email, String pass) {
        String url = URL + "account/add-email-pass-for-fb-user";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("id", user_id));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pass));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String forgotPass(String email) {
        String url = URL + "account/forgot-password";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", email));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String changePassword(String user_id, String old_pass, String new_pass, String confirm_pass) {
        String url = URL + "account/update-password";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("id", user_id));
        params.add(new BasicNameValuePair("old_password", old_pass));
        params.add(new BasicNameValuePair("new_password", new_pass));
        params.add(new BasicNameValuePair("confirm_password", confirm_pass));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String signup(String email, String pass, String firstname, String lastname) {
        String url = URL + "account/signup";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", pass));
        params.add(new BasicNameValuePair("firstname", firstname));
        params.add(new BasicNameValuePair("lastname", lastname));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String updateProfile(String user_id, String firstname, String lastname, String phone, String address, String city, String country, String postcode) {
        String url = URL + "account/profile";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("id", user_id));
        params.add(new BasicNameValuePair("firstName", firstname));
        params.add(new BasicNameValuePair("lastName", lastname));
        params.add(new BasicNameValuePair("phone", phone));
        params.add(new BasicNameValuePair("address", address));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("country", country));
        params.add(new BasicNameValuePair("postCode", postcode));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String uploadAvatar(String user_id, String image) {
        String url = URL + "account/upload-avatar";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("id", user_id));
        params.add(new BasicNameValuePair("image", image));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllProduct() {
        String url = URL + "product/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getOption() {
        String url = URL + "option/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getProductOption() {
        String url = URL + "product-option/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getProductByIdCate(String id_cate) {
        String url = URL + "product/get-by-category";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("Class", "Product"));
        params.add(new BasicNameValuePair("catid", id_cate));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllCategory() {
        String url = URL + "category/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllCountry() {
        String url = URL + "common/get-countries";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getComment(String id_product, String page, String limit) {
        String url = URL + "product-comment/get-by-product-id";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("productid", id_product));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("limit", limit));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String addComment(String user_id, String product_id, String comment) {
        String url = URL + "product-comment/add-comment";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("userid", user_id));
        params.add(new BasicNameValuePair("productid", product_id));
        params.add(new BasicNameValuePair("comment", comment));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getGroupProduct() {
        String url = URL + "group-product/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String addOrder(ObjectOrderDelivery orderDelivery) {
        String url = URL + "order/add-order";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("orderDate", orderDelivery.getOrderDate()));
        params.add(new BasicNameValuePair("orderStatus", orderDelivery.getOrderStatus()));
        params.add(new BasicNameValuePair("customerID", orderDelivery.getCustomerID()));
        params.add(new BasicNameValuePair("shippingFirstName", orderDelivery.getShippingFirstName()));
        params.add(new BasicNameValuePair("shippingLastName", orderDelivery.getShippingLastName()));
        params.add(new BasicNameValuePair("shippingCompany", orderDelivery.getShippingCompany()));
        params.add(new BasicNameValuePair("shippingAddress", orderDelivery.getShippingAddress()));
        params.add(new BasicNameValuePair("shippingApartment", orderDelivery.getShippingApartment()));
        params.add(new BasicNameValuePair("shippingCity", orderDelivery.getShippingCity()));
        params.add(new BasicNameValuePair("shippingCountry", orderDelivery.getShippingCountry()));
        params.add(new BasicNameValuePair("shippingPostCode", orderDelivery.getShippingPostCode()));
        params.add(new BasicNameValuePair("shippingEmail", orderDelivery.getShippingEmail()));
        params.add(new BasicNameValuePair("shippingPhone", orderDelivery.getShippingPhone()));
        params.add(new BasicNameValuePair("shippingNote", orderDelivery.getShippingNote()));
        params.add(new BasicNameValuePair("subTotal", orderDelivery.getSubTotal()));
        params.add(new BasicNameValuePair("discount", orderDelivery.getDiscount()));
        params.add(new BasicNameValuePair("shippingType", orderDelivery.getShippingType()));
        params.add(new BasicNameValuePair("total", orderDelivery.getTotal()));
        params.add(new BasicNameValuePair("order_product", orderDelivery.getOrder_product()));
        params.add(new BasicNameValuePair("couponid", orderDelivery.getCouponid()));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getBanner() {
        String url = URL + "banner/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getOrderHistoryByIdUser(String user_id) {
        String url = URL + "order/get-by-userid";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("userid", user_id));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getCouponByCode(String code) {
        String url = URL + "coupon/get-by-code";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("code", code));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllFaq() {
        String url = URL + "faq/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllFaqV2() {
        String url = URL + "faq/get-v2-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String addFaq(String userid, String question) {
        String url = URL + "faq/add-new";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("userid", userid));
        params.add(new BasicNameValuePair("question", question));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getAllFaqCommentByFaqId(String faqsid) {
        String url = URL + "faq-comment/get-by-faqs";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("faqsid", faqsid));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String addFaqComment(String userid, String faqsid, String comment) {
        String url = URL + "faq-comment/add-new";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("userid", userid));
        params.add(new BasicNameValuePair("faqsid", faqsid));
        params.add(new BasicNameValuePair("comment", comment));

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }

    public String getConfig() {
        String url = URL + "config/get-all";

        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        String content = "";
        HttpResponse response = null;

        try {
            HttpPost post = new HttpPost(url);
            response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null)
                result.append(line);

            content = result.toString();
            Log.d("message", content);
        } catch (Exception e) {
            Log.d("message", "fail" + e.toString());
        }

        return content;
    }
}
