package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class signUpAct2 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private CheckBox chk;
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    private int[] chk_id = {R.id.imageView4,R.id.chk1, R.id.chk2, R.id.chk3, R.id.chk5,R.id.imageView5,R.id.chk6, R.id.chk7};
    private DrawerLayout mDrawerlayout;
    private SeekBar seekBar,seekBar2,seekBar3,seekBar4,seekBar5;
    float allW[] = {50f,50f,50f};
    String Nmae[] = {"價格","口味","店家評價"};
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_act2);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar)findViewById(R.id.seekBar4);
        seekBar5 = (SeekBar)findViewById(R.id.seekBar5);

        for(int id : chk_id) {
            ((CheckBox) findViewById(id)).setOnCheckedChangeListener(this);
        }
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                allW[0] = seekBar3.getProgress();
                setupPieChart();
            }
        });
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                allW[1] = seekBar4.getProgress();
                setupPieChart();
            }
        });
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                allW[2] = seekBar5.getProgress();
                setupPieChart();
            }
        });
        setupPieChart();
    }
    int items =0;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            items++;
        }else{
            items --;
        }
        if(items==3) {
            for (int id : chk_id){
                chk = (CheckBox) findViewById(id);
                if (!chk.isChecked()) {
                    chk.setEnabled(false);
                }
            }
        }else{
            for(int id : chk_id) {
                chk = (CheckBox) findViewById(id);
                chk.setEnabled(true);
            }
        }
    }

    public void gotoSignin(View view) {
        try {
            if(items<3){
                Toast.makeText(this, "喜好口味請選3個", Toast.LENGTH_LONG).show();
            }else{
                json_write = new JSONObject();
                json_write.put("action", "Signup2");
                int[] like = new int[chk_id.length];
                int spicy = seekBar.getProgress();
                int fries = seekBar2.getProgress();
                for (int i = 0 ; i< chk_id.length ; i++) {
                    chk = (CheckBox) findViewById(chk_id[i]);
                    like[i] = 0;
                    if (chk.isChecked()) {
                        like[i] = 5;
                    }
                }
                like[0] = spicy;
                like[5] = fries;
                JSONArray j2 = new JSONArray(like);
                json_write.put("score", j2);
                float price = (float)seekBar3.getProgress()/(seekBar3.getProgress()+seekBar4.getProgress()+seekBar5.getProgress());
                float flavor = (float)seekBar4.getProgress()/(seekBar3.getProgress()+seekBar4.getProgress()+seekBar5.getProgress());
                float store = (float)seekBar4.getProgress()/(seekBar3.getProgress()+seekBar4.getProgress()+seekBar5.getProgress());
                float [] weight =new float[3];
                weight[0]=price; weight[1]=flavor;weight[2]=store;
                JSONArray j3 = new JSONArray(weight);
                json_write.put("weight", j3);
                globalVariable.c.send(json_write);
                String tmp = globalVariable.c.receive();
                if (tmp != null) {
                    json_read = new JSONObject(tmp);
                    if (!json_read.getBoolean("check")) {//接收失敗原因
                        String reason = json_read.getString("data");
                        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
                    } else {//成功並關閉
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        user.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "Email sent.");
                                        }
                                    }
                                });
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        this.finish();
                    }
                } else {
                    user.delete();
                    Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setupPieChart(){
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i = 0 ;i< allW.length ; i++){
            pieEntries.add(new PieEntry(allW[i],Nmae[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"(單位:%)");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        PieChart chart = (PieChart)findViewById(R.id.chart);
        chart.setData(data);
        chart.setUsePercentValues(true);
        chart.animateY(1000);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            globalVariable.c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            user = FirebaseAuth.getInstance().getCurrentUser();
            user.delete();
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
