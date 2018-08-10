package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class signUpAct extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    ImageView img2, img3;
    EditText editText4, editText5, editText6, editText7, editText8;
    boolean a = false, b = false, c = false;
    TextView tint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText8 = (EditText) findViewById(R.id.editText8);
        tint = (TextView) findViewById(R.id.tint);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        editText5.addTextChangedListener(new TextWatcher() {  //檢查信箱格式
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Linkify.addLinks(editText5.getText(), Linkify.EMAIL_ADDRESSES)) {
                    img2.setImageResource(android.R.drawable.button_onoff_indicator_on);
                    a = true;//判斷是否正確
                } else {
                    img2.setImageResource(android.R.drawable.checkbox_off_background);
                    a = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editText6.addTextChangedListener(new TextWatcher() {  //檢查帳號不得為空值
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText6.getText().toString().trim().length() > 0) {
                    tint.setText("");
                    b = true;
                } else{
                    tint.setText("不得為空");
                    b=false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        editText8.addTextChangedListener(new TextWatcher() {  //檢查帳號不得為空值
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = editText7.getText().toString();
                String passwordAgain = editText8.getText().toString();
                if (password.equals(passwordAgain)) {
                    img3.setImageResource(android.R.drawable.button_onoff_indicator_on);
                    c = true;
                } else {
                    img3.setImageResource(android.R.drawable.checkbox_off_background);
                    c = false;
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    public void gotoSignUpAct2(View v) {//下一步
        try {
            globalVariable.c = new Client("120.105.161.119", 5050);
            String tmp = globalVariable.c.receive();

            json_write = new JSONObject();
            json_write.put("action", "Signup");//
            String name = editText4.getText().toString();
            json_write.put("Name", name);
            String email = editText5.getText().toString();
            json_write.put("Email", email);
            String account = editText6.getText().toString();
            json_write.put("Saccount", account);
            String password = editText7.getText().toString();
            json_write.put("Spassword", password);
            String uname = "321";
            json_write.put("Uname", uname);
            String uphone = "321";
            json_write.put("Uphone", uphone);

            if (!a) {//當上面資料有誤時提示
                Toast.makeText(this, "請檢查信箱", Toast.LENGTH_SHORT).show();
            }else if (!b) {
                Toast.makeText(this, "帳號不得為空", Toast.LENGTH_SHORT).show();
            }else if (!c) {
                Toast.makeText(this, "請檢查密碼", Toast.LENGTH_SHORT).show();
            }else{//無誤則傳資料
                globalVariable.c.send(json_write);
                tmp = globalVariable.c.receive();
                if(tmp!=null) {
                    json_read = new JSONObject(tmp);
                    if (!json_read.getBoolean("check")) {//接收失敗原因
                        String reason = json_read.getString("data");
                        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
                    }else{//成功並關閉
                        Toast.makeText(this, name + "註冊成功", Toast.LENGTH_SHORT).show();
                        android.content.Intent it = new android.content.Intent(this, signUpAct2.class);
                        startActivity(it);
                        this.finish();
                    }
                }else{
                    Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
