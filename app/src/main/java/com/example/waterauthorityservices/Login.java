package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import Classes.Helper;
import Classes.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    Helper helper = new Helper();
    Button btLogin;
    EditText etUsername, etPassword;
    TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin = findViewById(R.id.btLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
    }


    public void LoginUser(View view) {
        tvError.setText("");
        if (!TextUtils.isEmpty(etUsername.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())) {

            OkHttpClient client = new OkHttpClient();
            User user=new User();
            user.userName=etUsername.getText().toString().trim();
            user.password=etPassword.getText().toString().trim();
            Gson gson = new Gson();
            String json=gson.toJson(user);

            // request body start--------------------------------------------
            RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),json);
            // request body end----------------------------------------------

            //request start----------------------------------------------------
            Request request = new Request.Builder()
                    .url(helper.MainUrl+"user/login")
                    .post(requestBody)
                    .build();
            //request end----------------------------------------------------


            //response start----------------------------------------------------

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    tvError.setText("Connection Error!");

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {

                        String respString = response.body().string();
                        User logedUser=gson.fromJson(respString, User.class);
                        if(logedUser.accountActive && logedUser.userType.equals("consumer")){
                        Login.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new Intent(Login.this,UserActivity.class);
                                intent.putExtra("userName",logedUser.userName );
                                intent.putExtra("userId",logedUser.id.toString());
                                startActivity(intent);
                            }
                        });}else {
                            tvError.setText("User not Active or not consumer");
                        }
                    } else {
                        tvError.setText("Username and/or password not correct");
                    }
                }
            });

            //response end--------------------------------------------------

        }else {
            tvError.setText("User Name or Password is missing!");
        }
    }
    public void GotoRegister(View view){
        tvError.setText("");
        Intent intent=new Intent(Login.this,Register.class);
        startActivity(intent);

    }


}
