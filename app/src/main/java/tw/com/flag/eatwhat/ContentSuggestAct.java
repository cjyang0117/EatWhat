package tw.com.flag.eatwhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class ContentSuggestAct extends AppCompatActivity
        implements DialogInterface.OnClickListener {
    static Activity ActivityC;
    private JSONObject json_read, json_write;
    private JSONArray j1;
    private TableLayout tblayout;
    private TableRow[] row;
    private GlobalVariable globalVariable;
    private int sp = 14 ,num,idx,tal;
    private Button ebtn;
    private boolean linkout=false;
    private boolean Switch = true;
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_suggest);
        ActivityC=this;
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        next = (Button)findViewById(R.id.buttondown);
        num=1;
        idx=0;
        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels <= 480) {
            sp = 12;
        }
        mTabLayout = findViewById(R.id.mTabLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            json_write = new JSONObject(); //接收店家資料，並動態產生表格顯示
            json_write.put("action", "Content");
            json_write.put("idx", "0");
            globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            if (tmp != null) {
                json_read = new JSONObject(tmp);
                tblayout = (TableLayout) findViewById(R.id.tbLayout);
                tblayout.setColumnShrinkable(0, true);
                tblayout.setColumnShrinkable(1, true);
                tblayout.setColumnStretchable(0, true);
                tblayout.setColumnStretchable(1, true);
                if (!json_read.getBoolean("check")) {//當回傳為false
                    String reason;
                    reason = json_read.getString("data");
                    Toast.makeText(ContentSuggestAct.this,reason, Toast.LENGTH_SHORT).show();
                }else{
                    j1 = json_read.getJSONArray("data");
                    if(tal%5==0){
                        tal=j1.length()/5;
                    }else {
                        tal=(j1.length()/5)+1;
                    }
                    info(num);
                }
            } else {
                //Toast.makeText(ContentSuggestAct.this, "連線逾時", Toast.LENGTH_LONG).show();
                linkout=true;
                AlertDialog.Builder b=new AlertDialog.Builder(this);
                //串聯呼叫法
                b.setCancelable(false);
                b.setTitle("警告")
                        .setMessage("連線逾時，請重新連線")
                        .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                        .setNegativeButton("離開", this)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "ContentError=" + e.toString());
        }
    }
    public void info(int pagenum){
        try {
            JSONArray j2;
            row = new TableRow[5];
            for (int i = 0; i < 5; i++) { //動態產生TableRow
                row[i] = new TableRow(ContentSuggestAct.this);
                row[i].setBackgroundResource(R.drawable.ripple);
                row[i].setId(i);
                tblayout.addView(row[i]);
            }
            for (int i = (pagenum-1)*5; i < pagenum*5; i++) { //拆解接收的JSON包並製作表格顯示
                j2 = j1.getJSONArray(i);
                row[i%5].setTag(j2.get(0).toString());
                row[i%5].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TableRow t=(TableRow) v;
                        gotostore(t.getTag().toString());
                    }
                });
                TextView tw = new TextView(ContentSuggestAct.this);
                tw.setText(j2.get(1).toString());
                tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                row[i%5].addView(tw);
                tw = new TextView(ContentSuggestAct.this);
                tw.setText(j2.get(6).toString());
                tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                row[i%5].addView(tw);
                tw = new TextView(ContentSuggestAct.this);
                tw.setText(j2.get(7).toString());
                tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                row[i%5].addView(tw);
                Button btn = new Button(ContentSuggestAct.this, null, android.R.attr.buttonStyleSmall);
                btn.setText("考慮");
                btn.setTypeface(null, Typeface.BOLD);
                btn.setBackgroundTintList(getResources().getColorStateList(R.color.pink));
                btn.setId(i);
                btn.setTag(j2.get(0).toString() + "," + j2.get(5).toString() + ",");
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        try {
                            FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                            String s = b.getTag().toString() + ((TextView) row[b.getId()].getChildAt(0)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(2)).getText().toString() + ",";
                            out.write(s.getBytes());
                            out.close();

                            b.setBackgroundTintList(getResources().getColorStateList(R.color.lightPink));
                            b.setEnabled(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                row[i%5].addView(btn);
                btn = new Button(ContentSuggestAct.this, null, android.R.attr.buttonStyleSmall);
                btn.setText("吃");
                btn.setBackgroundTintList(getResources().getColorStateList(R.color.waterBlue));
                btn.setTypeface(null, Typeface.BOLD);
                btn.setId(i);
                btn.setTag(j2.get(0).toString() + "," + j2.get(5).toString() + ",");
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ebtn = (Button) v;
                        AlertDialog.Builder b = new AlertDialog.Builder(ContentSuggestAct.this);
                        //串聯呼叫法
                        b.setTitle("確認")
                                .setMessage("確定要吃這個嗎?")
                                .setPositiveButton("GO", ContentSuggestAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                });
                row[i%5].addView(btn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "ContentError=" + e.toString());
        }
    }

    public void prepage(View view) {
        if(num!=1) {
            tblayout.removeAllViews();
            num--;
            info(num);
        }else{
            Toast.makeText(ContentSuggestAct.this, "第一頁了", Toast.LENGTH_LONG).show();
        }
    }
    public void nextpage(View view) {
        if(num*5<globalVariable.cnum) {
            tblayout.removeAllViews();
            num++;
            if (num % 10 == 1 && num >tal) {
                idx++;
                try {
                    json_write = new JSONObject(); //接收店家資料，並動態產生表格顯示
                    json_write.put("action", "Content");
                    json_write.put("idx", idx * 50);
                    globalVariable.c.send(json_write);
                    String tmp = globalVariable.c.receive();
                    if (tmp != null) {
                        json_read = new JSONObject(tmp);
                        if (!json_read.getBoolean("check")) {//當回傳為false
                            String reason;
                            reason = json_read.getString("data");
                            Toast.makeText(ContentSuggestAct.this, reason, Toast.LENGTH_SHORT).show();
                        } else {
                            JSONArray j3 = json_read.getJSONArray("data");
                            JSONArray j4;
                            for (int i = 0; i < j3.length(); i++) { //合併json封包
                                j4 = j3.getJSONArray(i);
                                j1.put(j4);
                            }
                            if(tal%5==0){
                                tal=j1.length()/5;
                            }else {
                                tal=(j1.length()/5)+1;
                            }
                            info(num);
                        }
                    } else {
                        //Toast.makeText(ContentSuggestAct.this, "連線逾時", Toast.LENGTH_LONG).show();
                        linkout=true;
                        AlertDialog.Builder b=new AlertDialog.Builder(this);
                        //串聯呼叫法
                        b.setCancelable(false);
                        b.setTitle("警告")
                                .setMessage("連線逾時，請重新連線")
                                .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                .setNegativeButton("離開", this)
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exception", "ContentError=" + e.toString());
                }
            } else {
                info(num);
            }
        }else{
            Toast.makeText(ContentSuggestAct.this, "已無資料", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(!linkout) {
            try {
                FileOutputStream out = openFileOutput("eat.txt", MODE_APPEND);
                String s;
                s = ebtn.getTag().toString() + ((TextView) row[ebtn.getId()].getChildAt(0)).getText().toString() + "," + ((TextView) row[ebtn.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row[ebtn.getId()].getChildAt(2)).getText().toString() + ",";
                out.write(s.getBytes());
                out.close();
                ebtn.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            if(which==DialogInterface.BUTTON_POSITIVE) {
                Intent it = new android.content.Intent(this, MainActivity.class);
                startActivity(it);
            }
            if(!Main2Activity.ActivityM.isFinishing()) Main2Activity.ActivityM.finish();
            this.finish();
        }
    }
    public void gotostore(String id){
        Bundle b = new Bundle();
        Intent i = new Intent(this, StoreAct.class);
        b.putBoolean("mode", true);
        b.putString("datanum", id);
        i.putExtras(b);
        startActivity(i);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
