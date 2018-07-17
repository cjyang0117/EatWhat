package tw.com.flag.eatwhat;


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

public class SearchAct extends AppCompatActivity {
    private JSONObject json_read, json_write;
    private GlobalVariable globalVariable;
    private TableRow[] row,row2;
    private int sp=14;
    private TableLayout tblayout, tblayout2;
    private Boolean isStore=true, isSort=false, sort=true;
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
                       tw.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                           }
                       });
                       float ratecount;
                       ratecount = Float.valueOf(j2.get(2).toString());
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
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setSingleLine();
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Button b=(Button)v;
                               try {
                                   FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                                   String s=((TextView)row[b.getId()].getChildAt(0)).getText().toString()+",-,-,";
                                   out.write(s.getBytes());
                                   out.close();
                               }catch (IOException e){
                                   e.printStackTrace();
                               }
                           }
                       });
                       row[i].addView(btn);
                       btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("吃");
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

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
                       for(int j=0;j<j2.length()-1;j++){
                           tw[j] = new TextView(this);
                           tw[j].setText(j2.get(j+1).toString());
                           tw[j].setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                           row2[i].addView(tw[j]);
                       }
                       Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("考慮");
                       btn.setId(i);
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Button b=(Button)v;
                               try {
                                   FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                                   String s=((TextView)row2[b.getId()].getChildAt(0)).getText().toString()+","+((TextView)row2[b.getId()].getChildAt(1)).getText().toString()+","+((TextView)row2[b.getId()].getChildAt(2)).getText().toString()+",";
                                   out.write(s.getBytes());
                                   out.close();
                               }catch (IOException e){
                                   e.printStackTrace();
                               }
                           }
                       });
                       row2[i].addView(btn);
                       btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                       btn.setText("吃");
                       btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                       btn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

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
}
