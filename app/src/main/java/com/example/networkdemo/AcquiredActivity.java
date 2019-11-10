package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AcquiredActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvShow;
    Button weather,price,news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquired);

        tvShow=findViewById(R.id.tv_display);
        weather=findViewById(R.id.weather_forest);
        price=findViewById(R.id.today_oil);
        news=findViewById(R.id.news);
        weather.setOnClickListener(this);
        price.setOnClickListener(this);
        news.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather_forest:
                break;
            case R.id.today_oil:
                break;
            case R.id.news:
                break;

        }
    }
}
