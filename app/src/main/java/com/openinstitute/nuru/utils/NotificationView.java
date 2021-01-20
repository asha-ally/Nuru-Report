package com.openinstitute.nuru.utils;

import android.app.Activity;
import android.os.Bundle;

import com.openinstitute.nuru.R;

public class NotificationView extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}