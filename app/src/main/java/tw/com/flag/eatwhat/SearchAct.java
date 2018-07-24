package tw.com.flag.eatwhat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class SearchAct extends AppCompatActivity
        implements DialogInterface.OnClickListener{
    private JSONObject json_read, json_write;
    private GlobalVariable globalVariable;
    private TableRow[] row,row2;
    private int sp=14;
    private TableLayout tblayout, tblayout2;
    private Boolean isStore=true, isSort=false, sort=true;
    private Button ebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle b=new Bundle();
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=12;
        }
    }
    public void storeMenuClick(View v){
        Button b=(Button)v;
        switch (b.getId()){
            case R.id.button20:
                isStore=true;
                b.setEnabled(false);
                b=findViewById(R.id.button21);
                b.setEnabled(true);
                b=findViewById(R.id.button23);
                b.setEnabled(false);
                ScrollView sc=findViewById(R.id.sc1);
                sc.setVisibility(View.VISIBLE);
                sc=findViewById(R.id.sc2);
                sc.setVisibility(View.INVISIBLE);
                break;
            case R.id.button21:
                isStore=false;
                b.setEnabled(false);
                b=findViewById(R.id.button20);
                b.setEnabled(true);
                b=findViewById(R.id.button23);
                b.setEnabled(true);
                ScrollView sc2=findViewById(R.id.sc2);
                sc2.setVisibility(View.VISIBLE);
                sc2=findViewById(R.id.sc1);
                sc2.setVisibility(View.INVISIBLE);
                break;
        }
    }
    public void orderClick(View v){
        Button b=(Button)v;
        switch (b.getId()){
            case R.id.button23:
                tblayout2.removeAllViews();
                if(!isSort) {
                    isSort = true; sort=true;
                    Sort(row2, 2);
                }
                if (sort) {
                    sort = false;
                    for (int i = 0; i < row2.length; i++) {
                        tblayout2.addView(row2[i]);
                    }
                } else {
                    sort = true;
                    for (int i = row2.length - 1; i > -1; i--) {
                        tblayout2.addView(row2[i]);
                    }
                }
                break;
            case R.id.button24:

                break;
            case R.id.button25:

                break;
        }
    }
    public void Sort(TableRow[] tr, int in) {
        int p1, p2;
        for (int i = tr.length-1; i > 0; --i){
            for (int j = 0; j < i; ++j) {
                p1 = Integer.parseInt(((TextView) tr[j].getChildAt(in)).getText().toString());
                p2 = Integer.parseInt(((TextView) tr[j+1].getChildAt(in)).getText().toString());
                if (p1 > p2) {
                    Button t1=((Button) tr[j].getChildAt(3));
                    Button t2=((Button) tr[j+1].getChildAt(3));
                    int t=t1.getId();
                    t1.setId(t2.getId());
                    t2.setId(t);
                    t1=((Button) tr[j].getChildAt(4));
                    t2=((Button) tr[j+1].getChildAt(4));
                    t=t1.getId();
                    t1.setId(t2.getId());
                    t2.setId(t);

                    TableRow tmp = tr[j];
                    tr[j] = tr[j + 1];
                    tr[j + 1] = tmp;
                }
            }
        }
    }
    public void searchClick(View v){
        EditText ed=(EditText)findViewById(R.id.editText10);
        isSort=false;
        if(isStore){
           try {
               if(tblayout!=null) tblayout.removeAllViews();
               json_write=new JSONObject(); //接收店家資料，並動態產生表格顯示
               json_write.put("action", "show");
               json_write.put("data", ed.getText().toString());
               globalVariable.c.send(json_write);
               String tmp=globalVariable.c.receive();
               if(tmp!=null) {
                   json_read = new JSONObject(tmp);
                   tblayout = (TableLayout) findViewById(R.id.tbLayout);
                   tblayout.setColumnShrinkable(0,true);
                   tblayout.setColumnStretchable(0, true);
                   tblayout.setColumnStretchable(3, true);
                   tblayout.setColumnStretchable(4, true);

                   JSONArray j1 = json_read.getJSONArray("data");
                   JSONArray j2;
                   row = new TableRow[j1.length()];
                   for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                       row[i] = new TableRow(this);
                       row[i].setId(i);
                       tblayout.addView(row[i]);
                   }
                   for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並製作表格顯示
                       j2 = j1.getJSONArray(i);
                       TextView tw = new TextView(this);
                       tw.setText(j2.get(1).toString());
                       tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       tw.setTag(j2.get(0).toString());
                       tw.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               TextView t=(TextView)v;
                               gotostore(t.getTag().toString());
                           }
                       });
                       float ratecount;
                       //ratecount = Float.valueOf(j2.get(2).toString());
                       row[i].addView(tw);
                       RatingBar rb=new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
                       rb.setNumStars(5);
                       //rb.setRating(ratecount);
                       rb.setRating(2);
                       //rb.setIsIndicator(true);
                       row[i].addView(rb);
                       TableRow.LayoutParams tlp=(TableRow.LayoutParams) rb.getLayoutParams();
                       tlp.gravity=Gravity.CENTER_VERTICAL;
                       tw = new TextView(this);
                       tw.setText("0.3km");
                       tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       row[i].addView(tw);
                       Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("考慮");
                       btn.setId(i);
                       btn.setTag(j2.get(0).toString()+",-,");
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setSingleLine();
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Button b=(Button)v;
                               try {
                                   FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                                   String s=b.getTag().toString()+((TextView)row[b.getId()].getChildAt(0)).getText().toString()+",-,-,";
                                   out.write(s.getBytes());
                                   out.close();

                                   b.setEnabled(false);
                               }catch (IOException e){
                                   e.printStackTrace();
                               }
                           }
                       });
                       row[i].addView(btn);
                       btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("吃");
                       btn.setId(i);
                       btn.setTag(j2.get(0).toString()+",-,");
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               ebtn=(Button)v;
                               AlertDialog.Builder b=new AlertDialog.Builder(SearchAct.this);
                               //串聯呼叫法
                               b.setTitle("確認")
                                       .setMessage("確定要吃這個嗎?")
                                       .setPositiveButton("GO", SearchAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                       .setNegativeButton("Cancel", null)
                                       .show();
                           }
                       });
                       row[i].addView(btn);
                   }
               }else{
                   Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
               }
           }catch (Exception e){
               e.printStackTrace();
               Log.e("Exception","StoreError="+e.toString());
           }

        }else{
           try {
               if(tblayout2!=null) tblayout2.removeAllViews();
               json_write=new JSONObject();                                //接收店家資料，並動態產生表格顯示
               json_write.put("action", "show2");
               json_write.put("data", ed.getText().toString());
               globalVariable.c.send(json_write);
               String tmp=globalVariable.c.receive();
               if(tmp!=null) {
                   json_read = new JSONObject(tmp);
                   tblayout2 = (TableLayout) findViewById(R.id.tb2Layout);
                   tblayout2.setColumnShrinkable(0,true);
                   tblayout2.setColumnShrinkable(1,true);
                   tblayout2.setColumnStretchable(0, true);
                   tblayout2.setColumnStretchable(1, true);
                   tblayout2.setColumnStretchable(3, true);
                   tblayout2.setColumnStretchable(4, true);

                   JSONArray j1 = json_read.getJSONArray("data");
                   JSONArray j2;
                   row2 = new TableRow[j1.length()];
                   for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                       row2[i] = new TableRow(this);
                       row2[i].setId(i);
                       tblayout2.addView(row2[i]);
                   }
                   for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並製作表格顯示
                       j2 = j1.getJSONArray(i);
                       TextView[] tw = new TextView[j2.length()];
                       for(int j=0;j<j2.length()-2;j++){
                           tw[j] = new TextView(this);
                           tw[j].setText(j2.get(j+2).toString());
                           tw[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                           row2[i].addView(tw[j]);
                           if(j==0){
                               tw[j].setTag(j2.get(0).toString());
                               tw[j].setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       TextView t=(TextView)v;
                                       gotostore(t.getTag().toString());
                                   }
                               });
                           }
                       }
                       Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("考慮");
                       btn.setId(i);
                       btn.setTag(j2.get(0).toString()+","+j2.get(1).toString()+",");
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Button b=(Button)v;
                               try {
                                   FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                                   String s=b.getTag().toString()+((TextView)row2[b.getId()].getChildAt(0)).getText().toString()+","+((TextView)row2[b.getId()].getChildAt(1)).getText().toString()+","+((TextView)row2[b.getId()].getChildAt(2)).getText().toString()+",";
                                   out.write(s.getBytes());
                                   out.close();

                                   b.setEnabled(false);
                               }catch (IOException e){
                                   e.printStackTrace();
                               }
                           }
                       });
                       row2[i].addView(btn);
                       btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("吃");
                       btn.setId(i);
                       btn.setTag(j2.get(0).toString()+","+j2.get(1).toString()+",");
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               ebtn=(Button)v;
                               AlertDialog.Builder b=new AlertDialog.Builder(SearchAct.this);
                               //串聯呼叫法
                               b.setTitle("確認")
                                       .setMessage("確定要吃這個嗎?")
                                       .setPositiveButton("GO", SearchAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                       .setNegativeButton("Cancel", null)
                                       .show();
                           }
                       });
                       row2[i].addView(btn);
                   }
               }else{
                   Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
               }
           }catch (Exception e){
               e.printStackTrace();
               Log.e("Exception","MenuError="+e.toString());
           }
        }
    }
    public void gotoRandomSuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,randomSuggestAct.class);
        startActivity(it);
    }
    public void gotoQuestionSuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,questionSuggestAct.class);
        startActivity(it);
    }
    public void gotoRecordAct(View v){
        android.content.Intent it = new android.content.Intent(this,recordAct.class);
        startActivity(it);
    }
    public void gotoSearchAct(View v){
        android.content.Intent it = new android.content.Intent(this,SearchAct.class);
        startActivity(it);
    }
    public void gotoMain2Activity(View v){
        android.content.Intent it = new android.content.Intent(this,Main2Activity.class);
        startActivity(it);
    }
    public void gotostore(String id){
        Bundle b = new Bundle();
        Intent i = new Intent(this, StoreAct.class);
        b.putBoolean("mode", true);
        b.putString("datanum", id);
        i.putExtras(b);
        startActivity(i);
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        try {
            FileOutputStream out = openFileOutput("eat.txt", MODE_APPEND);
            String s;
            if(isStore) {
                s=ebtn.getTag().toString()+((TextView) row[ebtn.getId()].getChildAt(0)).getText().toString() + ",-,-,";
            }else {
                s=ebtn.getTag().toString()+((TextView) row2[ebtn.getId()].getChildAt(0)).getText().toString() + "," + ((TextView) row2[ebtn.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row2[ebtn.getId()].getChildAt(2)).getText().toString() + ",";
            }
            out.write(s.getBytes());
            out.close();
            ebtn.setEnabled(false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
