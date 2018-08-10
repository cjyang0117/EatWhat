package tw.com.flag.eatwhat;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class signUpAct2 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private CheckBox chk;
    private int[] chk_id = {R.id.chk1, R.id.chk2, R.id.chk3, R.id.chk5, R.id.chk6, R.id.chk7};
    private DrawerLayout mDrawerlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_act2);

        for(int id : chk_id)
            ((CheckBox)findViewById(id)).setOnCheckedChangeListener(this);
    }
    int items =0;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            items++;
        }else{
            items --;
        }
        if(items==3) {
            for (int id : chk_id){
                chk = (CheckBox) findViewById(id);
                if (!chk.isChecked()) {
                    chk.setEnabled(false);
                }
            }
        }else{
            for(int id : chk_id) {
                chk = (CheckBox) findViewById(id);
                chk.setEnabled(true);
            }
        }
    }

}
