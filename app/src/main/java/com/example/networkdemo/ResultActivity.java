package com.example.networkdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php?ip=221.226.155.4";


    private TextView tv_info;
    private ImageView Image;
    private Button Get,Post,Update,Download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tv_info=findViewById(R.id.tv_info);
        Image=findViewById(R.id.img);
        Get=findViewById(R.id.bt_get);
        Post=findViewById(R.id.bt_post);
        Update=findViewById(R.id.bt_update);
        Download=findViewById(R.id.bt_download);
        Get.setOnClickListener(this);
        Post.setOnClickListener(this);
        Update.setOnClickListener(this);
        Download.setOnClickListener(this);

       Glide.with(this)
                .load("https://www.baidu.com/img/bd_logo1.png")
                .centerInside()
                .into(Image);
}


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_get:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String result=NetworkUtils.get(IP_URL);
                        Log.d("MainActivity",result);
                        if(result!=null){
                            tv_info.post(new Runnable() {
                                @Override
                                public void run() {
                                    tv_info.setText(result);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_info.setText("数据为null");
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.bt_post:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<NameValuePair> params=new ArrayList<>();
                        params.add(new BasicNameValuePair("ip","221.226.155.4"));
                        try {
                            final String result=NetworkUtils.post(IP_URL,params);
                            if(result!=null){
                                Log.d("MainActivity",result);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_info.setText(result);
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_info.setText("请求失败，未获得数据");
                                    }
                                });
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.bt_update:
                break;
            case R.id.bt_download:
                break;
        }
    }
}
