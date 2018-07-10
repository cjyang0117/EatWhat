package tw.com.flag.eatwhat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class userSuggestAct2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggest_act2);
    }
    public void gotoRandomSuggestAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,randomSuggestAct.class);
        startActivity(it);
    }
    public void gotoQuestionSuggestAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,questionSuggestAct.class);
        startActivity(it);
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
