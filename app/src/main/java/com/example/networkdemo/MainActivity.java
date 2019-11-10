package com.example.networkdemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button Connection,OkHttp,Acquired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connection=findViewById(R.id.bt_connect);
        OkHttp=findViewById(R.id.bt_OkHttp);
        Acquired=findViewById(R.id.bt_acquired);
        Connection.setOnClickListener(this);
        OkHttp.setOnClickListener(this);
        Acquired.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_connect:
                Intent intent=new Intent(MainActivity.this,ResultActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_OkHttp:
                intent=new Intent(MainActivity.this,OkHttpActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_acquired:
                intent=new Intent(MainActivity.this,AcquiredActivity.class);
                startActivity(intent);

        }
    }
}
