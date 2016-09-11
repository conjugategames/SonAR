package com.conjugategames.everestservice;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.harman.everestelite.Bluetooth;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    Button stopButton;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.button_start);
        stopButton = (Button) findViewById(R.id.button_stop);
        activity = this;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EVEREST","calling startService(new Intent(MainActivity.this, EverestService.class))");
                startService(new Intent(MainActivity.this, EverestService.class));
            }
        });

        stopButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("EVEREST","calling stopService((new Intent(MainActivity.this, EverestService.class)))");
                stopService((new Intent(MainActivity.this, EverestService.class)));
            }
        }));




    }
}
