package com.example.sun.httptest;

import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.ResultSet;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private String responseData;
    private TextView tv;
    private EditText et_username;
    private EditText et_userpwd;
    private Button bt;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        bt=findViewById(R.id.connect);
        bt.setOnClickListener(this);
        tv=findViewById(R.id.text);
        et_username=findViewById(R.id.username);
        et_userpwd=findViewById(R.id.userpwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connect:
                try {
                    if(checkNetwork())
                         ConnectHttp();
                    else
                        Toast.makeText(MainActivity.this,"网络未连接",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void ConnectHttp() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                client.connectTimeoutMillis();
                client.readTimeoutMillis();
                RequestBody requestBody=new FormBody.Builder()
                        .add("username", String.valueOf(et_username.getText()))
                        .add("userpwd",String.valueOf(et_userpwd.getText()))
                        .build();
                Request request=new Request.Builder()
                        .url("http://120.78.159.172:8080/test1/FirstServlet")
                        .post(requestBody)
                        .build();
                Response response= null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    responseData=response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if("false".equals(responseData))
                             tv.setText(responseData);
                            else {
                                Gson gson=new Gson();
                                UserData userData=gson.fromJson(responseData,UserData.class);
                                StringBuilder data=new StringBuilder();
                                data.append("ID: "+userData.getID()+"\n");
                                data.append("设备ID: "+userData.getDeviceID()+"\n");
                                data.append("用户名: "+userData.getUsername()+"\n");
                                data.append("昵称: "+userData.getNickname()+"\n");
                                data.toString();
                                tv.setText(data);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "ConnectHttp: "+responseData);

            }
        }).start();

    }

    // 检测网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(MainActivity.this.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
