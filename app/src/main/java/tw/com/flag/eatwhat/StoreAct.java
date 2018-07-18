package tw.com.flag.eatwhat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

public class StoreAct extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    TextView storename,storeaddr,storecell;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        storename = (TextView)findViewById(R.id.storename);
        storeaddr = (TextView)findViewById(R.id.storeaddr);
        storecell = (TextView)findViewById(R.id.storecell);
        b = this.getIntent().getExtras();
        storename.setText(b.getString("data"));
        storeaddr.setText(b.getString("dataddr"));
    }
}
