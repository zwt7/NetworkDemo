package com.example.networkdemo;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

@GlideModule
public class OkHttpGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context,
                                   @NonNull Glide glide,
                                   @NonNull Registry registry) {
        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        registry.replace(GlideUrl.class, InputStream.class,new OkHttpUrlLoader.Factory(client));
    }
}