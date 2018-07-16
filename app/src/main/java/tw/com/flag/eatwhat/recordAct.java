package tw.com.flag.eatwhat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class recordAct extends AppCompatActivity {
    private TableLayout tblayout, tblayout2;
    ArrayList<TableRow> row;
    private int sp=14;
    private int count, count2;
    boolean change=true, change2=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        DisplayMetrics dm = new DisplayMetrics();   //取得螢幕寬度並設定ScrollView尺寸
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels<=480){
            sp=12;
        }
        tblayout2 = (TableLayout) findViewById(R.id.tb2Layout);

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
            in.close();

            //if(tblayout!=null) tblayout.removeAllViews();

            if(s!="") {
                tblayout = (TableLayout) findViewById(R.id.tbLayout);
                tblayout.setColumnShrinkable(0,true);
                tblayout.setColumnShrinkable(1,true);
                tblayout.setColumnStretchable(0, true);
                tblayout.setColumnStretchable(1, true);
                tblayout.setColumnStretchable(3, true);

                count=0;
                row=new ArrayList<>();
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
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b=(Button)v;
                            try {
                                int i;
                                for(i=0;i<row.size();i++){
                                    if(((Button)row.get(i).getChildAt(3)).getId()==b.getId()){
                                        FileOutputStream out = openFileOutput("eat.txt", MODE_APPEND);
                                        String s=((TextView)row.get(i).getChildAt(0)).getText().toString()+","+((TextView)row.get(i).getChildAt(1)).getText().toString()+","+((TextView)row.get(i).getChildAt(2)).getText().toString()+",";
                                        out.write(s.getBytes());
                                        out.close();

                                        tblayout.removeView(row.get(i));
                                        row.remove(i);
                                        change=true; change2=true;
                                        break;
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    });
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
    public void switchClick(View v){
        Button b=(Button)v;
        switch (b.getId()){
            case R.id.button22:
                b.setEnabled(false);
                b=findViewById(R.id.button30);
                b.setEnabled(true);
                ScrollView sc=findViewById(R.id.sc1);
                sc.setVisibility(View.VISIBLE);
                sc=findViewById(R.id.sc2);
                sc.setVisibility(View.INVISIBLE);
                break;
            case R.id.button30:
                b.setEnabled(false);
                b=findViewById(R.id.button22);
                b.setEnabled(true);
                ScrollView sc2=findViewById(R.id.sc2);
                sc2.setVisibility(View.VISIBLE);
                sc2=findViewById(R.id.sc1);
                sc2.setVisibility(View.INVISIBLE);

                if(change){
                    change=false;
                    if(tblayout2!=null) tblayout2.removeAllViews();
                    try {
                        FileInputStream in = openFileInput("eat.txt");
                        byte[] buffer = new byte[1024];
                        String s = "";
                        int idx = in.read(buffer);
                        while (idx != -1) {
                            s = s + new String(buffer, 0, idx);
                            idx = in.read(buffer);
                        }
                        in.close();

                        if(s!="") {
                            tblayout2.setColumnShrinkable(0,true);
                            tblayout2.setColumnShrinkable(1,true);
                            tblayout2.setColumnStretchable(0, true);
                            tblayout2.setColumnStretchable(1, true);
                            tblayout2.setColumnStretchable(3, true);

                            ArrayList<TableRow> row2=new ArrayList<>();
                            count2=0;
                            while (s.contains(",")){
                                row2.add(new TableRow(this));
                                TextView[] tv=new TextView[3];
                                for(int i=0;i<3;i++){
                                    idx=s.indexOf(",");
                                    tv[i]=new TextView(this);
                                    tv[i].setText(s.substring(0, idx));
                                    s=s.substring(idx+1);
                                    row2.get(count2).addView(tv[i]);
                                }
                                Button btn=new Button(this, null, android.R.attr.buttonStyleSmall);
                                btn.setText("評價");
                                btn.setId(count2);
                                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
                                row2.get(count2).addView(btn);
                                tblayout2.addView(row2.get(count2));
                                count2++;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public void clearClick(View v){
        CheckBox cb;
        for(int i=0;i<row.size();i++){
            cb=new CheckBox(this);
            row.get(i).addView(cb);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(change2){
                FileOutputStream out = openFileOutput("think.txt", MODE_PRIVATE);
                String s="";
                for(int i=0;i<row.size();i++){
                    s+=((TextView)row.get(i).getChildAt(0)).getText().toString()+","+((TextView)row.get(i).getChildAt(1)).getText().toString()+","+((TextView)row.get(i).getChildAt(2)).getText().toString()+",";
                }
                out.write(s.getBytes());
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
