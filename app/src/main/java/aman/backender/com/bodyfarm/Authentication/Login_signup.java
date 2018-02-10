package aman.backender.com.bodyfarm.Authentication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aman.backender.com.bodyfarm.R;
import aman.backender.com.bodyfarm.Utils.Constant;
import aman.backender.com.bodyfarm.activity.Home;
import aman.backender.com.bodyfarm.retrofit.RetrofitResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static aman.backender.com.bodyfarm.activity.Splash.mPrefs;

public class Login_signup extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.inputLoginId)
    EditText inputLoginId;
    @BindView(R.id.inputPassword)
    EditText inputPassword;

    private static final int REQ_CODE = 9001;
    private GoogleApiClient googleApiClient;
    private CallbackManager callbackManager;
    private String TAG = "", email, firstName, lastName, gender, name, country, picPath, id, dob;
    private LoginManager fbLoginManager;
    public static int mode;
    private JSONObject response_userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        initGLogin();
        initFbLogin();
    }

    @OnClick(R.id.btnSignUp)
    public void signUp(View v) {
        mode = 0;
        Intent i = new Intent(Login_signup.this, Register_gym.class);
        startActivity(i);
    }

    @OnClick(R.id.btnLogin)
    public void login(View v) {
        if (inputLoginId.getText().toString().trim().length() <= 0) {
            inputLoginId.setError("Please Enter the User id");
            return;
        } else if (inputPassword.getText().toString().trim().length() <= 0) {
            inputPassword.setError("Please Enter the Password");
            return;
        } else {
            mode = 0;
            normal_signin(inputLoginId.getText().toString(), inputPassword.getText().toString());
        }
    }

    @OnClick(R.id.imgFbSignIn)
    public void fbSignIn(View v) {
        fbLoginManager.logInWithReadPermissions(Login_signup.this, Arrays.asList("email", "public_profile", "user_birthday"));
    }

    @OnClick(R.id.imgGSignIn)
    public void gSignIn(View v) {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, REQ_CODE);
    }

    @SuppressLint("WrongViewCast")
    public void initGLogin() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            JSONObject jsonObject = new JSONObject();
            Log.d(ContentValues.TAG, "gmailresponse" + jsonObject.toString());
            try {
                jsonObject.put("id", account.getId());//this is login id in
                jsonObject.put("DisplayName", account.getDisplayName());
                jsonObject.put("FirstName", account.getGivenName());
                jsonObject.put("LastName", account.getFamilyName());
                jsonObject.put("Email", account.getEmail());
                jsonObject.put("PhotoUrl", account.getPhotoUrl());
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                printHashKey();
                Log.d(ContentValues.TAG, "gmailresponse" + jsonObject.toString());
//here
                mode = 1;
                response_userInfo = new JSONObject();
                response_userInfo = jsonObject;
                normal_signin(account.getId(), "GMAIL");//

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(ContentValues.TAG, "Error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(ContentValues.TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(ContentValues.TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(ContentValues.TAG, "printHashKey()", e);
        }
    }

    public void initFbLogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        fbLoginManager = com.facebook.login.LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                Log.e(TAG, "onCompleted:email " + object.toString());
                                email = object.optString("email");
                                name = object.optString("name");
                                firstName = object.optString("first_name");
                                lastName = object.optString("last_name");
                                gender = object.optString("gender");
                                country = object.optString("locale");
                                id = object.optString("id");
                                picPath = "http://graph.facebook.com/" + id + "/picture?type=large";
                                dob = object.optString("birthday");

                                response_userInfo = new JSONObject();
                                response_userInfo = object;
                                mode = 2;
                                normal_signin(id, "FACEBOOK");
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email,address,first_name,last_name,gender,location,birthday,locale");

                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Log.e(ContentValues.TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "onError: ");
            }
        });
    }

    public void normal_signin(final String loginId, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + Constant.LoginUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(ContentValues.TAG, "ResponseData" + response);
                RetrofitResponse retroResult = new Gson().fromJson(response, RetrofitResponse.class);
                if (retroResult.getReturnCode() == true) {
                    if (mode == 0) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("IS_login", "yes");
                        editor.putInt("recId", retroResult.getUserInfo().getRecId());
                        editor.apply();
                        Intent i = new Intent(Login_signup.this, Home.class);
                        i.putExtra("userInfo", retroResult.getUserInfo().getRecId());
                        startActivity(i);
                    } else if (mode == 1) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("IS_login", "yes");
                        editor.putInt("recId", retroResult.getUserInfo().getRecId());
                        editor.apply();
                        Intent i = new Intent(Login_signup.this, Home.class);
                        i.putExtra("userInfo", retroResult.getUserInfo().getRecId());
                        startActivity(i);
                    } else if (mode == 2) {
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("IS_login", "yes");
                        editor.putInt("recId", retroResult.getUserInfo().getRecId());
                        editor.apply();
                        Intent i = new Intent(Login_signup.this, Home.class);
                        i.putExtra("userInfo", retroResult.getUserInfo().getRecId());
                        startActivity(i);
                    }
                    //till now i have not decide yet what to return if exists or not
                } else if (retroResult.getReturnCode() == false) {
                    if (mode == 0) {
                        Toast.makeText(Login_signup.this, response.toString(), Toast.LENGTH_SHORT).show();
                    } else if (mode == 1) {
                        Intent i = new Intent(Login_signup.this, Register_gym.class);
                        i.putExtra("userInfo", response_userInfo.toString());
                        startActivity(i);
                    } else if (mode == 2) {
                        Intent i = new Intent(Login_signup.this, Register_gym.class);
                        i.putExtra("userInfo", response_userInfo.toString());
                        startActivity(i);
                    }

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
                params.put("loginId", loginId);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
