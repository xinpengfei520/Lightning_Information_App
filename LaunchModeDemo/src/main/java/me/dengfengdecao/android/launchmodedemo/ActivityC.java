/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.dengfengdecao.android.launchmodedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Example Activity to demonstrate the lifecycle callback methods.
 */
public class ActivityC extends AppCompatActivity {

    private String mActivityName;
    private TextView mInstanceView;
    private TextView mTaskAllView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        mActivityName = getString(R.string.activity_c);
        mInstanceView = (TextView) findViewById(R.id.instance_view_c);
        mTaskAllView = (TextView) findViewById(R.id.task_view_all_c);
        StringBuilder sb = new StringBuilder();
        sb.insert(0, this.toString() + "\r\n");
        mInstanceView.setText(sb.toString());
        sb = new StringBuilder();
        sb.insert(0, getTaskId() + "\n");
        mTaskAllView.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startActivityA(View v) {
        Intent intent = new Intent(ActivityC.this, ActivityA.class);
        startActivity(intent);
    }

    public void startActivityB(View v) {
        Intent intent = new Intent(ActivityC.this, ActivityB.class);
        startActivity(intent);
    }

    public void startActivityC(View v) {
        Intent intent = new Intent(this, ActivityC.class);
        startActivity(intent);
    }

}
