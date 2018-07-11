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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class randomSuggestRul extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    TextView textViewrul ,textViewaddr, textViewmenu, textViewprice;
    String addr;
    double geoLatitude, geoLongitude;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_suggest_rul);
        textViewrul = (TextView) findViewById(R.id.textViewstore);
        textViewaddr = (TextView)findViewById(R.id.textViewaddr);
        textViewmenu = (TextView)findViewById(R.id.textViewmenu);
        textViewprice = (TextView)findViewById(R.id.textViewprice);
        TextView[] tvmap={textViewrul ,textViewaddr, textViewmenu, textViewprice};
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        b = this.getIntent().getExtras();

        if (b != null) {
            try {
                if(b.getInt("check")!=2) {
                    JSONObject json_read = new JSONObject(b.getString("data"));
                    JSONArray j1 = json_read.getJSONArray("data");
                    JSONArray j2 = new JSONArray();
                    for (int i = 0; i < j1.length(); i++) {
                        j2 = j1.getJSONArray(i);
                    }
                    textViewrul.setText(j2.get(0).toString());//店家名稱
                    textViewmenu.setText(j2.get(2).toString());//菜品
                    textViewprice.setText("價格" + j2.get(3).toString() + "元");//價格
                    addr = j2.get(1).toString().trim();//店家地址
                    textViewaddr.setText(addr);
                }else{
                    textViewrul.setText(b.getString("data1"));//店家名稱
                    textViewmenu.setText(b.getString("data3"));//菜品
                    textViewprice.setText("價格" + b.getString("data4") + "元");//價格
                    addr = b.getString("data2");//店家地址
                    textViewaddr.setText(addr);
                }
                for(int i = 0 ; i <tvmap.length ; i++) {
                    tvmap[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            openMap();
                        }
                    });
                }
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
    public  void openMap(){//google map 路徑
        getGPFromAddress(addr);
        Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+b.getString("Latitude")+","+b.getString("Longitude")+"&daddr="+geoLatitude+","+geoLongitude+"&hl=tw");
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
}