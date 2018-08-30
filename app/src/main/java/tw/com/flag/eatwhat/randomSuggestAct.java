package tw.com.flag.eatwhat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;


public class randomSuggestAct extends AppCompatActivity
        implements DialogInterface.OnClickListener{
    static Activity ActivityA;
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
   // private Spinner dist, disttime;
    private CheckBox chk;
    private int[] chk_id = {R.id.chk1, R.id.chk2, R.id.chk3, R.id.chk4, R.id.chk5, R.id.chk6, R.id.chk7, R.id.chk8};
    private int[] k_id = {16, 19, 36, 10, 7, 9, 25, 26};
    //private String[] eatime;int123
    private int[] eatime={2,1,33};//主食、早餐、點心
    private double[] limit = {100000, 300, 1000, 3000};
    private CheckBox sw1;
    TextView tv;
    Gps gps1;
    RadioGroup radioGroup1;
    RadioGroup radioGroup2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_suggest);

        ActivityA=this;
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        gps1 = new Gps(this);
        tv = (TextView) findViewById(R.id.textView);
        sw1=(CheckBox)findViewById(R.id.sw1);

        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);

        for (int id=0;id<chk_id.length;id++) {
            chk = (CheckBox) findViewById(chk_id[id]);
            chk.setTag(k_id[id]);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps1.delete();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        gps1.update();
    }

    public void gotoResult(android.view.View v){
        try {
                //int index = disttime.getSelectedItemPosition();
                int index = 0;
                for (int i = 0; i < radioGroup1.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) radioGroup1.getChildAt(i);
                    if (rb.isChecked()) {
                        index = i;
                        break;
                    }
                }
                int time = eatime[index];

                //int index2 = dist.getSelectedItemPosition();
                int index2 = 0;
                for (int i = 0; i < radioGroup2.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) radioGroup2.getChildAt(i);
                    if (rb.isChecked()) {
                        index2 = i;
                        break;
                    }
                }
                double distlimit = limit[index2];
                json_write = new JSONObject();
                json_write.put("action", "Random");
                json_write.put("Longitude", gps1.getGPSLongitude());//經度
                json_write.put("Latitude", gps1.getGPSLatitude());//緯度
                json_write.put("Eatype", time);//主餐1早餐2點心3
                json_write.put("isTime", sw1.isChecked());
                int[] dont1 = new int[10];
                int count =0;
                for (int id : chk_id) {
                    chk = (CheckBox) findViewById(id);
                    if (chk.isChecked()) {
                        dont1[count]= Integer.parseInt(chk.getTag().toString());//不要吃的口味
                        count++;
                    }
                }
                int[] dont2 = new int[count];
                if(count !=0) {
                    for (int i = 0; i < count; i++) {
                        dont2[i] = dont1[i];
                    }
                }else{
                    dont2 = new int[1];
                    dont2 [0] = -1;
                }
                JSONArray j2 = new JSONArray(dont2);
                json_write.put("Dontwant", j2);
                json_write.put("Distlimit", distlimit);//距離限制(0為不限距離，分有0, 2, 4, 6)
                globalVariable.c.send(json_write);
                tv.setText("緯度 :" + gps1.getGPSLatitude() + "  , 經度 :  " + gps1.getGPSLongitude());
                String tmp = globalVariable.c.receive();
                if (tmp != null) {
                    json_read = new JSONObject(tmp);
                    if (!json_read.getBoolean("check")) {//接收失敗原因
                        String reason = json_read.getString("data");
                        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
                    } else {//成功並關閉
                        Bundle b = new Bundle();
                        Intent i = new Intent(this, randomSuggestRul.class);
                        b.putString("data", tmp);
                        b.putString("Latitude", String.valueOf(gps1.getGPSLatitude()));
                        b.putString("Longitude", String.valueOf(gps1.getGPSLongitude()));
                        i.putExtras(b);
                        startActivity(i);
                        //this.finish();
                    }
                } else {
                    //Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder b=new AlertDialog.Builder(this);
                    //串聯呼叫法
                    b.setCancelable(false);
                    b.setTitle("警告")
                            .setMessage("連線逾時，請重新連線")
                            .setPositiveButton("連線", this)       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                            .setNegativeButton("離開", this)
                            .show();
                }
            //tv.setText("經度 : " + gps1.getGPSLatitude() + "  , 緯度 : " + gps1.getGPSLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
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
