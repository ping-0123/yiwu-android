package com.yinzhiwu.yiwu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Gson gson = new Gson();

    private ImageView sexGirl;
    private Bitmap bitmap;

    private TextView districtsTextView;
    private String districts;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                Log.d(TAG, "handleMessage() called with: msg = [" + msg + "]");
                sexGirl.setImageBitmap(bitmap);
                districtsTextView.setText(districts);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sexGirl = (ImageView) findViewById(R.id.image_sex_girl);
        districtsTextView = (TextView) findViewById(R.id.text_districts);
        new Thread() {

            @Override
            public void run() {
                try {
                    Log.d(TAG, "run() called");
                    URL url = new URL("http://s1.dwstatic.com/group1/M00/48/78/7e5836513a9b9ec8bbcacb3c7c62a858.jpg");
                    InputStream is = url.openStream();
                    bitmap = BitmapFactory.decodeStream(is);

                    URL url2 = new URL("http://yzw.chenksoft.com:8080/yiwu/api/district/list");
                    InputStream is2 = url.openStream();
                    districts = is2.toString();

                    handler.sendEmptyMessage(0x123);
                    is.close();
                    is2.close();


                } catch (MalformedURLException e) {
                    Log.e(TAG, "run: MalformedURLException ", e);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.e(TAG, "run: ", e);
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
