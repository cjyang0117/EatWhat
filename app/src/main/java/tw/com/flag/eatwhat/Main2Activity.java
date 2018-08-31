package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private GlobalVariable globalVariable;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private JSONObject json_read, json_write;
    static Activity ActivityM;
    String store;
    String[] dish;

    private boolean checkgps=false,random = false,question = false,search = false;
    private LocationManager status;
    private String notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityM=this;

        setContentView(R.layout.activity_main2);
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.inflateHeaderView(R.layout.header);
        TextView name = (TextView) header.findViewById(R.id.name);
        TextView email = (TextView) header.findViewById(R.id.email);
        name.setText("名稱:"+globalVariable.name);
        email.setText("信箱:"+globalVariable.email);
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


        status = (LocationManager) (this.getSystemService(LOCATION_SERVICE));
    }
    public void gotoRandomSuggestAct(android.view.View v){
        Checkgps();
        if(checkgps) {
            android.content.Intent it = new android.content.Intent(this, randomSuggestAct.class);
            startActivity(it);
        }else{
            notice = "未開啟GPS，隨機推薦功能將無法正常使用，是否開啟？";
            OpenGps();
            random=true;
        }
    }
    public void gotoQuestionSuggestAct(android.view.View v){
        Checkgps();
        if(checkgps) {
            android.content.Intent it = new android.content.Intent(this, questionSuggestAct.class);
            startActivity(it);
        }else{
            notice = "未開啟GPS，提問推薦功能將無法正常使用，是否開啟？";
            OpenGps();
            question = true;
        }
    }
    public void gotoRecordAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,recordAct.class);
        startActivity(it);
    }
    public void gotoSearchAct(android.view.View v){
        Checkgps();
        if(checkgps) {
            android.content.Intent it = new android.content.Intent(this, SearchAct.class);
            startActivity(it);
        }else{
            notice = "未開啟GPS，查詢功能將無法提供位置資訊，是否開啟？";
            OpenGps();
            search = true;
        }
    }
    public void gotoUsersuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,userSuggestAct.class);
        startActivity(it);
    }
    public void gotoContentsuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,ContentSuggestAct.class);
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
    public void Checkgps(){
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            checkgps = true;
        } else {
            checkgps = false;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent it;
        switch(id){
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
                sp.edit().putBoolean("checkemail", false).commit();
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
    void OpenGps() {
        if (!checkgps){
            new AlertDialog.Builder(this,R.style.AlertDialogCustom)
                    .setMessage(notice)
                    .setCancelable(true)
                    .setPositiveButton("開啟", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(it);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkgps = false;
                            random = false;
                            question = false;
                            if(search){
                                search = false;
                                android.content.Intent it = new android.content.Intent(Main2Activity.this, SearchAct.class);
                                startActivity(it);
                            }
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }

    }
    @Override
    public void onRestart() {
        super.onRestart();
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) ||status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            checkgps = true;
            if(random) {
                random = false;
                Timer timer = new Timer();
                TimerTask tast = new TimerTask() {
                    @Override
                    public void run() {
                        android.content.Intent it = new android.content.Intent(Main2Activity.this, randomSuggestAct.class);
                        startActivity(it);
                    }
                };
                timer.schedule(tast, 1000);
            }else if (question) {
                question = false;
                Timer timer = new Timer();
                TimerTask tast = new TimerTask() {
                    @Override
                    public void run() {
                        android.content.Intent it = new android.content.Intent(Main2Activity.this, questionSuggestAct.class);
                        startActivity(it);
                    }
                };
                timer.schedule(tast, 1000);
            }
        } else{
            checkgps = false;
            random = false;
            question = false;
            if(search){
                search = false;
                android.content.Intent it = new android.content.Intent(Main2Activity.this, SearchAct.class);
                startActivity(it);
            }
        }
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
}
