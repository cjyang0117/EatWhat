package tw.com.flag.eatwhat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.ArrayList;

public class recordAct extends AppCompatActivity {
    private TableLayout tblayout, tblayout2;
    private TableRow[] row,row2;
    private int sp=14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=12;
        }

        try {
            FileInputStream in=openFileInput("think.txt");
            int idx;
            byte[] buffer = new byte[1024];
            String s="";
            idx=in.read(buffer);
            while(idx!=-1){
                s=s+new String(buffer, 0, idx);
                idx=in.read(buffer);
            }
            Toast.makeText(this, s, Toast.LENGTH_LONG).show();

            //if(tblayout!=null) tblayout.removeAllViews();

            if(s!="") {
                tblayout = (TableLayout) findViewById(R.id.tbLayout);
                tblayout.setColumnShrinkable(0,true);
                tblayout.setColumnShrinkable(1,true);
                tblayout.setColumnStretchable(0, true);
                tblayout.setColumnStretchable(1, true);
                tblayout.setColumnStretchable(3, true);

                idx=0;
                int idx2, i2=0;
                int count=0;
                ArrayList<TableRow> row=new ArrayList<>();
                while (s.contains(",")){
                    row.add(new TableRow(this));
                    TextView[] tv=new TextView[3];
                    for(int i=0;i<3;i++){
                        idx=s.indexOf(",");
                        tv[i]=new TextView(this);
                        tv[i].setText(s.substring(0, idx));
                        s=s.substring(idx+1);
                        row.get(count).addView(tv[i]);
                    }
                    Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                    btn.setText("吃");
                    btn.setId(count);
                    btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                    row.get(count).addView(btn);
                    tblayout.addView(row.get(count));
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean onCreateOptionMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(0,0,menu.NONE,"刪除");

        return true;
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
