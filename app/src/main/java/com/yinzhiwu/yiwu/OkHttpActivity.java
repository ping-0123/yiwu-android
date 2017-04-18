package com.yinzhiwu.yiwu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yinzhiwu.yiwu.view.Department;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {

    private static final String TAG = "OkHttpActivity";

    private TextView tvResult;

    private EditText etMd5Input;

    private TextView tvMd5Output;
    
    OkHttpClient okHttpClient = new OkHttpClient();

    private MessageDigest md5 = MessageDigest.getInstance("SHA");

    private Gson gson = new Gson();

    private String url = "http://yzw.chenksoft.com:8080/yiwu/api/district/list";

    private Department dept;

    public OkHttpActivity() throws NoSuchAlgorithmException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);

        //设置Cookie 全区Session

        tvResult = (TextView) findViewById(R.id.textView_result);
        etMd5Input = (EditText) findViewById(R.id.et_md5_input);
        etMd5Input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvMd5Output = (TextView) findViewById(R.id.tv_md5_output);
    }

    public void testMd5(View v){
        Toast.makeText(this, md5.getClass().getName(), Toast.LENGTH_SHORT).show();
        md5.update(etMd5Input.getText().toString() .getBytes());
        Log.d(TAG, "testMd5: " + etMd5Input.getText());
        byte[] m = md5.digest();
        tvMd5Output.setText(getString(m));
    }

    private  String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }


    public void doGet(View v){
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        doRequest(request);
    }

    public void doPost(View v) {
        FormBody.Builder builder = new FormBody.Builder();
        RequestBody requestBody = builder.add("districtId","106").build();
        Request.Builder rB = new Request.Builder();
        Request request = rB.url("http://yzw.chenksoft.com:8080/yiwu/api/store/list").post(requestBody).build();
        doRequest(request);

    }

    private void doRequest(Request request) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                final String s  = response.body().string();
                final List<Department> depts = gson.fromJson(s,new TypeToken<List<Department>>(){}.getType());
                Log.i(TAG, "onResponse: " + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(depts.get(0).getName());
                    }
                });
            }
        });
    }


}
