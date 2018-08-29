package tw.com.flag.eatwhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

import static android.view.Gravity.CENTER;

public class userSuggestAct extends AppCompatActivity
        implements DialogInterface.OnClickListener{
    static Activity ActivityU;
    private JSONObject json_read, json_write;
    private TableLayout tblayout;
    private TableRow[] row, row2;
    private GlobalVariable globalVariable;
    private int sp=14;
    private Button ebtn;
    private boolean Switch=true,track = true;
    private Boolean isSort=false, sort=false;
    private boolean linkout=false;
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private int[] TollBarTitle = {R.string.userSuggest,R.string._followUserSuggest};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggest);
        ActivityU=this;
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=12;
        }

        row=loadUserData(true, R.id.tbLayout, row);
        if(row!=null) {
            row2 = loadUserData(false, R.id.tb2Layout, row2);
            mTabLayout = findViewById(R.id.mTabLayout);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { //用戶推薦or追蹤用戶推薦
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            isSort = false; sort=false;
                            toolbar.setTitle(TollBarTitle[0]);
                            Switch = true;
                            NestedScrollView sc = findViewById(R.id.sc1);
                            sc.setVisibility(View.VISIBLE);
                            sc = findViewById(R.id.sc2);
                            sc.setVisibility(View.INVISIBLE);
                            break;
                        case 1:
                            isSort = false; sort=false;
                            toolbar.setTitle(TollBarTitle[1]);
                            Switch = false;
                            NestedScrollView sc2 = findViewById(R.id.sc2);
                            sc2.setVisibility(View.VISIBLE);
                            sc2 = findViewById(R.id.sc1);
                            sc2.setVisibility(View.INVISIBLE);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }


    }
    public void onRadioButtonClicked(View v){
        int id= v.getId();
        switch (id){
            case R.id.radioButton10:
                if(Switch){
                    tblayout = (TableLayout) findViewById(R.id.tbLayout);
                    tblayout.removeAllViews();
                    if(!isSort) {
                        isSort = true; sort=true;
                        Sort(row, 3);
                    }
                    if (sort) {
                        sort = false;
                        for (int i = 0; i < row.length; i++) {
                            tblayout.addView(row[i]);
                        }
                    } else {
                        sort = true;
                        for (int i = row.length - 1; i > -1; i--) {
                            tblayout.addView(row[i]);
                        }
                    }
                }else{
                    tblayout = (TableLayout) findViewById(R.id.tb2Layout);
                    tblayout.removeAllViews();
                    if(!isSort) {
                        isSort = true; sort=true;
                        Sort(row2, 3);
                    }
                    if (sort) {
                        sort = false;
                        for (int i = 0; i < row2.length; i++) {
                            tblayout.addView(row2[i]);
                        }
                    } else {
                        sort = true;
                        for (int i = row2.length - 1; i > -1; i--) {
                            tblayout.addView(row2[i]);
                        }
                    }
                }
                break;
            case R.id.radioButton8:
                break;
            case R.id.radioButton9:
                break;
        }
    }
    public void Sort(TableRow[] tr, int in) {
        int p1 , p2 ;
        for (int i = tr.length-1; i > 0; --i){
            for (int j = 0; j < i; ++j) {
                p1 = Integer.parseInt(((TextView) tr[j].getChildAt(in)).getText().toString().trim());
                p2 = Integer.parseInt(((TextView) tr[j+1].getChildAt(in)).getText().toString().trim());
                if (p1 > p2) {
                    Button t1=((Button) tr[j].getChildAt(4));
                    Button t2=((Button) tr[j+1].getChildAt(4));
                    int t=t1.getId();
                    t1.setId(t2.getId());
                    t2.setId(t);
                    t1=((Button) tr[j].getChildAt(5));
                    t2=((Button) tr[j+1].getChildAt(5));
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
    private TableRow[] loadUserData(boolean isUser, int tbId, TableRow[] r){
        try {
            json_write=new JSONObject(); //接收店家資料，並動態產生表格顯示
            json_write.put("action", "User");
            json_write.put("isUser", isUser);
            globalVariable.c.send(json_write);
            String tmp=globalVariable.c.receive();
            if(tmp!=null) {
                json_read = new JSONObject(tmp);
                tblayout = (TableLayout) findViewById(tbId);
                tblayout.setColumnShrinkable(2,true);
                tblayout.setColumnStretchable(2, true);

                if(tblayout!=null) tblayout.removeAllViews();
                JSONArray j1 = json_read.getJSONArray("data");
                JSONArray j2;
                r = new TableRow[j1.length()];
                for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                    r[i] = new TableRow(this);
                    r[i].setBackgroundResource(R.drawable.ripple);
                    r[i].setId(i);
                    tblayout.addView(r[i]);
                    TableLayout.LayoutParams params=(TableLayout.LayoutParams)r[i].getLayoutParams();
                    params.setMargins(0,12,0,12);
                }

                for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並製作表格顯示
                    j2 = j1.getJSONArray(i);
                    r[i].setTag(j2.get(2).toString());
                    r[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TableRow t=(TableRow) v;
                            gotostore(t.getTag().toString());
                        }
                    });
                    TextView tw = new TextView(this);
                    tw.setText(j2.get(1).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    tw.setTag(j2.get(0).toString());
                    tw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView t =(TextView)v;
                            commitrate(t.getText().toString(),Integer.parseInt(t.getTag().toString()));
                        }
                    });
                    tw.setBackgroundColor( getResources().getColor(R.color.user));
                    tw.setPadding(0,8,0,8);
                    tw.setGravity(Gravity.CENTER);
                    r[i].addView(tw);

                    tw = new TextView(this);
                    tw.setText(j2.get(3).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    tw.setPadding(8,0,8,0);
                    r[i].addView(tw);

                    tw = new TextView(this);
                    tw.setText(j2.get(8).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    ScrollView sc=new ScrollView(this);
                    sc.addView(tw);
                    sc.setPadding(0,0,8,0);
                    r[i].addView(sc);
                    TableRow.LayoutParams params=(TableRow.LayoutParams)sc.getLayoutParams();
                    params.gravity=Gravity.CENTER;

                    tw = new TextView(this);
                    String s = "  "+j2.get(9).toString()+"  ";
                    tw.setText(s);
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    r[i].addView(tw);

                    Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn.setText("考慮");
                    btn.setTextColor(Color.WHITE);
                    btn.setTypeface(null, Typeface.BOLD);
                    btn.setBackgroundTintList(getResources().getColorStateList(R.color.pink));
                    btn.setId(i);
                    btn.setTag(j2.get(2).toString()+","+j2.get(7).toString()+",");
                    btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b=(Button)v;
                            try {
                                FileOutputStream out = openFileOutput("think.txt", MODE_APPEND);
                                String s;
                                if(Switch) {
                                    s = b.getTag().toString()+((TextView) row[b.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) ((ScrollView) row[b.getId()].getChildAt(2)).getChildAt(0)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(3)).getText().toString() + ",";
                                }else{
                                    s = b.getTag().toString()+((TextView) row2[b.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) ((ScrollView) row2[b.getId()].getChildAt(2)).getChildAt(0)).getText().toString() + "," + ((TextView) row2[b.getId()].getChildAt(3)).getText().toString() + ",";
                                }
                                out.write(s.getBytes());
                                out.close();

                                b.setBackgroundTintList(getResources().getColorStateList(R.color.lightPink));
                                b.setEnabled(false);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    r[i].addView(btn);

                    btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn.setText("吃");
                    btn.setBackgroundTintList(getResources().getColorStateList(R.color.waterBlue));
                    btn.setTextColor(Color.WHITE);
                    btn.setTypeface(null, Typeface.BOLD);
                    btn.setId(i);
                    btn.setTag(j2.get(2).toString()+","+j2.get(7).toString()+",");
                    btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ebtn=(Button)v;
                            AlertDialog.Builder b=new AlertDialog.Builder(userSuggestAct.this);
                            //串聯呼叫法
                            b.setTitle("確認")
                                    .setMessage("確定要吃這個嗎?")
                                    .setPositiveButton("GO", userSuggestAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    });
                    r[i].addView(btn);
                }
                return r;
            }else{
                //Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
                linkout=true;
                AlertDialog.Builder b=new AlertDialog.Builder(this);
                //串聯呼叫法
                b.setCancelable(false);
                b.setTitle("警告")
                        .setMessage("連線逾時，請重新連線")
                        .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                        .setNegativeButton("離開", this)
                        .show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(!linkout) {
            try {
                FileOutputStream out = openFileOutput("eat.txt", MODE_APPEND);
                String s;
                if (Switch) {
                    s = ebtn.getTag().toString() + ((TextView) row[ebtn.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) ((ScrollView) row[ebtn.getId()].getChildAt(2)).getChildAt(0)).getText().toString() + "," + ((TextView) row[ebtn.getId()].getChildAt(3)).getText().toString() + ",";
                } else {
                    s = ebtn.getTag().toString() + ((TextView) row2[ebtn.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) ((ScrollView) row2[ebtn.getId()].getChildAt(2)).getChildAt(0)).getText().toString() + "," + ((TextView) row2[ebtn.getId()].getChildAt(3)).getText().toString() + ",";
                }
                String[] ad = ebtn.getTag().toString().split(",");
                json_write = new JSONObject();
                json_write.put("action", "eat");
                json_write.put("mid", Integer.parseInt(ad[1]));
                globalVariable.c.send(json_write);
                String tmp = globalVariable.c.receive();
                if(tmp != null) {
                    json_read = new JSONObject(tmp);
                    if (!json_read.getBoolean("check")) {
                        String reason;
                        reason = json_read.getString("data");
                        Toast.makeText(userSuggestAct.this, reason, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    linkout=true;
                    AlertDialog.Builder b=new AlertDialog.Builder(userSuggestAct.this);
                    //串聯呼叫法
                    b.setCancelable(false);
                    b.setTitle("警告")
                            .setMessage("連線逾時，請重新連線")
                            .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                            .setNegativeButton("離開", this)
                            .show();
                }
                out.write(s.getBytes());
                out.close();
                ebtn.setEnabled(false);
            }catch (Exception e){
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
    public void commitrate(String name ,int t_uid){
        final Dialog rankDialog;
        rankDialog = new Dialog(userSuggestAct.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(rankDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        rankDialog.getWindow().setAttributes(lp);
        rankDialog.setCancelable(true);

        TextView pic = (TextView)rankDialog.findViewById(R.id.rank_dialog_text1);
        TextView username = (TextView)rankDialog.findViewById(R.id.rank_dialog_text2);
        username.setText(name);
        final Button okButton = (Button) rankDialog.findViewById(R.id.rank_dialog_ok);
        try {
            json_write = new JSONObject();
            json_write.put("action", "isTrack");
            json_write.put("Id", t_uid);
            globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            if(tmp!=null) {
                json_read = new JSONObject(tmp);
                okButton.setTag(t_uid);
                if (json_read.getBoolean("check")) {
                    okButton.setText("取消追蹤");
                    track = false;
                } else {
                    okButton.setText("追蹤");
                    track = true;
                }
                rankDialog.show();
            }else{
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
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Button bt = (Button)v;
                        json_write = new JSONObject();
                        json_write.put("action", "Track");
                        if(track){
                            track = false;
                            json_write.put("isTrack", false);
                        }else{
                            track = true;
                            json_write.put("isTrack", true);
                        }
                        json_write.put("Id", Integer.parseInt(bt.getTag().toString()));
                        globalVariable.c.send(json_write);
                        String tmp = globalVariable.c.receive();
                        if(tmp!=null) {
                            json_read = new JSONObject(tmp);
                            if (json_read.getBoolean("check")) {
                                if (bt.getText().equals("追蹤")) {
                                    bt.setText("取消追蹤");
                                } else {
                                    bt.setText("追蹤");
                                }
                                row2 = loadUserData(false, R.id.tb2Layout, row2);
                            } else {
                                Toast.makeText(userSuggestAct.this, "操作失敗", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            linkout=true;
                            AlertDialog.Builder b=new AlertDialog.Builder(userSuggestAct.this);
                            //串聯呼叫法
                            b.setCancelable(false);
                            b.setTitle("警告")
                                    .setMessage("連線逾時，請重新連線")
                                    .setPositiveButton("連線", userSuggestAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                    .setNegativeButton("離開", userSuggestAct.this)
                                    .show();
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e) {
            e.printStackTrace();
        }
        //rankDialog.show();
    }
    public void gotostore(String id){
        Bundle b = new Bundle();
        Intent i = new Intent(this, StoreAct.class);
        b.putBoolean("mode", true);
        b.putString("datanum", id);
        b.putInt("Activity", 3);
        i.putExtras(b);
        startActivity(i);
    }
}
