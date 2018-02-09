package aman.backender.com.bodyfarm.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.android.volley.Response;

import aman.backender.com.bodyfarm.Authentication.Register_gym;
import aman.backender.com.bodyfarm.R;
import aman.backender.com.bodyfarm.Utils.Constant;
import aman.backender.com.bodyfarm.bean.GymUserInfo;
import aman.backender.com.bodyfarm.bean.UserInfo;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static aman.backender.com.bodyfarm.activity.Splash.mPrefs;

public class Client_data extends AppCompatActivity {
    int RecId ;
    String GymRefId = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_data);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        RecId = bundle.getInt("userInfo");
        Toast.makeText(Client_data.this, String.valueOf(RecId), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.save_client)
    public void registerMe(View v) {
        Register_Me();
    }

    public void Register_Me() {

        GymUserInfo userInfo = new GymUserInfo();
        userInfo.setGymRefId(RecId);  //login User info recId
        //userInfo.setRecId(Integer.parseInt(RecId));
        userInfo.setFirstName("Aman");
        userInfo.setLastName("Aggarwal");
        userInfo.setEmailId("");
        userInfo.setAddress("Karol Bagh");
        userInfo.setDob("21-Nov-1994");
        userInfo.setFeeAmount(600);
        userInfo.setFeeDate("21-Nov-1994");
        userInfo.setContactNo("");
        //userInfo.setActive(0);
        //userInfo.setUpdatedDate(Calendar.getInstance().getTime().toString());
        //userInfo.setCreatedDate(Calendar.getInstance().getTime().toString());

        List<GymUserInfo> arrGymUser = new ArrayList<>();
        arrGymUser.add(userInfo);

        // ab logout krke login  kr,, arr users milega return mein


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //final String sign_up = new Gson().toJson(userInfo); //for single user registeration
        final String gymUsers = new Gson().toJson(arrGymUser);      //for multiple users
        Log.e("DATA", "Register_Me" + gymUsers);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
               // Constant.BASE_URL + Constant.REGISTER_GYM_USER,     //single user
                Constant.BASE_URL + Constant.REGISTER_GYM_USERS,       //multiple users
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(ContentValues.TAG, "Response_Data" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("gymUserInfo", sign_up);      //for single client register
                params.put("arrGymUser", gymUsers);      //for multiple users
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
}
