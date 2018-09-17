package tw.com.flag.eatwhat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.xml.sax.Attributes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class recordAct extends AppCompatActivity
    implements DialogInterface.OnClickListener{
    static Activity ActivityR;
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    private TableLayout tblayout, tblayout2;
    ArrayList<TableRow> row;
    private int sp=14;
    private int count, count2;
    private boolean change=true, change2=false;
    private boolean isDel=false;
    private boolean linkout=false;
    private Button ebtn,btn;
    private TabLayout mTabLayout;
    private Toolbar toolbar;
    private int[] TollBarTitle = {R.string.consider,R.string.eat};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ActivityR=this;
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=10;
        }
        tblayout2 = (TableLayout) findViewById(R.id.tb2Layout);

        try {
            FileInputStream in=openFileInput(globalVariable.account+"think.txt");
            int idx;
            byte[] buffer = new byte[1024];
            String s="";
            idx=in.read(buffer);
            while(idx!=-1){
                s=s+new String(buffer, 0, idx);
                idx=in.read(buffer);
            }
            in.close();

            if(s!="") {
                tblayout = (TableLayout) findViewById(R.id.tbLayout);
                tblayout.setColumnShrinkable(2,true);
                tblayout.setColumnStretchable(2, true);

                count=0;
                row=new ArrayList<>();
                while (s.contains(",")){
                    row.add(new TableRow(this));
                    row.get(count).setBackgroundResource(R.drawable.ripple);
                    String fid,sid,mid;
                    idx=s.indexOf(",");
                    fid = s.substring(0, idx);
                    s=s.substring(idx+1);
                    idx=s.indexOf(",");
                    sid = s.substring(0, idx);
                    s=s.substring(idx+1);
                    idx=s.indexOf(",");
                    mid = s.substring(0, idx);
                    s=s.substring(idx+1);

                    View vv=new View(this);
                    row.get(count).addView(vv);

                    TextView[] tv=new TextView[3];
                    for(int i=0;i<3;i++){
                        idx=s.indexOf(",");
                        tv[i]=new TextView(this);
                        tv[i].setText(s.substring(0, idx));
                        tv[i].setPadding(0,0,16,0);
                        if(i==0){
                            tv[i].setTag(sid);
                            row.get(count).addView(tv[i]);
                        }else if(i==1){
                            tv[i].setTag(mid);
                            final int ii = count;
                            tv[i].setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    TableRow t=(TableRow) row.get(ii);
                                    gotostore(t.getChildAt(1).getTag().toString());
//                                                    String TAG = "123";
//                                                    Log.v(TAG,"123");
                                }
                            });
                            ScrollView sc=new ScrollView(this);

                            sc.addView(tv[i]);
                            sc.setPadding(8,0,8,0);
                            row.get(count).addView(sc);
                            TableRow.LayoutParams params=(TableRow.LayoutParams)sc.getLayoutParams();
                            params.gravity=Gravity.CENTER;
                        }else{
                            tv[i].setGravity(Gravity.CENTER);
                            tv[i].setTag(fid);
                            row.get(count).addView(tv[i]);
                        }
                        s=s.substring(idx+1);
                    }
                    Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn.setText("吃");
                    btn.setBackgroundTintList(getResources().getColorStateList(R.color.waterBlue));
                    btn.setTextColor(Color.WHITE);
                    btn.setTypeface(null, Typeface.BOLD);
                    btn.setId(count);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ebtn=(Button)v;
                            AlertDialog.Builder b=new AlertDialog.Builder(recordAct.this);
                            //串聯呼叫法
                            b.setTitle("確認")
                                    .setMessage("確定要吃這個嗎?")
                                    .setPositiveButton("GO", recordAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    });
                    btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    row.get(count).addView(btn);
                    row.get(count).setTag(count);
                    row.get(count).setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(!isDel) {
                                isDel = true;
                                Button btn = (Button) findViewById(R.id.clearBtn);
                                btn.setVisibility(View.VISIBLE);

                                CheckBox cb;
                                for (int i = 0; i < row.size(); i++) {
                                    row.get(i).removeViewAt(0);

                                    cb = new CheckBox(recordAct.this);
                                    cb.setButtonTintList(getResources().getColorStateList(R.color.recordCheckbox));
                                    row.get(i).addView(cb,0);
                                }
                                return false;
                            }
                            return true;
                        }
                    });
                    row.get(count).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TableRow tr=(TableRow)v;
                            if(isDel){
                                for(int i=0;i<row.size();i++){
                                    if(row.get(i).getTag()==tr.getTag()){
                                        if(((CheckBox)row.get(i).getChildAt(0)).isChecked()){
                                            ((CheckBox)row.get(i).getChildAt(0)).setChecked(false);
                                        }else{
                                            ((CheckBox)row.get(i).getChildAt(0)).setChecked(true);
                                        }
                                        break;
                                    }
                                }
                            }else{
                                gotostore(tr.getChildAt(1).getTag().toString());
                            }
                        }
                    });
                    count++;
                }
                for(int i=row.size()-1;i>-1;i--){
                    tblayout.addView(row.get(i));
                    TableLayout.LayoutParams params=(TableLayout.LayoutParams)row.get(i).getLayoutParams();
                    params.setMargins(0,12,0,12);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTabLayout = findViewById(R.id.mTabLayout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { //考慮or吃
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        toolbar.setTitle(TollBarTitle[0]);
                        NestedScrollView sc = findViewById(R.id.sc1);
                        sc.setVisibility(View.VISIBLE);
                        sc = findViewById(R.id.sc2);
                        sc.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        toolbar.setTitle(TollBarTitle[1]);
                        NestedScrollView sc2 = findViewById(R.id.sc2);
                        sc2.setVisibility(View.VISIBLE);
                        sc2 = findViewById(R.id.sc1);
                        sc2.setVisibility(View.INVISIBLE);
                        if(change){
                            change=false;
                            if(tblayout2!=null) tblayout2.removeAllViews();
                            try {
                                FileInputStream in = openFileInput(globalVariable.account+"eat.txt");
                                byte[] buffer = new byte[1024];
                                String s = "";
                                int idx = in.read(buffer);
                                while (idx != -1) {
                                    s = s + new String(buffer, 0, idx);
                                    idx = in.read(buffer);
                                }
                                in.close();

                                if(s!="") {
                                    tblayout2.setColumnShrinkable(1,true);
                                    tblayout2.setColumnStretchable(1, true);

                                    final ArrayList<TableRow> row2=new ArrayList<>();
                                    count2=0;
                                    while (s.contains(",")){
                                        row2.add(new TableRow(recordAct.this));
                                        row2.get(count2).setBackgroundResource(R.drawable.ripple);
                                        final String sid,mid;
                                        idx=s.indexOf(",");
                                        sid = s.substring(0, idx);
                                        s=s.substring(idx+1);
                                        idx=s.indexOf(",");
                                        mid = s.substring(0, idx);
                                        s=s.substring(idx+1);
                                        TextView[] tv=new TextView[3];
                                        for(int i=0;i<3;i++){
                                            idx=s.indexOf(",");
                                            tv[i]=new TextView(recordAct.this);
                                            tv[i].setText(s.substring(0, idx));
                                            tv[i].setTag(sid);
                                            if(i==0){
                                                tv[i].setTag(sid);
                                                row2.get(count2).addView(tv[i]);
                                            }else if(i==1){
                                                ScrollView sc3=new ScrollView(recordAct.this);
                                                tv[i].setOnClickListener(new View.OnClickListener() {
                                                    public void onClick(View v) {
                                                        gotostore(v.getTag().toString());
//                                                    String TAG = "123";
//                                                    Log.v(TAG,"123");
                                                    }
                                                });
                                                sc3.addView(tv[i]);
                                                sc3.setPadding(8,0,8,0);
                                                row2.get(count2).addView(sc3);
                                                TableRow.LayoutParams params=(TableRow.LayoutParams)sc3.getLayoutParams();
                                                params.gravity=Gravity.CENTER;
                                            }else if(i==2){
                                                tv[i].setGravity(Gravity.CENTER);
                                                row2.get(count2).addView(tv[i]);
                                            }
                                            s=s.substring(idx+1);
                                        }
                                        btn=new Button(recordAct.this, null, android.R.attr.buttonStyleSmall);
                                        btn.setText("推薦");
                                        btn.setTextColor(Color.WHITE);
                                        btn.setTypeface(null, Typeface.BOLD);
                                        btn.setBackgroundTintList(getResources().getColorStateList(R.color.pink));
                                        btn.setId(count2);
                                        btn.setTag(mid);
                                        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                                        btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final Button bt=(Button)v;
                                                if( globalVariable.recmdtime <2) {
                                                    final AlertDialog.Builder b = new AlertDialog.Builder(recordAct.this);
                                                    b.setTitle("確認")
                                                            .setMessage("確定要推薦這道菜嗎?")
                                                            .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    json_write = new JSONObject();
                                                                    try {
                                                                        json_write.put("action", "Recommend");
                                                                        json_write.put("Mid", bt.getTag());
                                                                        globalVariable.c.send(json_write);
                                                                        String tmp = globalVariable.c.receive();
                                                                        bt.setBackgroundTintList(getResources().getColorStateList(R.color.lightPink));
                                                                        if (tmp != null) {
                                                                            json_read = new JSONObject(tmp);
                                                                            String reason = json_read.getString("data");
                                                                            if (!json_read.getBoolean("check")) {//接收失敗原因
                                                                            } else {//成功並關閉
                                                                                bt.setEnabled(false);
                                                                                globalVariable.recmdtime++;
                                                                            }
                                                                            Toast.makeText(recordAct.this, reason, Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            linkout=true;
                                                                            AlertDialog.Builder b=new AlertDialog.Builder(recordAct.this);
                                                                            //串聯呼叫法
                                                                            b.setCancelable(false);
                                                                            b.setTitle("警告")
                                                                                    .setMessage("連線逾時，請重新連線")
                                                                                    .setPositiveButton("連線", recordAct.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                                                                    .setNegativeButton("離開", recordAct.this)
                                                                                    .show();
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });
                                                    b.setNegativeButton("Cancel", null);
                                                    b.show();
                                                }else{
                                                    Toast.makeText(recordAct.this, "本日推薦次數已用完", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        if(tv[1].getText().toString().equals("-")){
                                            btn.setEnabled(false);
                                        }
                                        row2.get(count2).addView(btn);
                                        row2.get(count2).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TableRow tr=(TableRow)v;
                                                gotostore(tr.getChildAt(0).getTag().toString());
                                            }
                                        });
                                        count2++;
                                    }
                                    for(int i=row2.size()-1;i>-1;i--){
                                        tblayout2.addView(row2.get(i));
                                        TableLayout.LayoutParams params=(TableLayout.LayoutParams)row2.get(i).getLayoutParams();
                                        params.setMargins(0,12,0,12);
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
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

    public void clearClick(View v){
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        //串聯呼叫法
        b.setTitle("刪除確認")
                .setMessage("確定要刪除嗎?")
                .setPositiveButton("GO", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                .setNegativeButton("Cancel", null)
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(change2){
                FileOutputStream out = openFileOutput(globalVariable.account+"think.txt", MODE_PRIVATE);
                String s="";
                for(int i=0;i<row.size();i++){
                    s+=row.get(i).getChildAt(3).getTag().toString()+","+row.get(i).getChildAt(1).getTag().toString()+","+ ((ScrollView) row.get(i).getChildAt(2)).getChildAt(0).getTag().toString()+","+((TextView)row.get(i).getChildAt(1)).getText().toString()+","+((TextView) ((ScrollView) row.get(i).getChildAt(2)).getChildAt(0)).getText().toString()+","+((TextView)row.get(i).getChildAt(3)).getText().toString()+",";
                }
                out.write(s.getBytes());
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isDel) {
                isDel = false;
                Button btn=(Button)findViewById(R.id.clearBtn);
                btn.setVisibility(View.INVISIBLE);

                for (int i = 0; i < row.size(); i++) {
                    row.get(i).removeViewAt(0);
                    row.get(i).addView(new View(this),0);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event); //需在最後面
    }
    public void gotostore(String id){
        Bundle b = new Bundle();
        Intent i = new Intent(this, StoreAct.class);
        b.putBoolean("mode", true);
        b.putString("datanum", id);
        b.putInt("Activity", 6);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(!linkout) {
            if (isDel) {
                for (int i = 0; i < row.size(); i++) {
                    if (((CheckBox) row.get(i).getChildAt(0)).isChecked()) {
                        tblayout.removeView(row.get(i));
                        row.remove(i);
                        i -= 1;
                        change2 = true;
                    }
                }
                Button btn = (Button) findViewById(R.id.clearBtn);
                btn.setVisibility(View.INVISIBLE);

                for (int i = 0; i < row.size(); i++) {
                    row.get(i).removeViewAt(0);
                    row.get(i).addView(new View(this), 0);
                }
                isDel = false;
            } else {
                try {
                    int i;
                    for (i = 0; i < row.size(); i++) {
                        if (((Button) row.get(i).getChildAt(4)).getId() == ebtn.getId()) {
                            FileOutputStream out = openFileOutput(globalVariable.account+"eat.txt", MODE_APPEND);
                            String s = row.get(i).getChildAt(1).getTag().toString() + "," + ((ScrollView) row.get(i).getChildAt(2)).getChildAt(0).getTag().toString() + "," + ((TextView) row.get(i).getChildAt(1)).getText().toString() + "," + ((TextView) ((ScrollView) row.get(i).getChildAt(2)).getChildAt(0)).getText().toString() + "," + ((TextView) row.get(i).getChildAt(3)).getText().toString() + ",";
                            out.write(s.getBytes());
                            out.close();

                            if(!((TextView) row.get(i).getChildAt(3)).getText().toString().equals("-")) {
                                json_write = new JSONObject();
                                json_write.put("action", "eat");
                                json_write.put("mid", Integer.parseInt(((ScrollView) row.get(i).getChildAt(2)).getChildAt(0).getTag().toString()));
                                globalVariable.c.send(json_write);
                                String tmp = globalVariable.c.receive();
                                if (tmp != null) {
                                    json_read = new JSONObject(tmp);
                                    if (!json_read.getBoolean("check")) {
                                        String reason;
                                        reason = json_read.getString("data");
                                        Toast.makeText(recordAct.this, reason, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    linkout = true;
                                    AlertDialog.Builder b = new AlertDialog.Builder(recordAct.this);
                                    //串聯呼叫法
                                    b.setCancelable(false);
                                    b.setTitle("警告")
                                            .setMessage("連線逾時，請重新連線")
                                            .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                            .setNegativeButton("離開", this)
                                            .show();
                                }
                            }

                            json_write = new JSONObject();
                            json_write.put("action", "eatLog");
                            json_write.put("Fid", Integer.parseInt(row.get(i).getChildAt(3).getTag().toString()));
                            globalVariable.c.send(json_write);

                            tblayout.removeView(row.get(i));
                            row.remove(i);
                            change = true;
                            change2 = true;
                            break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
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
            if(!Main2Activity.ActivityM.isFinishing()) Main2Activity.ActivityM.finish();
            this.finish();
        }
    }
}
