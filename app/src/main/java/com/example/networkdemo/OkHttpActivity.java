package com.example.networkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.networkdemo.model.Ip;
import com.example.networkdemo.model.IpData;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {
    private ScrollView scrollView;
    private TextView tvResult;
    private ImageView img2;
    private Button Get,Post,Update,Download;
    private static final String IP_BASE_URL="http://ip.taobao.com/service/getIpInfo.php";
    private static final String IP_URL=IP_BASE_URL+"?ip=221.226.155.13";

    private static final String UPLOAD_FILE_URL = "https://api.github.com/markdown/raw";
    private static final String DOWNLOAD_URL =
            "https://github.com/zhayh/AndroidExample/blob/master/README.md";
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    public static final String  TAG1="OkHttpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        scrollView=findViewById(R.id.scrollView2);
        img2=findViewById(R.id.img2);
        tvResult=findViewById(R.id.tv_ok);
        Get=findViewById(R.id.bt_get2);
        Post=findViewById(R.id.bt_post2);
        Update=findViewById(R.id.bt_update2);
        Download=findViewById(R.id.bt_download2);
        Get.setOnClickListener(this);
        Post.setOnClickListener(this);
        Update.setOnClickListener(this);
        Download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //在data/<包名>/file/readme
        String path=getFilesDir().getAbsolutePath();
        switch (v.getId()){
            case R.id.bt_get2:
                scrollView.setVisibility(View.VISIBLE);
                img2.setVisibility(View.GONE);
                get(IP_URL);
                break;
            case R.id.bt_post2:
                scrollView.setVisibility(View.VISIBLE);
                img2.setVisibility(View.GONE);
                Map<String,String> params=new HashMap<>();
                params.put("ip","221.226.155.13");
                post(IP_URL,params);
                break;
            case R.id.bt_update2:
                scrollView.setVisibility(View.VISIBLE);
                img2.setVisibility(View.GONE);
                final String fileName=path+ File.separator+"readme.md";
                uploadFile(UPLOAD_FILE_URL,fileName);
                break;
            case R.id.bt_download2:
                downFile(DOWNLOAD_URL,path);
                break;
        }
    }



    private void get(String url){
        Request request=new Request.Builder().url(url)
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36" +
                                "(KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .addHeader("Accept","application/json")
                .get()
                .method("GET",null)
                .build();

        //发送请求，并处理回调
        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    //获得响应主体的json字符串
                    String json=response.body().string();
                    //使用FastJson库解析json字符串
                    final Ip ip= JSON.parseObject(json,Ip.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //根据返回的code判断过去是否成功
                            if(ip.getCode()!=0){
                                tvResult.setText("未获得数据");

                            }
                            else {
                                //解析数据
                                IpData data=ip.getData();
                                //显示出来的只是放进去的值，别的那么多数据，显示不出来
                                tvResult.setText(data.getIp()+","+data.getArea());
                            }
                        }
                    });
                }
            }
        });
    }
    private RequestBody setRequestBody(Map<String,String> params){
        FormBody.Builder builder=new FormBody.Builder();
        for (String key:params.keySet()){
            builder.add(key,params.get(key));
        }
        return builder.build();
    }
    private void post(String url,Map<String,String> params){
        RequestBody body=setRequestBody(params);
        Request request=new Request.Builder().url(url).post(body)
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36"+" " +
                                "(KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36")
                .addHeader("Accept","application/json")
                .build();

        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String json=response.body().string();
                    final Ip ip= JSON.parseObject(json,Ip.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(ip.getCode()!=0){
                                tvResult.setText("未获得数据");

                            }
                            else{
                                IpData data=ip.getData();
                                tvResult.setText(data.getIp()+","+data.getArea());
                            }
                        }
                    });
                }
            }
        });
    }
    //上传文件
    private void uploadFile(String url, String fileName) {
        Request request =new Request.Builder().url(url)
                .post(RequestBody.create(new File(fileName),MEDIA_TYPE_MARKDOWN))
                .build();
        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.e(TAG1,e.getMessage());
                tvResult.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("上传失败,"+ e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText("上传成功，"+str);
                        }
                    });
                }
                else
                    Log.d(TAG1,response.body().string());
            }
        });
    }
    //提交Multipart图片
   public static MediaType MEDIA_TYPE_PNG=MediaType.parse("img/png");
    private void uploadImage(String url,String fileName){
        //1.创建请求主体RequestBody
        RequestBody fileBody=RequestBody.create(new File(fileName),MEDIA_TYPE_PNG);
        RequestBody body=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title","头像")
                .addFormDataPart("file",fileName,fileBody)
                .build();
        //2.创建请求
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //
        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                Log.e(TAG1,e.getMessage());
                tvResult.post(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("上传失败,"+ e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText("上传成功，"+str);
                        }
                    });
                }
                else
                    Log.d(TAG1,response.body().string());
            }
        });
    }

    /*将输入流写入文件
    @param is 输入流对象
    @param path 存储路径
    @param fileName 存储文件名

    * */
    //将输入流写入文件
   public void writeFile(InputStream is,String path,String fileName) throws IOException {
        //1.根据path创建目录对象，并检查path是否存在，不存在就创建
        File directory=new File(path);
        if(!directory.exists()){
            directory.mkdirs();
        }
        //2.根据path和fileName创建文件对象，如果文件存在则删除
        File file=new File(path,fileName);
        if (file.exists()){
            file.delete();
        }
        //3.创建文件输出流对象，根据输入流创建缓冲输入流对象

        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        //4.以每次1024和字节写入输入流对象
        byte[] buffer=new byte[1024];
        int len;
        while((len =bis.read(buffer))!=-1){
            fos.write(buffer,0,len);
        }
        fos.flush();
        //5.关闭输入流，输出流对象
        fos.close();
        bis.close();
    }
    //下载文件
    private void downFile(final String url,final String path){
       //1.Request对象
        Request request=new Request.Builder().url(url).build();
        //okHttpClient对象，发送请求，并处理回调
        OkHttpClient client=HttpsUtil.handleSSLHandshakeByOkHttp();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                Log.e("OkHttpActivity",e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText("文件下载失败");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    //1.获取下载文件的后缀名
                    String ext=url.substring(url.lastIndexOf(".")+1);
                    //2.根据当前时间创建文件名，避免重名冲突
                    final String fileName=System.currentTimeMillis()+"."+ext;
                    //3.获得响应字体的字节流
                    InputStream is=response.body().byteStream();
                    //4.将文件写入path目录
                    writeFile(is,path,fileName);
                    //5.在界面上给出提示信息
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(fileName+"下载成功，存放在"+path);
                        }
                    });
                }
            }
        });
    }
}
