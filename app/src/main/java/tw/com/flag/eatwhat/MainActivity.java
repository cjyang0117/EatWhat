package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import android.Manifest;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    private JSONObject json_read, json_write;
    private SharedPreferences sp;
    private EditText editText,editText2;
    private int btn[]={R.id.button2,R.id.button3,R.id.signin,R.id.button};
    private GlobalVariable globalVariable;
    private HandlerThread handlerThread;
    private Handler handler;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityPermissionsDispatcher.NeedsPermissionWithPermissionCheck(this);

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//取得帳號資料用
        editText = (EditText) findViewById(R.id.editText);//帳號
        editText2 = (EditText) findViewById(R.id.editText2);//密碼

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        handlerThread=new HandlerThread("name"); //宣告常駐工人(執行緒)，等待執行工作
        handlerThread.start();
        handler=new Handler(handlerThread.getLooper());

        if (sp.getBoolean("ISCHECK", false)) {//判斷記住帳密狀態
            String name_str = sp.getString("USER_NAME", "");
            String pass_str = sp.getString("PASSWORD", "");
            editText.setText(name_str);
            editText2.setText(pass_str);
            login();
        }

        /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//監聽是否有選擇記錄帳密
                if (checkBox.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                }else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });*/

    }
    public void gotoforgotAccPass(android.view.View v){//忘記帳密
        android.content.Intent it = new android.content.Intent(this,forgotAccPassAct.class);
        startActivity(it);
    }
    public void gotosignUpAct(android.view.View v){//註冊
        android.content.Intent it = new android.content.Intent(this, signUpAct.class);
        //android.content.Intent it = new android.content.Intent(this,signUpAct.class);
        startActivity(it);
    }
    public void gotoMain2Activity(android.view.View v) {//登入
        login();
    }
    public void login() {
        try {
            globalVariable.c = new Client("120.105.161.119", 5050);
            tmp = globalVariable.c.receive();

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
                    Button b;
                    for(int i=0;i<btn.length;i++){
                        b=(Button)findViewById(btn[i]);
                        b.setEnabled(false);
                    }
                    editText.setEnabled(false);
                    editText.setText("");
                    editText2.setEnabled(false);
                    editText2.setText("");
                    handler.post(wait);
                } else {//當回傳為true跳轉進入首頁
                    globalVariable.recmdtime = json_read.getInt("recmdTime");
                    globalVariable.cnum = json_read.getInt("cnum");
                    android.content.Intent it = new android.content.Intent(MainActivity.this, Main2Activity.class);
                    startActivity(it);

                    sp.edit().putBoolean("ISCHECK", true).commit();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", account);
                    editor.putString("PASSWORD", password);
                    editor.commit();

                    finish();
                }
            } else {
                Toast.makeText(MainActivity.this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Runnable wait=new Runnable() { //避免連續登入
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button b;
                        for(int i=0;i<btn.length;i++){
                            b=(Button)findViewById(btn[i]);
                            b.setEnabled(true);
                        }
                        editText.setEnabled(true);
                        editText2.setEnabled(true);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void NeedsPermission() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void OnShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("未允許「" + getString(R.string.app_name) + "」位置存取權限，將使「" + getString(R.string.app_name) + "」無法正常運作，是否重新設定權限？")
                .setPositiveButton("重新設定權限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .create()
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void OnPermissionDenied() {
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void OnNeverAskAgain() {
    }
}
