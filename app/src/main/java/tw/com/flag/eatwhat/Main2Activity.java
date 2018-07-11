package tw.com.flag.eatwhat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.ImageView;

import android.view.ViewGroup.LayoutParams;
public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

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
        }

        return false;

    }
}
