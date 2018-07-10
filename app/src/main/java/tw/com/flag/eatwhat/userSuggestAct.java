package tw.com.flag.eatwhat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class userSuggestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggest);
    }
    public void gotoUsersuggestAct2(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,userSuggestAct2.class);
        startActivity(it);
    }

}
