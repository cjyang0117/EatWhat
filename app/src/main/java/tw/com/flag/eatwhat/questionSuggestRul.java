package tw.com.flag.eatwhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class questionSuggestRul extends AppCompatActivity implements DialogInterface.OnClickListener{
        static Activity ActivityQ;
        private GlobalVariable globalVariable;
        TextView textViewrul ,textViewaddr, textViewmenu, textViewprice;
        String addr;
        double geoLatitude, geoLongitude;
        String cell,star,menunum,storename,mname,num,price;
        private JSONObject json_write,json_read;
        TableLayout tbrulLayout;
        private boolean linkout=false;
        Button ebtn;
        Bundle b;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityQ=this;
            setContentView(R.layout.activity_question_suggest_rul);
            textViewrul = (TextView) findViewById(R.id.textViewstore);
            textViewaddr = (TextView)findViewById(R.id.textViewaddr);
            textViewmenu = (TextView)findViewById(R.id.textViewmenu);
            textViewprice = (TextView)findViewById(R.id.textViewprice);
            globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

            b = this.getIntent().getExtras();

            if (b != null) {//隨機
                try {
                    //提問
                    storename = b.getString("data1").toString().trim();//店名
                    num = b.getString("data8").toString().trim();//菜號
                    price =b.getString("data4").toString().trim();
                    menunum =  b.getString("data5").toString().trim();//店號
                    textViewmenu.setText( b.getString("data3").toString().trim());//菜名
                    textViewprice.setText("價格" + price + "元");//價格
                    addr =  b.getString("data2").toString().trim();//地址
                    cell = b.getString("data6").toString().trim();//電話
                    star =  b.getString("data7").toString().trim();//星數

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
                            FileOutputStream out = openFileOutput(globalVariable.account+"think.txt", MODE_APPEND);
                            String s="2,"+menunum.toString().trim()+","+num.toString().trim()+","+storename.toString().trim()+","+textViewmenu.getText().toString()+","+price.toString()+",";
                            out.write(s.getBytes());
                            out.close();
                            b.setEnabled(false);
                            b.setBackgroundResource(R.drawable.question_rul_button_false_2);
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
                        AlertDialog.Builder b=new AlertDialog.Builder(questionSuggestRul.this);
                        //串聯呼叫法
                        b.setTitle("確認")
                                .setMessage("確定要吃這個嗎?")
                                .setPositiveButton("GO", questionSuggestRul.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                });
                Button again = (Button)findViewById(R.id.again);
                again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionSuggestRul.this.finish();
                    }
                });
            }
        }//onCreate
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
        {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                if(!questionSuggestAct.Activityqa.isFinishing()){
                    questionSuggestAct.Activityqa.finish();
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
            b.putInt("Activity", 2);
            i.putExtras(b);
            startActivity(i);
        }
        public  void openMap(){//google map 路徑
            getGPFromAddress(addr);
            Uri uri;
            uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+b.getString("Latitude2")+","+b.getString("Longitude2")+"&daddr="+geoLatitude+","+geoLongitude+"&hl=tw");
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
            if(!linkout) {
                try {
                    FileOutputStream out = openFileOutput(globalVariable.account+"eat.txt", MODE_APPEND);
                    String s = menunum.toString().trim() + "," + num.toString().trim() + "," + storename.toString().trim() + "," + textViewmenu.getText().toString() + "," + price.toString() + ",";
                    out.write(s.getBytes());
                    out.close();

                    json_write = new JSONObject();
                    json_write.put("action", "eat");
                    json_write.put("mid", Integer.parseInt(num.toString().trim()));
                    globalVariable.c.send(json_write);
                    String tmp = globalVariable.c.receive();
                    if (tmp != null) {
                        json_read = new JSONObject(tmp);
                        if (!json_read.getBoolean("check")) {
                            String reason;
                            reason = json_read.getString("data");
                            Toast.makeText(questionSuggestRul.this, reason, Toast.LENGTH_SHORT).show();
                        }else{
                            json_write = new JSONObject();
                            json_write.put("action", "eatLog");
                            json_write.put("Fid", 2);
                            globalVariable.c.send(json_write);
                        }
                    } else {
                        linkout = true;
                        AlertDialog.Builder b = new AlertDialog.Builder(questionSuggestRul.this);
                        //串聯呼叫法
                        b.setCancelable(false);
                        b.setTitle("警告")
                                .setMessage("連線逾時，請重新連線")
                                .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                .setNegativeButton("離開", this)
                                .show();
                    }
                    ebtn.setEnabled(false);
                    ebtn.setBackgroundResource(R.drawable.question_rul_button_false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    globalVariable.c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(which==DialogInterface.BUTTON_POSITIVE) {
                    Intent it = new android.content.Intent(this, MainActivity.class);
                    startActivity(it);
                }
                if(!questionSuggestAct.Activityqa.isFinishing()) questionSuggestAct.Activityqa.finish();
                if(!questionSuggestAct2.ActivityQ2.isFinishing()) questionSuggestAct2.ActivityQ2.finish();
                if(!Main2Activity.ActivityM.isFinishing()) Main2Activity.ActivityM.finish();
                this.finish();
            }
        }
    }