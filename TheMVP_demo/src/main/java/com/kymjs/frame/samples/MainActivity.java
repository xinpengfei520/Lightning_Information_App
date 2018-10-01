/*
 * Copyright (c) 2015, 张涛.
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
package com.kymjs.frame.samples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kymjs.frame.samples.demo1.SimpleActivity;
import com.kymjs.frame.samples.demo2.Demo2Activity;
import com.kymjs.frame.samples.demo3.ShellActivity;
import com.kymjs.frame.samples.demo5.Demo5Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startActivity(new Intent(this, SimpleActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, Demo2Activity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, ShellActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, com.kymjs.frame.samples.demo4.ShellActivity.class));
                break;
            case R.id.button5:
                startActivity(new Intent(this, Demo5Activity.class));
                break;
            default:
                break;
        }
    }
}
