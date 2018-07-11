package tw.com.flag.eatwhat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;


public class questionSuggestAct extends AppCompatActivity {
    private double[] limit = {100000, 1000, 3000};
    private Spinner dist2;
    Gps gps2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_suggest);

        dist2 = (Spinner) findViewById(R.id.dist2);
        gps2 = new Gps(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        gps2.delete();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        gps2.update();
    }

    public void gotoRandomSuggestAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,randomSuggestAct.class);
        startActivity(it);
    }
    public void gotoQuestionSuggestAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,questionSuggestAct.class);
        startActivity(it);
    }
    public void gotoQuestionSuggestAct2(android.view.View v){
        int index2 = dist2.getSelectedItemPosition();
        double distlimit = limit[index2];
        Bundle b = new Bundle();
        Intent i = new Intent(this, questionSuggestAct2.class);
        b.putString("Distlimit",String.valueOf(distlimit));
        b.putString("Latitude", String.valueOf(gps2.getGPSLatitude()));
        b.putString("Longitude", String.valueOf(gps2.getGPSLongitude()));
        i.putExtras(b);
        startActivity(i);
        this.finish();
        /*android.content.Intent it = new android.content.Intent(this,questionSuggestAct2.class);
        startActivity(it);*/
    }
    public void gotoRecordAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,recordAct.class);
        startActivity(it);
    }
    public void gotoSearchAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,SearchAct.class);
        startActivity(it);
    }
    public void gotoMain2Activity(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,Main2Activity.class);
        startActivity(it);
    }

}
