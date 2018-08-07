package tw.com.flag.eatwhat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.view.ViewGroup.LayoutParams;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private GlobalVariable globalVariable;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private JSONObject json_read, json_write;
    String store;
    String[] dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem((int)R.id.home);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        height = height/3;
        ImageView imageView2; //咖啡背景圖
        imageView2 = findViewById(R.id.imageView2);
        LayoutParams para;
        para = imageView2.getLayoutParams();
        para.height = height;
        para.width = width;
        imageView2.setLayoutParams(para);

        dish = new String[]{"雞腿飯", "魯肉飯", "排骨飯", "水餃", "陽春麵"};
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        try {
            json_write.put("action", "re");
            globalVariable.c.send(json_write);
            String tmp = globalVariable.c.receive();
            json_read = new JSONObject(tmp);
            if(tmp!=null) {
                json_read.getString("data");
                dish = new String[]{"雞腿飯", "魯肉飯", "排骨飯", "水餃", "陽春麵"};
            }else{
                Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //commitrate();
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
    public void gotoUsersuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,userSuggestAct.class);
        startActivity(it);
    }
    @Override
    public void onBackPressed() { //案返回健
        if (mDrawerlayout.isDrawerOpen(findViewById(R.id.nav_view))) //側選單開著
            mDrawerlayout.closeDrawers(); //關抽屜
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent it;
        switch(id){
            case R.id.home:
                mDrawerlayout.closeDrawers();
                break;
            case R.id.setting:
                it = new android.content.Intent(this, settingAct.class);
                startActivity(it);
                break;
            case R.id.comment:
                it = new android.content.Intent(this, commentAct.class);
                startActivity(it);
                break;
            case R.id.logout:
                it = new android.content.Intent(this, MainActivity.class);
                SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//取得帳號資料用
                sp.edit().putBoolean("ISCHECK", false).commit();
                startActivity(it);
                try {
                    globalVariable.c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            globalVariable.c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*public void commitrate(){
        final Dialog rankDialog;
        RatingBar rating ;
        rankDialog = new Dialog(Main2Activity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        rating = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        rating.setRating(5);

        TextView storename = (TextView)rankDialog.findViewById(R.id.rank_dialog_text1);
        storename.setText("老八");
        Spinner myeatlist = (Spinner)rankDialog.findViewById(R.id.myeatlist);
        ArrayAdapter<String> eatList = new ArrayAdapter<>(Main2Activity.this,android.R.layout.simple_spinner_item,dish);
        myeatlist.setAdapter(eatList);

        Button skipButton = (Button) rankDialog.findViewById(R.id.rank_dialog_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.dismiss();
            }
        });
        Button okButton = (Button) rankDialog.findViewById(R.id.rank_dialog_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button recommend = (Button) rankDialog.findViewById(R.id.rank_dialog_recommend);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.dismiss();
            }
        });
        rankDialog.show();
    }*/
}
