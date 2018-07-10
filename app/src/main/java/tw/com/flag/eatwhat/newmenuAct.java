package tw.com.flag.eatwhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class newmenuAct extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    EditText editText4, editText5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        /*Intent it = getIntent();
        int sid = it.getIntExtra("id",999999);*/
    }

    public void gotonewmenu2(View v) {//繼續新增
        try {
            json_write = new JSONObject();
            json_write.put("action", "Newmenu");
            String name = editText4.getText().toString();
            json_write.put("Mname", name);
            String price = editText5.getText().toString();
            json_write.put("Price", price);

           /* globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            json_read = new JSONObject(tmp);
            if (!json_read.getBoolean("check")) {//接收失敗原因
                String reason = json_read.getString("data");
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
            }else{//成功並關閉
                */
                Toast.makeText(this, name + "新增菜單成功", Toast.LENGTH_SHORT).show();
                editText4.setText("");
                editText5.setText("");
           // }
       } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void gotonewdbsetupAct(View v) {//完成
        Intent it = new Intent(this, dbsetUpAct.class);
        startActivity(it);
        this.finish();
    }
}
