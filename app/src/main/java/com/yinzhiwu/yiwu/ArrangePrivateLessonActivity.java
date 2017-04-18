package com.yinzhiwu.yiwu;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yinzhiwu.yiwu.view.Employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArrangePrivateLessonActivity extends AppCompatActivity {
    private static final String TAG = "ArrangePrivateLessonAct";

    private OkHttpClient client = new OkHttpClient();

    private Gson gson = new Gson();

    private Spinner spinnerCoach;

    private ArrayAdapter adapter;

    private List<Employee> mCoaches = new ArrayList<>();

    private String[] strings = new String[]{"张三", "李四", "王五", "赵六", "钱七", "孙八"};

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_private_lesson);
        spinnerCoach = (Spinner) findViewById(R.id.spinner_coach);
        setCoaches();

         adapter = new ArrayAdapter<Employee>(this, android.R.layout.simple_spinner_item, mCoaches);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                Log.d(TAG, "onChanged() called");
            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,strings);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoach.setAdapter(adapter);

    }

    private  void setCoaches(){
        String url = "http://192.168.0.115:8080/yiwu/api/employee/getAllCoaches";
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        doRequest(request);

    }

    private  void doRequest(Request request) {
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                final String s  = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCoaches = gson.fromJson(s,new TypeToken<List<Employee>>(){}.getType());
                        adapter.addAll(mCoaches);
                    }
                });


            }
        });
    }
}
