package aman.backender.com.bodyfarm.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import aman.backender.com.bodyfarm.R;
import aman.backender.com.bodyfarm.Utils.Constant;
import aman.backender.com.bodyfarm.retrofit.RetrofitResponse;

public class Home extends AppCompatActivity {
    String loginId;
    String EmailId;
    String PhotoUrl;
   // String recId;
    private int recId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        recId = bundle.getInt("userInfo");
        Log.d(ContentValues.TAG, "Request_Data" + recId);
        Register_Me(recId);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Client_data.class);
                intent.putExtra("userInfo", recId);
                startActivity(intent);
            }
        });
    }

    public void Register_Me(final int str) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constant.BASE_URL + Constant.User_Data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(ContentValues.TAG, "Response_Data" + response);
                        RetrofitResponse retroResult = new Gson().fromJson(response, RetrofitResponse.class);
                       // String arrSize = String.valueOf(retroResult.getArrUsers().size());
                       // Toast.makeText(Home.this, arrSize, Toast.LENGTH_SHORT).show();
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
                params.put("userId", String.valueOf(str));//lolx ham to pagal hai straing mai laye
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
