package Classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waterauthorityservices.AttachSubscriptions;
import com.example.waterauthorityservices.NewMeter;
import com.example.waterauthorityservices.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Helper {

    public String MainUrl = "http://www.wcsapi23.somee.com/";
}

//-----------------------GET Method with OBJECT response-----------------------------------------------------------
//    public void Test(View view) {
//        Gson gson = new Gson();
//        OkHttpClient client = new OkHttpClient();
//        ServicesRequest request = new ServicesRequest.Builder()
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


//---------------------------Alert Dialog---------------------------------------------------------------------
//    AlertDialog.Builder builder=new AlertDialog.Builder(AttachSubscriptions.this);
//        builder.setTitle("Confirmation!!");
//                builder.setMessage("Are you sure this subscription belongs to you?");
//                builder.setCancelable(false);
//                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//@Override
//public void onClick(DialogInterface dialog, int which) {
//        Send();
//        }
//        });
//        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//@Override
//public void onClick(DialogInterface dialog, int which) {
//        dialog.cancel();           }
//        });
//        AlertDialog alertDialog=builder.create();
//        alertDialog.show();


//-------------Toast------------------------
//    Toast.makeText(getApplicationContext(), "YOUR REQUEST IS SENT", Toast.LENGTH_LONG).show();



//------------------------GET with jsonArray Response------------------------------------


//--------------------PHOTO HANDLING---------------------------------------
//public class NewMeter extends AppCompatActivity {
//    ImageView imageNewMeter,imageTest;
//    Integer pic_id=123;
//    byte[] photArray;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_meter);
//
//        imageNewMeter=findViewById(R.id.imageNewMeter);
//        imageTest=findViewById(R.id.imageTest);
//
//    }
//    public void OpenCamera(View view){
//        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Start the activity with camera_intent, and request pic id
//        startActivityForResult(camera_intent, pic_id);
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == pic_id) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageNewMeter.setImageBitmap(photo);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            photArray=baos.toByteArray();
//            try {
//                baos.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//    }
//    public void byteToPhpto(View view){
//        imageTest.setImageBitmap(BitmapFactory.decodeByteArray(photArray, 0, photArray.length));
//
//    }
//}
//--------------------PHOTO HANDLING end---------------------------------------

//    private void ShowMesBox(String reqNo) {
//
//
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(NewMeter.this);
//        builder.setTitle("Success");
//        builder.setMessage("Your new Meter request No is: "+reqNo);
//        builder.setIcon(R.drawable.baseline_check_circle_24);
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
