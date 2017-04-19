package com.yinzhiwu.yiwu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.yinzhiwu.net.okhttp.CommonOkHttpClient;
import com.yinzhiwu.net.okhttp.listener.DisposeDataHandle;
import com.yinzhiwu.net.okhttp.listener.DisposeDataListener;
import com.yinzhiwu.sys.YiwuApplication;
import com.yinzhiwu.yiwu.view.Store;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class ArrangePrivateLessonActivity extends AppCompatActivity {
    private static final String TAG = "ArrangePrivateLessonAct";

    private  YiwuApplication application;


    private Spinner mStoreSpinner;
    private ArrayAdapter<Store> mStoreAdapter;
    private List mStores = new ArrayList<>();

    private EditText mDateEditText;
    private Button  mDateButton;

    private EditText mStartTimeEditText;
    private Button mStartTimeButton;

    private EditText mEndTimeEditText;
    private Button  mEndTimeButton;

    private EditText mMemberEditText;
    private Button  mMemberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_private_lesson);
        application = (YiwuApplication) getApplication();
        mStoreSpinner = (Spinner) findViewById(R.id.spinner_store);

        mStoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,mStores);
        mStoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStoreSpinner.setAdapter(mStoreAdapter);
        mDateEditText = (EditText) findViewById(R.id.edit_date);
        mDateButton = (Button) findViewById(R.id.button_date);
        mMemberEditText = (EditText) findViewById(R.id.edit_member);
        mMemberButton = (Button) findViewById(R.id.button_member);


        setStoreList();
    }


    public void searchMember(View view) {
    }

    public void selectDate(View view) {
    }

    public void selectTime(View view) {
    }


    private void setStoreList(){
        String url = application.getBaseApiUrl() + "api/store/getAllApiStores";
        CommonOkHttpClient.get(new Request.Builder().url(url).get().build(), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mStores.clear();
                mStores.addAll((List<Store>)responseObj);
                mStoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        }, Store.class));
    }
}
