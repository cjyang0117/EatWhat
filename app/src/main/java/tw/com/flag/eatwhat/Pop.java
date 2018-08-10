package tw.com.flag.eatwhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
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

import static android.graphics.Color.WHITE;

public class Pop extends Activity
        implements DialogInterface.OnClickListener{
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    private TableLayout storecommit;
    private TableRow[] row;
    private EditText ed1;
    private Button submit;
    private boolean check;
    RatingBar storerate;
    ScrollView sc2;
    int sp = 12;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        b = this.getIntent().getExtras();

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));

        storecommit = (TableLayout) findViewById(R.id.storecommit);
        storecommit.setColumnShrinkable(0, false);
        storecommit.setColumnShrinkable(1, true);
        storecommit.setColumnStretchable(0, false);
        storecommit.setColumnStretchable(1, true);
        try {
            json_read = new JSONObject(b.getString("data"));
            JSONArray j1 = json_read.getJSONArray("Evaluation");
            JSONArray j2 = new JSONArray();
            row = new TableRow[j1.length()];
            for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                row[i] = new TableRow(this);
                row[i].setId(i);
                storecommit.addView(row[i]);
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
            submit = (Button)findViewById(R.id.submit);
            ed1 = (EditText)findViewById(R.id.ed1);
            storerate = (RatingBar)findViewById(R.id.store_rate);
            if (json_read.getBoolean("check")) {
                //JSONObject json_read2 = new JSONObject("myEvaluation");
                JSONArray j3 = json_read.getJSONArray("myEvaluation");
                JSONArray j4 = new JSONArray();
                for (int i = 0; i < j3.length(); i++) { //動態產生TableRow
                    j4 = j3.getJSONArray(i);
                }
                ed1.setText(j4.get(0).toString());
                storerate.setRating(Float.valueOf(j4.get(1).toString()));
                Commented();
            }else{
                sub();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "StoreError=" + e.toString());
        }
    }
    public void sub(){
        storerate.setIsIndicator(false);
        ed1.setBackgroundColor(Color.WHITE);
        ed1.setFocusable(true);
        ed1.setFocusableInTouchMode(true);
        submit.setText("提交");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json_write = new JSONObject();
                try {
                    json_write.put("action", "Comment");
                    //json_write.put("Sid",b.getInt("sid"));
                    json_write.put("Evaluation", ed1.getText().toString());
                    json_write.put("Escore", storerate.getRating());
                    globalVariable.c.send(json_write);
                    String tmp = globalVariable.c.receive();
                    if(tmp!=null) {
                        json_read = new JSONObject(tmp);
                        if (!json_read.getBoolean("check")) {//接收失敗原因
                            //String reason = json_read.getString("data");
                            //Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
                        } else {
                            Commented();
                        }
                    }else{
                        AlertDialog.Builder b=new AlertDialog.Builder(Pop.this);
                        //串聯呼叫法
                        b.setCancelable(false);
                        b.setTitle("警告")
                                .setMessage("連線逾時，請重新連線")
                                .setPositiveButton("連線", Pop.this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                .setNegativeButton("離開", Pop.this)
                                .show();
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void info(){
        try {
            json_write = new JSONObject();

            if(storecommit!=null) storecommit.removeAllViews();
            json_write.put("action", "Store");
            json_write.put("Id", b.getInt("sid"));
            globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            if(tmp!=null) {
                json_read = new JSONObject(tmp);
                JSONArray j1 = json_read.getJSONArray("Evaluation");
                JSONArray j2 = new JSONArray();
                row = new TableRow[j1.length()];
                for (int i = 0; i < j1.length(); i++) { //動態產生TableRow
                    row[i] = new TableRow(this);
                    row[i].setId(i);
                    storecommit.addView(row[i]);
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
            }else{
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
            Log.e("Exception", "StoreError=" + e.toString());
        }
    }
    public void Commented(){
        ed1.setFocusable(false);
        ed1.setBackgroundColor(Color.GRAY);
        info();
        //ed1.setFocusableInTouchMode(true);
        storerate.setIsIndicator(true);
        submit.setText("編輯");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which==DialogInterface.BUTTON_POSITIVE) {
            Intent it = new android.content.Intent(this, MainActivity.class);
            startActivity(it);
        }
        if(!randomSuggestRul.ActivityR.isFinishing()) randomSuggestRul.ActivityR.finish();
        if(!questionSuggestRul.ActivityQ.isFinishing()) questionSuggestRul.ActivityQ.finish();
        if(!userSuggestAct.ActivityU.isFinishing()) userSuggestAct.ActivityU.finish();
        if(!SearchAct.ActivityS.isFinishing()) SearchAct.ActivityS.finish();
        if(!ContentSuggestAct.ActivityC.isFinishing()) ContentSuggestAct.ActivityC.finish();
        if(!recordAct.ActivityR.isFinishing()) recordAct.ActivityR.finish();
        if(!StoreAct.ActivityS.isFinishing()) StoreAct.ActivityS.finish();
        if(!Main2Activity.ActivityM.isFinishing()) Main2Activity.ActivityM.finish();
        this.finish();
    }
}
