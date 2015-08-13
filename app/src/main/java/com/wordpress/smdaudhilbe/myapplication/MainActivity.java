package com.wordpress.smdaudhilbe.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.smdaudhilbe.myapplication.util.AudienceSpeedoMeter;

public class MainActivity extends AppCompatActivity {

    private AudienceSpeedoMeter audiSpeedoMeter;
    private EditText eTextView;
    private Toast toast;
    private TextView txtView;
    float count = 0;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audience_speedo_meter);
        initViews();
    }

    private void initViews() {
        eTextView = (EditText) findViewById(R.id.percentageEditText);
        txtView = (TextView) findViewById(R.id.percentageTxtView);
        audiSpeedoMeter = (AudienceSpeedoMeter) findViewById(R.id.audiSpeedoMeter);
    }

    @fromXML
    public void audienceSpeedoMeterClick(View view) {
        audiSpeedoMeter.performedClick();
    }

    @fromXML
    public void submitClick(View view) {
        if (TextUtils.isEmpty(eTextView.getText().toString()) || Float.parseFloat(eTextView.getText().toString()) < 0f || Float.parseFloat(eTextView.getText().toString()) > 100f) {
            showToast("Invalid Entry");
            return;
        }
        final float percentEntered = Float.parseFloat(eTextView.getText().toString());
        audiSpeedoMeter.invalidateArrowView((percentEntered * 180) / 100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count <= percentEntered) {
                    txtView.setText(String.valueOf(count++));
                    handler.postDelayed(this, 1);
                } else {
                    count = 0;
                }
            }
        }, 1);
    }

    //  showing Toast
    public void showToast(String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}