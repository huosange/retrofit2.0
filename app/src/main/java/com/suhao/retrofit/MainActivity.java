package com.suhao.retrofit;

import android.Manifest;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Retrofit retrofit1;
    private MyService service1;
    private Retrofit retrofit2;
    private MyService service2;
    private Retrofit retrofit3;
    private MyService service3;
    private Retrofit retrofit4;
    private MyService service4;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btn10;
    private Button btn11;
    private Button btn12;
    private TextView content;
    private DownloadListener downloadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRetrofit();
        initWidgets();

        downloadListener=new DownloadListener() {
            @Override
            public void start() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content.setText("正在等待服务器写入文件");
                    }
                });
            }

            @Override
            public void onProgress(final double currentLength) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DecimalFormat decimalFormat=new DecimalFormat("0.00");
                        content.setText("进度："+decimalFormat.format(currentLength)+"%");
                    }
                });
            }

            @Override
            public void onFinish(String localPath) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content.setText("下载完成");
                    }
                });
            }

            @Override
            public void onFailure() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content.setText("下载失败");
                    }
                });
            }
        };

        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();
    }

    private void initWidgets() {
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);
        btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(this);
        btn5 = findViewById(R.id.btn5);
        btn5.setOnClickListener(this);
        btn6 = findViewById(R.id.btn6);
        btn6.setOnClickListener(this);
        btn7 = findViewById(R.id.btn7);
        btn7.setOnClickListener(this);
        btn8=findViewById(R.id.btn8);
        btn8.setOnClickListener(this);
        btn9=findViewById(R.id.btn9);
        btn9.setOnClickListener(this);
        btn10=findViewById(R.id.btn10);
        btn10.setOnClickListener(this);
        btn11=findViewById(R.id.btn11);
        btn11.setOnClickListener(this);
        btn12=findViewById(R.id.btn12);
        btn12.setOnClickListener(this);
        content = findViewById(R.id.content);
    }

    private void initRetrofit() {
        retrofit1 = new Retrofit.Builder()
                .baseUrl("http://chanyouji.com/")
                .build();
        service1 = retrofit1.create(MyService.class);

        /**
         * 忽略https证书
         */
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        retrofit2 = new Retrofit.Builder()
                .baseUrl("https://m.osportsmedia.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        service2 = retrofit2.create(MyService.class);

        retrofit3 = new Retrofit.Builder()
                .baseUrl("http://192.168.1.138:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service3 = retrofit3.create(MyService.class);

        retrofit4=new Retrofit.Builder()
                .baseUrl("http://appdl.hicloud.com/")
                .build();
        service4=retrofit4.create(MyService.class);
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            content.setText(msg.obj.toString());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                service1.getDestinations().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn2:
                service1.getDestinationById(75).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn3:
                service1.getTripsByPage(10).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn4:
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("page", 5);
                service1.getTripsByPage(map).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn5:
                service2.likeStory(112789, 1).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn6:
                Map<String, Object> map2 = new HashMap<>();
                map2.put("id", 112789);
                map2.put("c", 1);
                service2.likeStory(map2).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn7:
                Param param = new Param("suhao", "123456");
                service3.getMyServlet01(param).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
                break;
            case R.id.btn8:
                service3.uploadFile(getPart("兔子.jpg","file")).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });
                break;
            case R.id.btn9:
                List<MultipartBody.Part> files=new ArrayList<>();
                files.add(getPart("蓝色.jpg","first"));
                files.add(getPart("石头.jpg","second"));

                service3.uploadFiles(files).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("a","b");
                    }
                });
                break;
            case R.id.btn10:
                Param p = new Param("lidandan", "i love you");
                service3.uploadFileAndText(p,getPart("塔.jpg","file")).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            content.setText(response.body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                break;
            case R.id.btn11:
                service3.downloadFile().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        //下载文件放在子线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                writeFile2Disk(response,downloadListener,"wangyiyun.mp4");
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("a","b");
                    }
                });
                break;
            case R.id.btn12:
                service4.downloadEclipse().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        //下载文件放在子线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                writeFile2Disk(response,downloadListener,"haha.apk");
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("a","b");
                    }
                });
                break;
        }
    }

    private MultipartBody.Part getPart(String filename,String partName){
        File file=new File(Environment.getExternalStorageDirectory()+"/Huawei/MagazineUnlock/"+filename);
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body=MultipartBody.Part.createFormData(partName,file.getName(),requestBody);
        return body;
    }

    private void writeFile2Disk(Response<ResponseBody> response,DownloadListener downloadListener,String filename){
        downloadListener.start();
        OutputStream out=null;
        InputStream in=null;

        try{
            File file=new File(Environment.getExternalStorageDirectory()+"/Huawei/MagazineUnlock/"+filename);
            out=new FileOutputStream(file);
            in=response.body().byteStream();
            long total=in.available();
            long current=0;
            byte[] buffer=new byte[1024];
            int len=0;
            while((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
                current+=len;
                double p=((double)current)/total*100;
                downloadListener.onProgress(p);
                if(p==100){
                    downloadListener.onFinish(file.getAbsolutePath());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
