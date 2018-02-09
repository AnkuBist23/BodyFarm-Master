package aman.backender.com.bodyfarm.Authentication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import aman.backender.com.bodyfarm.R;
import aman.backender.com.bodyfarm.Utils.Constant;
import aman.backender.com.bodyfarm.activity.Home;
import aman.backender.com.bodyfarm.activity.Utility;
import aman.backender.com.bodyfarm.bean.UserInfo;
import aman.backender.com.bodyfarm.retrofit.RetrofitResponse;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static aman.backender.com.bodyfarm.Authentication.Login_signup.mode;
import static aman.backender.com.bodyfarm.Utils.Utility.getBitmapFromURL;
import static aman.backender.com.bodyfarm.Utils.Utility.getBitmapFromURLfb;
import static aman.backender.com.bodyfarm.activity.Splash.mPrefs;

public class Register_gym extends AppCompatActivity {
    String LoginId;
    String Password;
    String EmailId;
    String PhotoUrl;
    ImageView blurimage;
    private String userChoosenTask;
    Bitmap photo = null;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    EditText us_fname, us_ownername, us_gymname, us_emailid, us_password, us_phone, us_altNo, us_address;
    String email;
    String emailPattern = "([a-zA-Z0-9._-]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    TextInputLayout pass_layout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gym); ///date of birth pr leke chal


