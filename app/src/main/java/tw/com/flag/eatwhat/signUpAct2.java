package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class signUpAct2 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private CheckBox chk;
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    private int[] chk_id = {R.id.chk1, R.id.chk2, R.id.chk3, R.id.chk5, R.id.chk6, R.id.chk7};
    private int[] k_id = {16,19,36,15,30,18};
    private DrawerLayout mDrawerlayout;
    private SeekBar seekBar,seekBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_act2);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar2 = (SeekBar)findViewById(R.id.seekBar2);

        for(int id : chk_id) {
            ((CheckBox) findViewById(id)).setOnCheckedChangeListener(this);
            chk = (CheckBox) findViewById(chk_id[id]);
            chk.setTag(k_id[id]);
        }
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
            json_write = new JSONObject();
            json_write.put("action", "Basic");
            int[] like = new int[3];
            int count =0;
            for (int id : chk_id) {
                chk = (CheckBox) findViewById(id);
                if (chk.isChecked()) {
                    like[count]= Integer.parseInt(chk.getTag().toString());//不要吃的口味
                    count++;
                }
            }
            int spicy = seekBar.getProgress();
            int fries = seekBar2.getProgress();
            JSONArray j2 = new JSONArray(like);
            json_write.put("like", j2);
            json_write.put("spicy", spicy);
            json_write.put("fries", fries);
            String tmp = globalVariable.c.receive();
            if (tmp != null) {
                json_read = new JSONObject(tmp);
                if (!json_read.getBoolean("check")) {//接收失敗原因
                    String reason = json_read.getString("data");
                    Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
                } else {//成功並關閉
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    this.finish();
                }
            } else {
                Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
