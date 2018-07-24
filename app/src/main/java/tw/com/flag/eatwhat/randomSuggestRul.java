package tw.com.flag.eatwhat;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class randomSuggestRul extends AppCompatActivity {
    private GlobalVariable globalVariable;
    TextView textViewrul ,textViewaddr, textViewmenu, textViewprice;
    String addr;
    double geoLatitude, geoLongitude;
    private TableRow[] row,row2;
    private TableLayout tblayout, tblayout2;
    String cell,star,menunum,storename,mname;
    TableLayout tbrulLayout;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_suggest_rul);
        textViewrul = (TextView) findViewById(R.id.textViewstore);
        textViewaddr = (TextView)findViewById(R.id.textViewaddr);
        textViewmenu = (TextView)findViewById(R.id.textViewmenu);
        textViewprice = (TextView)findViewById(R.id.textViewprice);
        //TextView[] tvmap={textViewrul ,textViewaddr, textViewmenu, textViewprice};
        //TextView[] tw,tw1;
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
                    textViewmenu.setText(j2.get(5).toString().trim());//菜品
                    textViewprice.setText("價格" + j2.get(6).toString() + "元");//價格
                }else{//提問
                    storename = b.getString("data1").toString().trim();//店名
                    menunum =  b.getString("data5").toString().trim();//店號
                    textViewmenu.setText( b.getString("data3").toString().trim());//菜名
                    textViewprice.setText("價格" + b.getString("data4").toString().trim() + "元");//價格
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
        }
    }//onCreate
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
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
    /*
    public  void openMaptw(String storeaddr){//google map 路徑
        getGPFromAddress(storeaddr);
        Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+b.getString("Latitude2")+","+b.getString("Longitude2")+"&daddr="+geoLatitude+","+geoLongitude+"&hl=tw");
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(uri);
        if (it.resolveActivity(getPackageManager()) != null) {
            startActivity(it);
        }
    }
    */
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
}