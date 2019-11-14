package com.example.networkdemo;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.networkdemo.model.WeatherCurrent;
import com.example.networkdemo.model.WeatherFuture;
import com.example.networkdemo.model.WeatherRealtime;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class AcquiredActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvShow;
    Button weather,price,news;
    private static final String KEY="1b72dd975519dcd65cebad9d737860f1";
    private static final String WEATHER_URL="http://apis.juhe.cn/simpleWeather/query";

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
                getWeather("南京");
                break;
            case R.id.today_oil:
                break;
            case R.id.news:
                break;

        }
    }

    private void getWeather(String city) {

        try {
            //1.组装数据请求的url
            String url=WEATHER_URL+"?key="+KEY+"&city="+URLEncoder.encode(city,"utf-8");
            //2.使用okHTTP发送请求
            Request request=new Request.Builder().url(url).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("AcquiredActivity",e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    //3.数据的处理
                    if(response.isSuccessful()){
                        String json=response.body().string();
                        //用Android自带的Json解析，将json字符串解析为JSONObject;
                        JSONObject obj=JSON.parseObject(json);
                        if(obj!=null){
                            JSONObject result=obj.getJSONObject("result");//result结果是city
                            JSONObject realtime=result.getJSONObject("realtime");//从结果的数组里找到realtime这个对象
                            final String city=result.getString("city");
                            final WeatherRealtime weather=JSON.parseObject(realtime.toJSONString(),
                                    WeatherRealtime.class);
                            //获取5天的天气趋势
                            JSONArray futureWeather=result.getJSONArray("future");
                            //jsonArray要转化成toJSONString(中间桥梁)
                            final List<WeatherFuture> weatherFutures=JSON.parseArray(futureWeather.toJSONString(),
                                    WeatherFuture.class);



                            //4.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    StringBuilder builder=new StringBuilder();
                                    builder.append(city).append(weather)
                                            .append("\n\n").append("5天的天气趋势");
                                    for (WeatherFuture future:weatherFutures){
                                        builder.append("\n").append(future);
                                    }
                                    tvShow.setText(builder.toString());
                                }
                            });
                        }
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
