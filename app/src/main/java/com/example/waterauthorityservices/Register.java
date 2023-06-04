package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;

import Classes.Consumer;
import Classes.Helper;
import Classes.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    EditText etConsumerName, etConsumerAddress, etConsumerPhone, etRegisterUserName, etRegisterPassword, etPasswordConfirm;
    TextView tvError;
    Consumer consumer = new Consumer();
    Helper helper = new Helper();
    Boolean userExist = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etConsumerName = findViewById(R.id.etConsumerName);
        etConsumerAddress = findViewById(R.id.etConsumerAddress);
        etConsumerPhone = findViewById(R.id.etConsumerPhone);
        etRegisterUserName = findViewById(R.id.etRegisterUserName);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        tvError = findViewById(R.id.tvError);
    }

    public void RegisterNewConsumer(View view) {
        tvError.setText("");
        if (!TextUtils.isEmpty(etConsumerName.getText().toString()) &&
                !TextUtils.isEmpty(etConsumerAddress.getText().toString()) &&
                !TextUtils.isEmpty(etConsumerPhone.getText().toString()) &&
                !TextUtils.isEmpty(etRegisterUserName.getText().toString()) &&
                !TextUtils.isEmpty(etRegisterPassword.getText().toString()) &&
                !TextUtils.isEmpty(etPasswordConfirm.getText().toString())) {

            if (TextUtils.equals(etRegisterPassword.getText().toString(), etPasswordConfirm.getText().toString())) {
                // start check if user exist
                OkHttpClient client = new OkHttpClient();
                String uri = helper.MainUrl + "user/exists/" + etRegisterUserName.getText().toString().trim();
                Request request = new Request.Builder()
                        .url(uri)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        tvError.setText("Connection Error");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        if (response.isSuccessful()) {
                            String respo = response.body().string();

                            Register.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (TextUtils.equals(respo, "true")) {
                                        tvError.setText("User Exist, please try again.");
                                        userExist = true;
                                    } else {
                                        userExist = false;
                                    }
                                }
                            });
                        }
                    }
                });

                // end check if user exist

                User user1 = new User();
                user1.userName = etRegisterUserName.getText().toString().trim();
                user1.password = etRegisterPassword.getText().toString().trim();
                user1.userType = "consumer";
                user1.accountActive = false;
                consumer.consumerName = etConsumerName.getText().toString().trim();
                consumer.consumerAddress = etConsumerAddress.getText().toString().trim();
                consumer.consumerPhone = etConsumerPhone.getText().toString().trim();
                consumer.user = user1;

                // start add consumer---------------------------------------------------------------
                if (!userExist) {

                    RegisterMethod(consumer);

                }

                // end add consumer-----------------------------------------------------------------
            } else {
                tvError.setText("Check password and confirm it");
            }


        } else {
            tvError.setText("Please enter all data");
        }

    }

    public void RegisterMethod(Consumer consumer) {

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(consumer);

        // request body start--------------------------------------------
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        // request body end----------------------------------------------

        //request start----------------------------------------------------
        Request request = new Request.Builder()
                .url(helper.MainUrl + "consumer")
                .post(requestBody)
                .build();
        //request end----------------------------------------------------

        //response start----------------------------------------------------

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvError.setText("failed to connect to system");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {


                    Register.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etConsumerName.getText().clear();
                            etConsumerAddress.getText().clear();
                            etConsumerPhone.getText().clear();
                            etRegisterUserName.getText().clear();
                            etRegisterPassword.getText().clear();
                            etPasswordConfirm.getText().clear();
                            tvError.setText("");
                            Toast.makeText(getApplicationContext(), "Consumer added successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    tvError.setText("Faild to add Consumer");
                }
            }
        });

        //response end--------------------------------------------------


    }

}
