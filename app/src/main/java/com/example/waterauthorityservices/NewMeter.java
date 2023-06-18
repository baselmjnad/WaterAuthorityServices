package com.example.waterauthorityservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import Classes.Consumer;
import Classes.Department;
import Classes.Helper;
import Classes.RequestDetails;
import Classes.SbyteDocument;
import Classes.ServicesRequest;
import Classes.Subscription;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewMeter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String userName, userId, meterType;
    Integer pic_id = 123;
    byte[] photArray;

    EditText etNewMeterAddress;
    ImageView imageViewDoc;
    TextView tvNewMeterWelcome, tvNewMeterError;
    Spinner spinnerNewMeter;
    TextView tvNewMeterConsumerName, tvNewMeterPhone, tvNewMeterAddress;
    Helper helper = new Helper();
    private static final String[] subsTypes = {"residential", "commercial", "industrial"};
    Consumer consumer1 = new Consumer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meter);

        etNewMeterAddress = findViewById(R.id.etNewMeterAddress);
        imageViewDoc = findViewById(R.id.imageViewDoc);
        spinnerNewMeter = findViewById(R.id.spinnerNewMeter);
        tvNewMeterWelcome = findViewById(R.id.tvNewMeterWelcome);
        tvNewMeterError = findViewById(R.id.tvNewMeterError);
        tvNewMeterConsumerName = findViewById(R.id.tvNewMeterConsumerName);
        tvNewMeterPhone = findViewById(R.id.tvNewMeterPhone);
        tvNewMeterAddress = findViewById(R.id.tvNewMeterAddress);
        Intent t = getIntent();
        userName = t.getStringExtra("userName");
        userId = t.getStringExtra("userId");

        tvNewMeterWelcome.setText("User: " + userName.toUpperCase());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewMeter.this,
                android.R.layout.simple_spinner_item, subsTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNewMeter.setAdapter(adapter);
        spinnerNewMeter.setOnItemSelectedListener(this);
        GetConsumer(userId);

    }

    public void GetConsumer(String id) {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(helper.MainUrl + "consumer/getbyuser/" + id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                tvNewMeterError.setText("failed to connect to api");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String res = response.body().string();
                    Consumer consumer = gson.fromJson(res, Consumer.class);

                    NewMeter.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            consumer1 = consumer;
                            tvNewMeterConsumerName.setText(consumer.consumerName);
                            tvNewMeterPhone.setText(consumer.consumerPhone);
                            tvNewMeterAddress.setText(consumer.consumerAddress);
                        }
                    });


                } else {
                    tvNewMeterError.setText("System Error");
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                meterType = "residential";
                break;
            case 1:
                meterType = "commercial";

                break;
            case 2:
                meterType = "industrial";

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void OpenCamera(View view) {
        photArray = null;
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera_intent, pic_id);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pic_id && resultCode==RESULT_OK && data !=null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewDoc.setImageBitmap(photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            photArray = baos.toByteArray();
            try {
                baos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public void SendNewMeterRequest(View view) {
        if (photArray != null) {
            ServicesRequest req = new ServicesRequest();
            RequestDetails details = new RequestDetails();
            SbyteDocument sbyteDocument = new SbyteDocument();

            details.newSubAddress = etNewMeterAddress.getText().toString();
            details.newSubType = meterType;

//           details.document = photArray;


            Department department = new Department();
            department.departmentName = "DocumentControllerDep";
            department.id = 1;
            req.consumer = consumer1;
            req.requestDate = Calendar.getInstance().getTime();
            req.requestType = "new";
            req.requestStatus = "onprogress";
            req.currentDepartment = department;
            req.details = details;
            sbyteDocument.request = req;
            sbyteDocument.document = photArray;

            OkHttpClient client = new OkHttpClient();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

            String json = gson.toJson(sbyteDocument);

            // request body start--------------------------------------------
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            // request body end----------------------------------------------

            //request start----------------------------------------------------
            Request request = new Request.Builder()
                    .url(helper.MainUrl + "Request/postSbyte")
                    .post(requestBody)
                    .build();
            //request end----------------------------------------------------


            //response start----------------------------------------------------

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    tvNewMeterError.setText("Connection Error!");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if (response.isSuccessful()) {

                        String respo = response.body().string();
//                        ServicesRequest req1 = gson.fromJson(respString, ServicesRequest.class);
                        NewMeter.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowMesBox(respo);
                            }
                        });
                    } else {
                        tvNewMeterError.setText("Sending request Error!");
                    }
                }
            });
        } else {
            tvNewMeterError.setText("Please take photo for Apartment document");
        }
    }

    private void ShowMesBox(String reqNo) {


        AlertDialog.Builder builder = new AlertDialog.Builder(NewMeter.this);
        builder.setTitle("Success");
        builder.setMessage("Your new Meter request No is: "+reqNo);
        builder.setIcon(R.drawable.baseline_check_circle_24);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
