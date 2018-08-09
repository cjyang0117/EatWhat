package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class randomSuggestRul extends AppCompatActivity implements DialogInterface.OnClickListener{
    private GlobalVariable globalVariable;
    TextView textViewrul ,textViewaddr, textViewmenu, textViewprice;
    String addr;
    double geoLatitude, geoLongitude;
    String cell,star,menunum,storename,mname,num,price;
    TableLayout tbrulLayout;
    Button ebtn;
    Bundle b;
    private WebView webView;
    private Handler handler;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_suggest_rul);
        textViewrul = (TextView) findViewById(R.id.textViewstore);
        textViewaddr = (TextView)findViewById(R.id.textViewaddr);
        textViewmenu = (TextView)findViewById(R.id.textViewmenu);
        textViewprice = (TextView)findViewById(R.id.textViewprice);
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        b = this.getIntent().getExtras();

        if (b != null) {//隨機
            try {
                if(b.getInt("check")!=2) {
                    JSONObject json_read = new JSONObject(b.getString("data"));
                    JSONArray j1 = json_read.getJSONArray("data");
                    JSONArray j2 = new JSONArray();
                    for (int i = 0; i < j1.length(); i++) {//拆隨機資料
                        j2 = j1.getJSONArray(i);
                    }
                    storename = j2.get(1).toString();//店名
                    menunum =  j2.get(0).toString().trim();//店號
                    addr = j2.get(2).toString().trim();//店家地址
                    cell = j2.get(3).toString();//電話
                    star = j2.get(4).toString();//星數
                    num = j2.get(5).toString();//菜號
                    price = j2.get(7).toString();
                    textViewmenu.setText(j2.get(6).toString().trim());//菜品
                    textViewprice.setText( price + "元");//價格
                }else{//提問
                    storename = b.getString("data1").toString().trim();//店名
                    num = b.getString("data8").toString().trim();//菜號
                    price =b.getString("data4").toString().trim();
                    menunum =  b.getString("data5").toString().trim();//店號
                    textViewmenu.setText( b.getString("data3").toString().trim());//菜名
                    textViewprice.setText(price + "元");//價格
                    addr =  b.getString("data2").toString().trim();//地址
                    cell = b.getString("data6").toString().trim();//電話
                    star =  b.getString("data7").toString().trim();//星數
                }
                textViewrul.setText(storename);//店名
                textViewaddr.setText(addr);//地址
                textViewaddr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        openMap();//開地圖
                    }
                });
                textViewrul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        gotostore();//前往店家頁面
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception", "StoreError=" + e.toString());
            }
            Button consider=(Button)findViewById(R.id.consider);
            consider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b=(Button)v;
                    try {
                        FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                        String s=menunum.toString().trim()+","+num.toString().trim()+","+storename.toString().trim()+","+textViewmenu.getText().toString()+","+price.toString()+",";
                        out.write(s.getBytes());
                        out.close();
                        b.setEnabled(false);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            Button eat=(Button)findViewById(R.id.eat);
            eat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ebtn=(Button)v;
                    AlertDialog.Builder b=new AlertDialog.Builder(randomSuggestRul.this);
                    //串聯呼叫法
                    b.setTitle("確認")
                            .setMessage("確定要吃這個嗎?")
                            .setPositiveButton("GO", randomSuggestRul.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
            Button again = (Button)findViewById(R.id.again);
            again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    randomSuggestRul.this.finish();
                }
            });
        }
        //--動畫
        webView=findViewById(R.id.webView);
        imageView = findViewById(R.id.imageView);

        webView.setVerticalScrollBarEnabled(false); //垂直滚动条不显示
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        WebSettings webSettings=webView.getSettings();
        webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);//屏幕适配:设置webview推荐使用的窗口，设置为true
        webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式，也设置为true
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);//是否支持缩放
        webSettings.setBuiltInZoomControls(true);//添加对js功能的支持
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(0);
        String gifPath = "file:///android_asset/box.gif";
        webView.loadUrl(gifPath);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        webView.destroy();
                        webView.setVisibility(View.INVISIBLE);
                        textViewrul.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        textViewmenu.setVisibility(View.VISIBLE);
                        textViewprice.setVisibility(View.VISIBLE);
//                        MainActivity.this.finish();
                }
            }
        };
        new Thread(){

            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                Log.i("test","System.currentTimeMillis()1:"+System.currentTimeMillis());
                try {
                    this.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                Log.i("test","System.currentTimeMillis()2:"+System.currentTimeMillis());
                if (endTime - startTime >1000){
                    //startTime = endTime;
                    Message message=new Message();
                    message.what=0;
                    handler.sendMessage(message);

                }
            }
        } .start();

        //--動畫end
    }//onCreate
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(b.getInt("check")!=2){
                if(!randomSuggestAct.ActivityA.isFinishing()){
                    randomSuggestAct.ActivityA.finish();
                }
            }else {
                if(!questionSuggestAct.Activityqa.isFinishing()){
                    questionSuggestAct.Activityqa.finish();
                }
            }
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void gotostore(){//送資料到店家頁面
        Bundle b = new Bundle();
        Intent i = new Intent(this, StoreAct.class);
        b.putBoolean("mode", false);//判斷店家頁面用
        b.putString("data", storename);
        //b.putString("datamname",textViewmenu.getText().toString());
        b.putString("datanum", menunum);
        b.putString("datastar", star);
        b.putString("datacell", cell);
        b.putString("dataddr", addr);
        i.putExtras(b);
        startActivity(i);
    }
    public  void openMap(){//google map 路徑
        getGPFromAddress(addr);
        Uri uri;
        if(b.getInt("check")!=2) {
            uri = Uri.parse("http://maps.google.com/maps?f=d&saddr=" + b.getString("Latitude") + "," + b.getString("Longitude") + "&daddr=" + geoLatitude + "," + geoLongitude + "&hl=tw");
        }else{
            uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+b.getString("Latitude2")+","+b.getString("Longitude2")+"&daddr="+geoLatitude+","+geoLongitude+"&hl=tw");
        }
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(uri);
        if (it.resolveActivity(getPackageManager()) != null) {
            startActivity(it);
        }
    }
    public void getGPFromAddress(String addr) {//地址轉經緯
        if (!addr.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            Address address = null;
            try {
                addresses = geocoder.getFromLocationName(addr, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses == null || addresses.isEmpty()) {
                Toast.makeText(this, "找不到該地址", Toast.LENGTH_SHORT).show();
            } else {
                address = addresses.get(0);
                geoLatitude = address.getLatitude();
                geoLongitude = address.getLongitude();
            }
        } else {
            Toast.makeText(this, "未輸入地址", Toast.LENGTH_SHORT).show();
        }
    }
    public void gotoMain2Activity(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,Main2Activity.class);
        startActivity(it);
        this.finish();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        try {
            FileOutputStream out = openFileOutput("eat.txt", MODE_APPEND);
            String s=menunum.toString().trim()+","+num.toString().trim()+","+storename.toString().trim()+","+textViewmenu.getText().toString()+","+price.toString()+",";
            out.write(s.getBytes());
            out.close();

            ebtn.setEnabled(false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }




}