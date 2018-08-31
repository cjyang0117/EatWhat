package tw.com.flag.eatwhat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class commentAct extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private EditText ed1;
    private JSONObject json_read, json_write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        TextView name = (TextView)findViewById(R.id.name);
        TextView email = (TextView)findViewById(R.id.email);
        name.setText("名稱:"+globalVariable.name);
        email.setText("信箱:"+globalVariable.email);
        ed1 = (EditText)findViewById(R.id.comment);
    }
    public void submit(View view) {
        try {
            json_write = new JSONObject();
            json_write.put("action", "feedback");
            json_write.put("feedback", ed1.getText().toString());
            globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            if(tmp!= null){
                json_read = new JSONObject(tmp);
                if (!json_read.getBoolean("check")) {
                    String reason;
                    reason = json_read.getString("data");
                    Toast.makeText(commentAct.this, reason, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(commentAct.this, "感謝您的意見", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }else {
                Toast.makeText(commentAct.this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
