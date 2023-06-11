package com.example.waterauthorityservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class NewMeter extends AppCompatActivity {
    ImageView imageNewMeter, imageTest;
    Integer pic_id = 123;
    byte[] photArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meter);

        imageNewMeter = findViewById(R.id.imageNewMeter);
        imageTest = findViewById(R.id.imageTest);

    }

    public void OpenCamera(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start the activity with camera_intent, and request pic id
        startActivityForResult(camera_intent, pic_id);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageNewMeter.setImageBitmap(photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            photArray = baos.toByteArray();
            long len = photArray.length / 1024;
            String ss = String.valueOf(len);
            Toast.makeText(getApplicationContext(), ss, Toast.LENGTH_LONG).show();
            try {
                baos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void byteToPhpto(View view) {
        imageTest.setImageBitmap(BitmapFactory.decodeByteArray(photArray, 0, photArray.length));
    }
}