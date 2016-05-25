package com.antidote.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import sg.antidote.R;

import com.antidote.adapters.SpinnerAdapter;
import com.antidote.daocontroller.CartController;
import com.antidote.daocontroller.CategoryController;
import com.antidote.daocontroller.CountryController;
import com.antidote.daocontroller.ProductController;
import com.antidote.daocontroller.UserController;
import com.antidote.service.AntidoteService;
import com.antidote.staticfunction.StaticFunction;
import com.facebook.Session;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import antidote.ObjectCountry;
import antidote.ObjectProduct;
import antidote.ObjectUser;

/**
 * Created by USER on 5/19/2015.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private int GET_IMAGE = 11;

    private RelativeLayout rltLeft;
    private RelativeLayout rltSave;
    private TextView txtEdit;
    private RelativeLayout rltOrderHistory;
    private RelativeLayout rltLogout;
    private ImageView imvAvatar;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtEmail;
    private ImageView imvEditEmail;
    private EditText edtPassword;
    private ImageView imvEditPassword;
    private EditText edtPhone;
    private EditText edtAddress;
    private EditText edtCity;
    private EditText edtPostcode;
    private EditText edtFavorite;
    private ImageView imvFavorite;
    private RelativeLayout rltPassword;
    private Spinner spnCountry;
    private List<ObjectCountry> listCountry;
    private List<String> listNameCountry;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private boolean isEdit;

    private Dialog dialogAddEmail;
    private Dialog dialogChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        overrideFontsLight(findViewById(R.id.root));
        overrideFontsMedium(findViewById(R.id.header));

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .displayer(new RoundedBitmapDisplayer((int) (5 * StaticFunction.getDensity(ProfileActivity.this))))
                .showImageForEmptyUri(R.mipmap.icon)
                .showImageOnLoading(R.color.white)
                .cacheOnDisk(true).build();

        if (activityReceiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(AntidoteService.RECEIVER_UPDATE_PROFILE);
            intentFilter.addAction(AntidoteService.RECEIVER_ADD_EMAIL);
            intentFilter.addAction(AntidoteService.RECEIVER_CHANGE_PASS);
            intentFilter.addAction(AntidoteService.RECEIVER_UPLOAD_AVATAR);
            registerReceiver(activityReceiver, intentFilter);
        }

        isEdit = false;

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (activityReceiver != null) {
            this.unregisterReceiver(activityReceiver);
        }
    }

    private void initView() {
        rltLeft = (RelativeLayout) findViewById(R.id.rltLeft);
        rltSave = (RelativeLayout) findViewById(R.id.rltSave);
        txtEdit = (TextView) findViewById(R.id.txtEdit);
        rltOrderHistory = (RelativeLayout) findViewById(R.id.rltOrderHistory);
        rltLogout = (RelativeLayout) findViewById(R.id.rltLogout);
        imvAvatar = (ImageView) findViewById(R.id.imvAvatar);
        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
        edtLastname = (EditText) findViewById(R.id.edtLastname);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        imvEditEmail = (ImageView) findViewById(R.id.imvEditEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        imvEditPassword = (ImageView) findViewById(R.id.imvEditPassword);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtPostcode = (EditText) findViewById(R.id.edtPostcode);
        edtFavorite = (EditText) findViewById(R.id.edtFavorite);
        imvFavorite = (ImageView) findViewById(R.id.imvFavorite);
        rltPassword = (RelativeLayout) findViewById(R.id.rltPassword);
        spnCountry = (Spinner) findViewById(R.id.spnCountry);

        edtEmail.setEnabled(false);
        edtPassword.setEnabled(false);

        txtEdit.setText("Edit");
        setModeEdit(false);

        rltLeft.setOnClickListener(this);
        rltSave.setOnClickListener(this);
        rltOrderHistory.setOnClickListener(this);
        rltLogout.setOnClickListener(this);
        imvEditEmail.setOnClickListener(this);
        imvEditPassword.setOnClickListener(this);
        imvAvatar.setOnClickListener(this);
    }

    private void setModeEdit(boolean b) {
        ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
        if (user != null) {
            if (user.getIdFacebook().length() == 0) {
                edtFirstname.setEnabled(b);
                edtLastname.setEnabled(b);
            } else {
                edtFirstname.setEnabled(false);
                edtLastname.setEnabled(false);
            }
        }
        edtPhone.setEnabled(b);
        edtAddress.setEnabled(b);
        edtCity.setEnabled(b);
        edtPostcode.setEnabled(b);
        spnCountry.setEnabled(b);
    }

    private void initData() {
        ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
        if (user != null) {
            edtFirstname.setText(user.getFirstName());
            edtLastname.setText(user.getLastName());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            edtAddress.setText(user.getAddress());
            edtCity.setText(user.getCity());
            edtPostcode.setText(user.getPostCode());
            if (user.getIdFacebook().length() == 0) {
                imageLoader.displayImage(StaticFunction.URL + user.getImage(), imvAvatar, options);
            } else {
                imageLoader.displayImage("https://graph.facebook.com/" + user.getIdFacebook() + "/picture?height=200&width=200", imvAvatar, options);
            }

            if (user.getIdFacebook().length() == 0) {
                if (user.getEmail().length() > 0) {
                    imvEditEmail.setVisibility(View.GONE);
                    rltPassword.setVisibility(View.VISIBLE);
                } else {
                    imvEditEmail.setVisibility(View.VISIBLE);
                    rltPassword.setVisibility(View.GONE);
                }
            } else {
                rltPassword.setVisibility(View.GONE);
                imvEditEmail.setVisibility(View.GONE);
            }

            listCountry = CountryController.getAllCountries(ProfileActivity.this);
            listNameCountry = new ArrayList<String>();
            for (ObjectCountry country : listCountry) {
                listNameCountry.add(country.getName());
            }

            String code = user.getCountry();
            int i = 0;
            if (code.length() > 0) {
                for (ObjectCountry country : listCountry) {
                    if (code.equals(country.getCode())) {
                        break;
                    }
                    i++;
                }
            } else {
                for (ObjectCountry country : listCountry) {
                    if (country.getCode().equals("SG")) {
                        break;
                    }
                    i++;
                }
            }

//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listNameCountry);
//            spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.spinner_item, listNameCountry);

            spnCountry.setAdapter(spinnerAdapter);
            spnCountry.setSelection(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltLeft:
                finish();
                break;
            case R.id.rltSave:
                if (!isEdit) {
                    isEdit = true;
                    txtEdit.setText("Save");
                    setModeEdit(true);
                } else {
                    updateProfile();
                }
                break;
            case R.id.rltOrderHistory:
                Intent orderHistory = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
                startActivity(orderHistory);
                break;
            case R.id.rltLogout:
                showPopupConfirmLogout();
                break;
            case R.id.imvEditEmail:
                showPopupAddEmail();
                break;
            case R.id.imvEditPassword:
                showPopupChangePassword();
                break;
            case R.id.imvAvatar:
                ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                if (user != null) {
                    if (user.getIdFacebook().length() == 0) {
                        openImagePicker();
                    }
                }
                break;
        }
    }

    private void openImagePicker() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_IMAGE);
        Crop.pickImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GET_IMAGE) {
            Uri selectedImageUri = data.getData();
//            imageLoader.displayImage(selectedImageUri.toString(), imvAvatar, options);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                Bitmap b = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

//                imvAvatar.setImageBitmap(b);

                ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                if (user != null) {
                    Intent intentUpdateProfile = new Intent(ProfileActivity.this, AntidoteService.class);
                    intentUpdateProfile.setAction(AntidoteService.ACTION_UPLOAD_AVATAR);
                    intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_USER_ID, user.getId() + "");
                    intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_AVATAR, encodeTobase64(b));
                    startService(intentUpdateProfile);
                    showProgressDialog();
                }
            } catch (Exception e) {

            }
        }
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
            Uri selectedImageUri = Crop.getOutput(result);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                Bitmap b = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

//                imvAvatar.setImageBitmap(b);

                ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                if (user != null) {
                    Intent intentUpdateProfile = new Intent(ProfileActivity.this, AntidoteService.class);
                    intentUpdateProfile.setAction(AntidoteService.ACTION_UPLOAD_AVATAR);
                    intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_USER_ID, user.getId() + "");
                    intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_AVATAR, encodeTobase64(b));
                    startService(intentUpdateProfile);
                    showProgressDialog();
                }
            } catch (Exception e) {

            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        // Log.e("base64", imageEncoded);
        return imageEncoded;
    }

    private void logout() {
        List<ObjectUser> users = UserController.getAllUsers(ProfileActivity.this);
        if (users.size() > 0) {
            ObjectUser user = users.get(0);
            if (user.getIdFacebook().length() == 0) {
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", user.getEmail());
                editor.commit();
            }
        }
        UserController.clearAllUsers(ProfileActivity.this);
        CartController.clearAllCarts(ProfileActivity.this);
//        CategoryController.clearAllCategories(ProfileActivity.this);
//        ProductController.clearAllProducts(ProfileActivity.this);
//        StaticFunction.isLogout = true;

        Session session = Session.getActiveSession();
        if (session != null) {
            session.closeAndClearTokenInformation();
            Session.setActiveSession(null);
        }

        finish();
    }

    private void updateProfile() {
        String firstname = edtFirstname.getText().toString().trim();
        String lastname = edtLastname.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String country = listCountry.get(spnCountry.getSelectedItemPosition()).getCode();
        String postcode = edtPostcode.getText().toString().trim();

        if (firstname.length() == 0) {
            showToast("Please enter first name.");
        } else if (lastname.length() == 0) {
            showToast("Please enter last name.");
        } else if (!StaticFunction.isValidPhoneNumber(phone)) {
            showToast("Phone number is invalid.");
        } else if (address.length() == 0) {
            showToast("Please enter address.");
        } else if (postcode.length() != 6) {
            showToast("Postcode is invalid.");
        } else {
            ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
            if (user != null) {
                Intent intentUpdateProfile = new Intent(ProfileActivity.this, AntidoteService.class);
                intentUpdateProfile.setAction(AntidoteService.ACTION_UPDATE_PROFILE);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_USER_ID, user.getId() + "");
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_FIRSTNAME, firstname);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_LASTNAME, lastname);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_PHONE, phone);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_ADDRESS, address);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_CITY, city);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_COUNTRY, country);
                intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_POSTCODE, postcode);
                startService(intentUpdateProfile);
                showProgressDialog();
            }
        }
    }

    private void showPopupConfirmLogout() {
        // custom dialog
        final Dialog dialog = new Dialog(ProfileActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_confirm);

        overrideFontsLight(dialog.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialog.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialog.findViewById(R.id.lnlCancel);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMsg.setText("Do you wish to log out?");

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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

    private void showPopupAddEmail() {
        // custom dialog
        dialogAddEmail = new Dialog(ProfileActivity.this);

        dialogAddEmail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddEmail.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialogAddEmail.setCanceledOnTouchOutside(true);
        dialogAddEmail.setContentView(R.layout.popup_add_email);

        overrideFontsLight(dialogAddEmail.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialogAddEmail.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialogAddEmail.findViewById(R.id.lnlCancel);
        final EditText edtEmail = (EditText) dialogAddEmail.findViewById(R.id.edtEmail);
        final EditText edtPass = (EditText) dialogAddEmail.findViewById(R.id.edtPassword);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                if (user != null) {
                    String email = edtEmail.getText().toString().trim();
                    String pass = edtPass.getText().toString().trim();
                    if (email.length() == 0) {
                        showToast("Please enter email.");
                    } else if (pass.length() == 0) {
                        showToast("Please enter password.");
                    } else {
                        Intent intentUpdateProfile = new Intent(ProfileActivity.this, AntidoteService.class);
                        intentUpdateProfile.setAction(AntidoteService.ACTION_ADD_EMAIL);
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_USER_ID, user.getId() + "");
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_EMAIL, email);
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_PASS, pass);
                        startService(intentUpdateProfile);
                        showProgressDialog();
                    }
                }
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddEmail.dismiss();
            }
        });

        dialogAddEmail.show();
    }

    private void showPopupChangePassword() {
        // custom dialog
        dialogChangePass = new Dialog(ProfileActivity.this);

        dialogChangePass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogChangePass.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialogChangePass.setCanceledOnTouchOutside(true);
        dialogChangePass.setContentView(R.layout.popup_change_pass);

        overrideFontsLight(dialogChangePass.findViewById(R.id.root));

        LinearLayout lnlOk = (LinearLayout) dialogChangePass.findViewById(R.id.lnlOk);
        LinearLayout lnlCancel = (LinearLayout) dialogChangePass.findViewById(R.id.lnlCancel);
        final EditText edtOldPass = (EditText) dialogChangePass.findViewById(R.id.edtOldPassword);
        final EditText edtNewPass = (EditText) dialogChangePass.findViewById(R.id.edtNewPassword);
        final EditText edtConfirmPass = (EditText) dialogChangePass.findViewById(R.id.edtConfirmPassword);

        lnlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                if (user != null) {
                    String old_pass = edtOldPass.getText().toString().trim();
                    String new_pass = edtNewPass.getText().toString().trim();
                    String confirm_pass = edtConfirmPass.getText().toString().trim();
                    if (old_pass.length() == 0) {
                        showToast("Please enter current password.");
                    } else if (new_pass.length() == 0) {
                        showToast("Please enter new password.");
                    } else if (confirm_pass.length() == 0) {
                        showToast("Please enter confirm new password.");
                    } else {
                        Intent intentUpdateProfile = new Intent(ProfileActivity.this, AntidoteService.class);
                        intentUpdateProfile.setAction(AntidoteService.ACTION_CHANGE_PASS);
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_USER_ID, user.getId() + "");
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_OLD_PASS, old_pass);
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_NEW_PASS, new_pass);
                        intentUpdateProfile.putExtra(AntidoteService.EXTRA_PROFILE_CONFIRM_PASS, confirm_pass);
                        startService(intentUpdateProfile);
                        showProgressDialog();
                    }
                }
            }
        });

        lnlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChangePass.dismiss();
            }
        });

        dialogChangePass.show();
    }

    private BroadcastReceiver activityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_UPDATE_PROFILE)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                    if (user != null) {
                        edtFirstname.setText(user.getFirstName());
                        edtLastname.setText(user.getLastName());
                        edtPhone.setText(user.getPhone());
                        edtAddress.setText(user.getAddress());
                        edtCity.setText(user.getCity());
                        edtPostcode.setText(user.getPostCode());
                    }
                    isEdit = false;
                    txtEdit.setText("Edit");
                    setModeEdit(false);
                    showToast("Your profile has been updated successfully.");
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_ADD_EMAIL)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                    if (user != null) {
                        if (user.getIdFacebook().length() == 0) {
                            if (user.getEmail().length() > 0) {
                                imvEditEmail.setVisibility(View.GONE);
                                rltPassword.setVisibility(View.VISIBLE);
                                edtEmail.setText(user.getEmail());
                            } else {
                                imvEditEmail.setVisibility(View.VISIBLE);
                                rltPassword.setVisibility(View.GONE);
                            }
                        } else {
                            rltPassword.setVisibility(View.GONE);
                            imvEditEmail.setVisibility(View.GONE);
                        }
                    }
                    showToast("Add email successfully.");
                    dialogAddEmail.dismiss();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_CHANGE_PASS)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    showToast("Change password successfully.");
                    dialogChangePass.dismiss();
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                }
                hideProgressDialog();
            } else if (intent.getAction().equalsIgnoreCase(AntidoteService.RECEIVER_UPLOAD_AVATAR)) {
                String result = intent.getStringExtra(AntidoteService.EXTRA_RESULT);
                if (result.equals(AntidoteService.RESULT_OK)) {
                    ObjectUser user = UserController.getCurrentUser(ProfileActivity.this);
                    if (user != null) {
                        imageLoader.displayImage(StaticFunction.URL + user.getImage(), imvAvatar, options);
                    }
                    showToast("Change picture successfully.");
                } else if (result.equals(AntidoteService.RESULT_FAIL)) {
                    String message = intent.getStringExtra(AntidoteService.EXTRA_RESULT_MESSAGE);
                    showToast(message);
                } else if (result.equals(AntidoteService.RESULT_NO_INTERNET)) {
                    showToast(getString(R.string.nointernet));
                }
                hideProgressDialog();
            }
        }
    };
}
