package tw.com.flag.eatwhat;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class StoreAct extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    TextView storename,storeaddr,storecell,mname;
    private TableLayout storeLayout;
    private TableRow[] row;
    private int sp=14,sid;
    String tmp;
    RatingBar rb;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        storename = (TextView)findViewById(R.id.storename);
        storeaddr = (TextView)findViewById(R.id.storeaddr);
        storecell = (TextView)findViewById(R.id.storecell);
        mname = (TextView)findViewById(R.id.mname);
        rb = (RatingBar)findViewById(R.id.store_ratingbar);

        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=12;
        }
        b = this.getIntent().getExtras();
        storename.setText(b.getString("data"));
        mname.setText(b.getString("datamname"));
        storeaddr.setText("地址:"+ b.getString("dataddr"));
        storecell.setText("電話:"+b.getString("datacell"));
        float starmum = Float.valueOf(b.getString("datastar"));
        sid = Integer.parseInt(b.getString("datanum"));
        rb.setRating(starmum);
        try {
            json_write = new JSONObject();
            json_write.put("action", "Store");
            json_write.put("Id", sid);
            globalVariable.c.send(json_write);
            tmp = globalVariable.c.receive();
            json_read = new JSONObject(tmp);
            if(tmp!=null) {
                storeLayout = (TableLayout) findViewById(R.id.storeLayout);
                storeLayout.setColumnShrinkable(0, true);
                storeLayout.setColumnShrinkable(1, true);
                storeLayout.setColumnStretchable(0, true);
                storeLayout.setColumnStretchable(1, true);

                JSONArray j1 = json_read.getJSONArray("Menu");
                JSONArray j2;
                row = new TableRow[j1.length()];
                for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                    row[i] = new TableRow(this);
                    row[i].setId(i);
                    storeLayout.addView(row[i]);
                }
                for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並製作表格顯示
                    j2 = j1.getJSONArray(i);
                    TextView tw = new TextView(this);
                    tw.setText(j2.get(0).toString());
                    tw.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    tw.setTextColor(Color.BLACK);
                    row[i].addView(tw);
                    tw = new TextView(this);
                    tw.setText(j2.get(1).toString());
                    tw.setTextColor(Color.BLACK);
                    row[i].addView(tw);
                }
                Button commit = (Button) findViewById(R.id.commit);
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoCommit();
                    }
                });
            }else{
                Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void gotoCommit(){
        Bundle b = new Bundle();
        Intent i = new Intent(this,Pop.class);
        b.putString("data", tmp);
        b.putInt("sid",sid);
        i.putExtras(b);
        startActivity(i);
    }
}