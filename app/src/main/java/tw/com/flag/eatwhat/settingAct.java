package tw.com.flag.eatwhat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class settingAct extends AppCompatActivity {
    private SeekBar seekBar3,seekBar4,seekBar5;
    private static final String TAG = "settingAct";
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    float allW[] = {50f,50f,50f};
    String Nmae[] = {"價格","口味","店家評價"};
    public static final int[] colorselector = {
            Color.rgb(238,197,145), Color.rgb(246, 171, 148), Color.rgb(197,213,216)
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        try {
        json_write = new JSONObject();
        json_write.put("action", "weight");
        globalVariable.c.send(json_write);
        String tmp = globalVariable.c.receive();
        if (tmp != null) {
            json_read = new JSONObject(tmp);
            JSONArray j1 = json_read.getJSONArray("data");
            JSONArray j2;
            for (int i = 0; i < j1.length(); i++) {
                j2 = j1.getJSONArray(i);
                allW[0] = Float.valueOf(j2.get(0).toString())*100;
                allW[1] = Float.valueOf(j2.get(1).toString())*100;
                allW[2] = Float.valueOf(j2.get(2).toString())*100;
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        seekBar3 = (SeekBar)findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar)findViewById(R.id.seekBar4);
        seekBar5 = (SeekBar)findViewById(R.id.seekBar5);
        seekBar3.setProgress((int)allW[0]);
        seekBar4.setProgress((int)allW[1]);
        seekBar5.setProgress((int)allW[2]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        /*Button subscribeButton = findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Subscribing to news topic");
                // [START subscribe_topics]
                FirebaseMessaging.getInstance().subscribeToTopic("news")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = getString(R.string.msg_subscribed);
                                if (!task.isSuccessful()) {
                                    msg = getString(R.string.msg_subscribe_failed);
                                }
                                Log.d(TAG, msg);
                                Toast.makeText(settingAct.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END subscribe_topics]
            }
        });*/
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
    public void gotoreset(View view) {
        try {
            json_write = new JSONObject();
            json_write.put("action", "reset");
            float price = (float)seekBar3.getProgress()/(seekBar3.getProgress()+seekBar4.getProgress()+seekBar5.getProgress());
            float flavor = (float)seekBar4.getProgress()/(seekBar3.getProgress()+seekBar4.getProgress()+seekBar5.getProgress());
            float store = (float)seekBar5.getProgress()/(seekBar3.getProgress()+seekBar4.getProgress()+seekBar5.getProgress());
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
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
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
        dataSet.setColors(ColorTemplate.createColors(colorselector));
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
}
