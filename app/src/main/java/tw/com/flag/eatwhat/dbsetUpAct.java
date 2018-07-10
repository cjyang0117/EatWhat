package tw.com.flag.eatwhat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class dbsetUpAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbset_up);
    }

    public void gotonewstore(View v) {//新增店家
        android.content.Intent it = new android.content.Intent(this, newstoreAct.class);
        startActivity(it);
    }

    public void gotomodifystore(View v) {//修改店家
        android.content.Intent it = new android.content.Intent(this, signUpAct2.class);
        startActivity(it);
    }
}
