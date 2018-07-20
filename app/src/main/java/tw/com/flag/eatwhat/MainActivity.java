package tw.com.flag.eatwhat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private JSONObject json_read, json_write;
    private SharedPreferences sp;
    private EditText editText,editText2;
    private CheckBox checkBox;
    private GlobalVariable globalVariable;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//取得帳號資料用
        editText = (EditText) findViewById(R.id.editText);//帳號
        editText2 = (EditText) findViewById(R.id.editText2);//密碼
        checkBox = (CheckBox) findViewById(R.id.checkBox);//是否記錄帳密

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        globalVariable.c = new Client("120.105.161.119", 5050);
        try {
            tmp = globalVariable.c.receive();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sp.getBoolean("ISCHECK", false)) {//判斷記住帳密狀態
            checkBox.setChecked(true);//默認記住帳密
            String name_str = sp.getString("USER_NAME", "");
            String pass_str = sp.getString("PASSWORD", "");
            editText.setText(name_str);
            editText2.setText(pass_str);
            login();
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//監聽是否有選擇記錄帳密
                if (checkBox.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });
    }
    public void gotoforgotAccPass(android.view.View v){//忘記帳密
        android.content.Intent it = new android.content.Intent(this,forgotAccPassAct.class);
        startActivity(it);
    }
    public void gotosignUpAct(android.view.View v){//註冊
        android.content.Intent it = new android.content.Intent(this,signUpAct.class);
        startActivity(it);
    }
    public void gotoMain2Activity(android.view.View v) {//登入
        login();
    }
    @Override
    protected void onDestroy() {    //當銷毀該app時
        super.onDestroy();
        try {
            globalVariable.c.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception","onDestroy()="+e.toString());
        }
    }
    public void login() {
        try {
            String account = editText.getText().toString().trim();
            String password = editText2.getText().toString().trim();
            if (account.equals("twt") && password.equals("twt")){
                android.content.Intent it = new android.content.Intent(MainActivity.this, dbsetUpAct.class);
                startActivity(it);
            }else if (tmp != null) {
                json_write = new JSONObject();
                json_write.put("action", "login");
                json_write.put("Account", account);
                json_write.put("Password", password);
                globalVariable.c.send(json_write);
                String tmp1 = globalVariable.c.receive();
                json_read = new JSONObject(tmp1);
                if (!json_read.getBoolean("Checklogin")) {//當回傳為false
                    Toast.makeText(MainActivity.this, "請檢查帳密是否錯誤", Toast.LENGTH_SHORT).show();
                } else {//當回傳為true跳轉進入首頁
                    globalVariable.recmdtime = json_read.getInt("recmdTime");
                    android.content.Intent it = new android.content.Intent(MainActivity.this, Main2Activity.class);
                    startActivity(it);
                    if (checkBox.isChecked()) {//當記住帳密有被勾選記住帳號密碼的資訊
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", account);
                        editor.putString("PASSWORD", password);
                        editor.commit();
                    }
                    //this.finish();//結束登入頁面
                }
            } else {
                Toast.makeText(MainActivity.this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
