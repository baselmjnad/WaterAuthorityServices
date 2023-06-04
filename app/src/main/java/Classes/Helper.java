package Classes;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.waterauthorityservices.AttachSubscriptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Helper {

    public String MainUrl = "http://www.wcsapi23.somee.com/";
}


//    public void Test(View view) {
//        Gson gson = new Gson();
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://www.wcs-api.somee.com/subscription/getWithConsumer/7")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                tvAttachError.setText("failed to connect to api");
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//
//                if (response.isSuccessful()) {
//                    String res = response.body().string();
//                    Subscription subscription = gson.fromJson(res, Subscription.class);
//                    AttachSubscriptions.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tvAttachError.setText(subscription.consumer.consumerName);
//                        }
//                    });
//
//
//                } else {
//                    tvAttachError.setText("System Error");
//                }
//            }
//        });
//
//    }
