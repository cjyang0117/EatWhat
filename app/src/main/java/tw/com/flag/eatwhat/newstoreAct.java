package tw.com.flag.eatwhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class newstoreAct extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    EditText editText4, editText5, editText6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
    }

    public void gotonewmenu(View v) {//下一步
        try {
            json_write = new JSONObject();
            json_write.put("action", "Newshop");
            String name = editText4.getText().toString();
            json_write.put("Sname", name);
            String email = editText5.getText().toString();
            json_write.put("Saddress", email);
            String account = editText6.getText().toString();
            json_write.put("Sphone", account);
            globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            json_read = new JSONObject(tmp);
            if (!json_read.getBoolean("check")) {//接收失敗原因
                String reason = json_read.getString("data");
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
            }else{//成功並關閉
                Intent i = new Intent(this, newmenuAct.class);
               // int sid = json_read.getInt("Sid");
               // i.putExtras("id",sid);
                startActivity(i);
                this.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
