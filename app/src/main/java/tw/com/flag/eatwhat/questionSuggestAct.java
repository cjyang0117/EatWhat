package tw.com.flag.eatwhat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;


public class questionSuggestAct extends AppCompatActivity {
    static Activity Activityqa;
    private double[] limit = {1000000, 1000, 3000};//使用者距離限制
    private Spinner dist2;
    private CheckBox checkbox;
    private GlobalVariable globalVariable;
    private JSONObject json_write;
    Gps gps2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_suggest);

        Activityqa=this;
        checkbox = (CheckBox)findViewById(R.id.sw1);
        dist2 = (Spinner) findViewById(R.id.dist2);
        gps2 = new Gps(this);

        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        try {
            json_write=new JSONObject();
            json_write.put("action", "useLog");
            json_write.put("Fid", 2);
            globalVariable.c.send(json_write);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps2.delete();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        gps2.update();
    }
    public void gotoQuestionSuggestAct2(android.view.View v){
        int index2 = dist2.getSelectedItemPosition();
        double distlimit = limit[index2];
        Bundle b = new Bundle();
        Intent i = new Intent(this, questionSuggestAct2.class);
        b.putString("Distlimit",String.valueOf(distlimit));
        b.putString("Latitude", String.valueOf(gps2.getGPSLatitude()));
        b.putString("Longitude", String.valueOf(gps2.getGPSLongitude()));
        b.putBoolean("isTime", checkbox.isChecked());
        i.putExtras(b);
        startActivity(i);
        //this.finish();
    }
}

