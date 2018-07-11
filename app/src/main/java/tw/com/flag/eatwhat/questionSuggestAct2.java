package tw.com.flag.eatwhat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class questionSuggestAct2 extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    private String[] eatime =  {"早餐","點心","正餐","宵夜"};
    private String[] bd =  {"甜","辣","牛肉","豬肉","雞肉","海鮮","漢堡","麵食","炸","煎餅/蛋餅類","吐司","貝果","中式"};
    private String[] mn =  {"牛肉","豬肉","雞肉","羊肉","海鮮","漢堡","米食","鴨肉","粥","麵食","異國料理","咖哩","滷味","日/韓式","炸","鐵板","火鍋","餃子類","中式"};
    boolean bdroute = false , mnroute = false ;
    String[] like;
    String[] sosolike;
    String[] dontlike;
    Button OK , soso , NO;
    TextView question;
    int questioncount , score ,countlike , countsoso , countdont;
    Bundle b;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_suggest_act2);

        b = this.getIntent().getExtras();
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        OK = (Button)findViewById(R.id.button54);
        soso = (Button)findViewById(R.id.button55);
        NO = (Button)findViewById(R.id.button56);
        questioncount = 0; score = 0;
        like = new String[19];
        sosolike = new String[19];
        dontlike = new String[19];
        countlike =0;countsoso =0; countdont=0;
        question = (TextView)findViewById(R.id.question);
        for(int i=0; i < bd.length; i ++){
            int b =(int)(Math.random()*12);
            String tmp = bd[b];
            bd[b] = bd[i];
            bd[i] = tmp;
        }
        for(int i=0; i < mn.length; i ++){
            int m =(int)(Math.random()*18);
            String tmp = mn[m];
            mn[m] = mn[i];
            mn[i] = tmp;
        }
        randomeatmain();
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(OK.getText().equals("早餐")||OK.getText().equals("點心")){
                        bdroute = true;
                        like[countlike]=  OK.getText().toString();
                        countlike++;
                        OK.setText("OK");soso.setText("還好");
                        question.setText("要吃" + bd[questioncount] + "嗎?");
                        score +=2;
                    }else if(OK.getText().equals("正餐")||OK.getText().equals("宵夜")){
                        mnroute = true;
                        question.setText("要吃" + mn[questioncount ] + "嗎?");
                        like[countlike]=  OK.getText().toString();
                        countlike++;
                        OK.setText("OK");soso.setText("還好");
                        score +=2;
                    }
                    else{
                        if (bdroute) {
                            like[countlike]=  bd[questioncount];
                            score += 2;
                            questioncount++;
                            countlike++;
                            if (score < 5 && questioncount <4) {
                                question.setText("要吃" + bd[questioncount] + "嗎?");
                            }else if(score > 3 && questioncount >=4){
                                send();
                            }else{
                                send();
                            }
                        }
                        if (mnroute) {
                            like[countlike]=  mn[questioncount];
                            score += 2;
                            countlike++;
                            questioncount++;
                            if (score < 5 && questioncount <4) {
                                question.setText("要吃" + mn[questioncount] + "嗎?");
                            }else if(score > 3 && questioncount >=4){
                                send();
                            }else{
                                send();
                            }
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        soso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(soso.getText().equals("早餐")||soso.getText().equals("點心")){
                        bdroute = true;
                        like[countlike]=  soso.getText().toString();
                        countlike++;
                        OK.setText("OK");soso.setText("還好");
                        question.setText("要吃" + bd[questioncount] + "嗎?");
                        score +=2;
                    }else if(soso.getText().equals("正餐")||soso.getText().equals("宵夜")){
                        mnroute = true;
                        question.setText("要吃" + mn[questioncount] + "嗎?");
                        like[countlike]=  soso.getText().toString();
                        countlike++;
                        OK.setText("OK");soso.setText("還好");
                        score +=2;
                    }
                    else{
                        if (bdroute) {
                            sosolike[countsoso]=  bd[questioncount];
                            countsoso++;
                            questioncount++;
                            score += 1;
                            if (score < 5 && questioncount <4) {
                                question.setText("要吃" + bd[questioncount] + "嗎?");
                            }else if(score > 3 && questioncount >=4){
                                send();
                            }else{
                                send();
                            }
                        }
                        if (mnroute) {
                            sosolike[countsoso] = mn[questioncount];
                            countsoso++;
                            questioncount++;
                            score += 1;
                            if (score < 5 && questioncount <4) {
                                question.setText("要吃" + mn[questioncount] + "嗎?");
                            }else if(score > 3 && questioncount >=4){
                                send();
                            }else{
                                send();
                            }
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!bdroute && !mnroute){
                        randomeatmain();
                    }
                    else {
                        if (bdroute) {
                            dontlike[countdont] = bd[questioncount];
                            countdont++;
                            questioncount++;
                            if (score < 3 | questioncount < 4) {
                                question.setText("要吃" + bd[questioncount] + "嗎?");
                            } else {
                                send();
                            }
                        }
                        if (mnroute) {
                            dontlike[countdont] = mn[questioncount];
                            countdont++;
                            questioncount++;
                            if ( score < 3 | questioncount < 4) {
                                question.setText("要吃" + mn[questioncount] + "嗎?");
                            } else {
                                send();
                            }
                        }
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void gotoRandomSuggestAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,randomSuggestAct.class);
        startActivity(it);
        this.finish();
    }
    public void gotoQuestionSuggestAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,questionSuggestAct.class);
        startActivity(it);
        this.finish();
    }
    public void gotoRecordAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,recordAct.class);
        startActivity(it);
        this.finish();
    }
    public void gotoSearchAct(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,SearchAct.class);
        startActivity(it);
        this.finish();
    }
    public void gotoMain2Activity(android.view.View v){
        android.content.Intent it = new android.content.Intent(this,Main2Activity.class);
        startActivity(it);
        this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//按返回頁面關閉
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void randomeatmain(){
        int x = (int)(Math.random()* 3);
        int y = (int)(Math.random()* 3);
        while(y == x){
            y = (int)(Math.random()* 3);
        }
        OK.setText(eatime[x]);soso.setText(eatime[y]);
        question.setText("要吃"+eatime[x]+"還是"+eatime[y]);
    }
    public void send(){
        json_write = new JSONObject();
        try {
            json_write.put("action", "Question");
            if (b != null) {
                json_write.put("Longitude", b.getString("Longitude"));//經度
                json_write.put("Latitude",b.getString("Latitude"));//緯度
                json_write.put("Distlimit", b.getString("Distlimit"));
            }
            String[] like2 = new String[countlike];
            for(int i = 0;i < countlike ; i++){
                like2[i] = like[i];
            }
            JSONArray jlike= new JSONArray(like2);
            json_write.put("Like", jlike);
            String[] soso2;
            if(countsoso!=0){
                soso2 = new String[countsoso];
                for(int i = 0;i < countsoso ; i++){
                    soso2[i] = sosolike[i];
                }
            }else {
                soso2 = new String[1];
                soso2 [0] = "false";
            }
            //JSONArray jsoso= new JSONArray(soso2);
            //json_write.put("Soso", jsoso);
            String[] dont2;
            if(countdont!=0) {
                dont2 = new String[countdont];
                for (int i = 0; i < countdont; i++) {
                    dont2[i] = dontlike[i];
                }
            }else {
                dont2 = new String[1];
                dont2 [0] = "false";
            }
            JSONArray jdont= new JSONArray(dont2);
            json_write.put("Dont", jdont);
            soso.setVisibility(View.INVISIBLE);
            globalVariable.c.send(json_write);
            //String tmp = globalVariable.c.receive();
            //json_read = new JSONObject(tmp);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
