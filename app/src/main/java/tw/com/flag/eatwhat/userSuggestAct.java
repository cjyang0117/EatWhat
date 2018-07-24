package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
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

public class userSuggestAct extends AppCompatActivity
        implements DialogInterface.OnClickListener{
    private JSONObject json_read, json_write;
    private TableLayout tblayout, tblayout2;
    private TableRow[] row, row2;
    private GlobalVariable globalVariable;
    private int sp=14;
    private Button ebtn;
    private boolean Switch=true;
    private TabLayout mTabLayout,mTabLayout2;
    private Toolbar toolbar;
    private int[] TollBarTitle = {R.string.userSuggest,R.string.followUserSuggest};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggest);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=12;
        }

        row=loadUserData(true, R.id.tbLayout, row);
        row2=loadUserData(false, R.id.tb2Layout, row2);
        mTabLayout = findViewById(R.id.mTabLayout);
        mTabLayout2 = findViewById(R.id.mTabLayout2);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { //用戶推薦or追蹤用戶推薦
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        toolbar.setTitle(TollBarTitle[0]);
                        Switch=true;
                        ScrollView sc = findViewById(R.id.sc1);
                        sc.setVisibility(View.VISIBLE);
                        sc = findViewById(R.id.sc2);
                        sc.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        toolbar.setTitle(TollBarTitle[1]);
                        Switch=false;
                        ScrollView sc2 = findViewById(R.id.sc2);
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
        mTabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { //排序
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0: //價格排序
                        break;
                    case 1: //距離排序
                        break;
                    case 2: //星級排序
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
                tblayout.setColumnShrinkable(1,true);
                tblayout.setColumnShrinkable(2,true);
                tblayout.setColumnStretchable(1, true);
                tblayout.setColumnStretchable(2, true);

                JSONArray j1 = json_read.getJSONArray("data");
                JSONArray j2;
                r = new TableRow[j1.length()];
                for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                    r[i] = new TableRow(this);
                    r[i].setId(i);
                    tblayout.addView(r[i]);
                }

                for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並製作表格顯示
                    j2 = j1.getJSONArray(i);
                    TextView tw = new TextView(this);
                    tw.setText(j2.get(1).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    r[i].addView(tw);
                    tw = new TextView(this);
                    tw.setText(j2.get(3).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    r[i].addView(tw);
                    tw = new TextView(this);
                    tw.setText(j2.get(7).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    r[i].addView(tw);
                    tw = new TextView(this);
                    tw.setText(j2.get(8).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    r[i].addView(tw);

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
                                String s;
                                if(Switch) {
                                    s = ((TextView) row[b.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(2)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(3)).getText().toString() + ",";
                                }else{
                                    s = ((TextView) row2[b.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row2[b.getId()].getChildAt(2)).getText().toString() + "," + ((TextView) row2[b.getId()].getChildAt(3)).getText().toString() + ",";
                                }
                                out.write(s.getBytes());
                                out.close();

                                b.setEnabled(false);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    r[i].addView(btn);
                    btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn.setText("吃");
                    btn.setId(i);
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
                Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        try {
            FileOutputStream out = openFileOutput("eat.txt", MODE_APPEND);
            String s;
            if(Switch) {
                s = ((TextView) row[ebtn.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row[ebtn.getId()].getChildAt(2)).getText().toString() + "," + ((TextView) row[ebtn.getId()].getChildAt(3)).getText().toString() + ",";
            }else {
                s = ((TextView) row2[ebtn.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row2[ebtn.getId()].getChildAt(2)).getText().toString() + "," + ((TextView) row2[ebtn.getId()].getChildAt(3)).getText().toString() + ",";
            }
            out.write(s.getBytes());
            out.close();
            ebtn.setEnabled(false);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