        blurimage = findViewById(R.id.blurimage);
        us_fname = findViewById(R.id.us_fname);
        us_ownername = findViewById(R.id.us_ownername);
        us_gymname = findViewById(R.id.us_gymname);
        us_emailid = findViewById(R.id.us_emailid);
        us_password = findViewById(R.id.us_password);
        us_phone = findViewById(R.id.us_phone);
        us_altNo = findViewById(R.id.us_altNo);
        us_address = findViewById(R.id.us_address);
        pass_layout = findViewById(R.id.pass_layout);

        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (mode == 1) {
            Bundle bundle = getIntent().getExtras();
            String str = bundle.getString("userInfo");
            try {
                JSONObject array = new JSONObject(str);
                LoginId = array.optString("id");
                EmailId = array.optString("Email");
                us_emailid.setText(EmailId);
                us_fname.setText(array.optString("FirstName")+" "+ array.optString("LastName"));
                Password = "GMAIL";
                PhotoUrl = array.optString("PhotoUrl");
                Picasso.with(getApplicationContext()).load(PhotoUrl).into(blurimage);
                photo = getBitmapFromURL(PhotoUrl);// url to bitmap
                PhotoUrl = getStringImage(photo);//bitmap to base64
                if(EmailId.matches(emailPattern)){
                    us_emailid.setKeyListener(null);
                }
                pass_layout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(Register_gym.this, str.toString(), Toast.LENGTH_SHORT).show();

        } else if (mode == 2) {
            Bundle bundle = getIntent().getExtras();
            String str = bundle.getString("userInfo");
            try {
                JSONObject array = new JSONObject(str);
                LoginId = array.optString("id");
                EmailId = array.optString("email");
                us_emailid.setText(EmailId);
                us_fname.setText(array.optString("first_name")+" "+ array.optString("last_name"));
                PhotoUrl = "http://graph.facebook.com/" + LoginId + "/picture?type=small";
                Password = "FACEBOOK";
               // Picasso.with(getApplicationContext()).load(PhotoUrl).into(blurimage);
              //  photo = getBitmapFromURLfb(PhotoUrl);// url to bitmap
              //  PhotoUrl = getStringImage(photo);//bitmap to base64
                if(EmailId.matches(emailPattern)){
                    us_emailid.setKeyListener(null);
                }
                pass_layout.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(Register_gym.this, str.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.register_me)
    public void registerMe(View v) {
        if (mode == 0) {
            email = us_emailid.getText().toString().trim();
            if (email.matches(emailPattern)) {
                LoginId = us_emailid.getText().toString().trim();
                EmailId = us_emailid.getText().toString().trim();
                if (us_fname.getText().toString().trim().length() <= 0) {
                    us_fname.setError("Please Enter the FirstName");
                    return;
                }else if (us_ownername.getText().toString().trim().length() <= 0) {
                    us_ownername.setError("Please Enter the OwnerName");
                    return;
                }  else if (us_gymname.getText().toString().trim().length() <= 0) {
                    us_gymname.setError("Please Enter the GymName");
                    return;
                }else if (us_password.length() <= 7 || us_password.length() >= 12) {
                    us_password.setError("Please Enter the Password");
                    return;
                }else if (us_phone.length() == 9 ) {
                    us_phone.setError("Please Enter the Phone");
                    return;
                } else if (us_altNo.length() == 9 ) {
                    us_altNo.setError("Please Enter the AltPhone");
                    return;
                } else if (us_address.getText().toString().trim().length() <= 0) {
                    us_address.setError("Please Enter the Address");
                    return;
                }else {
                    Password = us_password.getText().toString();
                    Register_Me();
                }
            } else {
                us_emailid.setError("Please Enter Valid Email");
                return;
            }//password Filed Visible
        }else if(mode == 1 ){
            if (us_gymname.getText().toString().trim().length() <= 0) {
                us_gymname.setError("Please Enter the GymName");
                return;
            }else if (us_ownername.getText().toString().trim().length() <= 0) {
                us_ownername.setError("Please Enter the OwnerName");
                return;
            }else if (us_phone.length() == 9) {
                us_phone.setError("Please Enter the Phone");
                return;
            } else if (us_altNo.length() == 9) {
                us_altNo.setError("Please Enter the AltPhone");
                return;
            } else if (us_address.getText().toString().trim().length() == 0) {
                us_address.setError("Please Enter the Address");
                return;
            }else {
                Register_Me();
            }
        }else if(mode == 2 ){
            if (us_gymname.getText().toString().trim().length() == 0) {
                us_gymname.setError("Please Enter the GymName");
                return;
            }else if (us_ownername.getText().toString().trim().length() <= 0) {
                us_ownername.setError("Please Enter the OwnerName");
                return;
            }else if (us_phone.length() == 9) {
                us_phone.setError("Please Enter the Phone");
                return;
            } else if (us_altNo.length() == 9) {
                us_altNo.setError("Please Enter the AltPhone");
                return;
            } else if (us_address.getText().toString().trim().length() <= 0) {
                us_address.setError("Please Enter the Address");
                return;
            }else {
                Register_Me();
            }
        }
        // byte[] decodedString = Base64.decode(PhotoUrl, Base64.DEFAULT);
        // Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        // blurimage1.setImageBitmap(decodedByte);

       // Register_Me();
    }

    @OnClick(R.id.blurimage)
    public void blurimage(View v) {
        selectImage();
    }

    public void Register_Me() {
        UserInfo userInfo = new UserInfo();
        userInfo.setProfileImage(PhotoUrl);
        userInfo.setFirstName(us_fname.getText().toString().trim());
        userInfo.setLastName("");
        userInfo.setOwnerName(us_ownername.getText().toString().trim());
        userInfo.setLoginId(LoginId);
        userInfo.setEmailId(EmailId);
        userInfo.setPassword(Password);
        userInfo.setMobileNo(us_phone.getText().toString());
        userInfo.setAlternateNo(us_altNo.getText().toString());
        userInfo.setAddress(us_address.getText().toString());
        userInfo.setGymName(us_gymname.getText().toString().trim());
        userInfo.setDob("21-Nov-1994"); //21-Nov-1994


       /* private String LoginId; //in case of fb/gmail it should be their actual id, no email.
        private String password;
        private String gymName;
        private String ownerName;
        private String alternateNo;
        private String dob;
        private String profileImage;*/
        //input these values as empty or some value.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String sign_up = new Gson().toJson(userInfo);
        Log.e("DATA", "Register_Me: " + sign_up);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.RegisterUser, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(ContentValues.TAG, "Response_Data" + response);
                RetrofitResponse retroResult = new Gson().fromJson(response, RetrofitResponse.class);
                if (retroResult.getReturnCode() == true){
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("IS_login", "yes");
                    editor.putInt("recId", retroResult.getUserInfo().getRecId());
                    editor.apply();
                    Intent i = new Intent(Register_gym.this, Home.class);
                    i.putExtra("userInfo", retroResult.getUserInfo().getRecId());
                    startActivity(i);
                }else {
                    Toast.makeText(Register_gym.this, retroResult.getStrMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userInfo", sign_up);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Register_gym.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Register_gym.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                PhotoUrl = getStringImage(photo);
                Log.v(ContentValues.TAG, "base64" + PhotoUrl);
            } else if (requestCode == REQUEST_CAMERA) {
                photo = (Bitmap) data.getExtras().get("data");
                blurimage.setImageBitmap(photo);
                PhotoUrl = getStringImage(photo);
                Log.v(ContentValues.TAG, "base64" + PhotoUrl);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blurimage.setImageBitmap(photo);
    }

}
