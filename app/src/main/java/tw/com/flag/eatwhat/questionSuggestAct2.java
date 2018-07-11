package tw.com.flag.eatwhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.view.View.VISIBLE;

public class questionSuggestAct2 extends AppCompatActivity {
    private GlobalVariable globalVariable;
    private JSONObject json_read, json_write;
    private String[] eatime =  {"早餐","點心","正餐","宵夜"};
    private String[] bd =  {"甜","辣","牛肉","豬肉","雞肉","海鮮","漢堡","麵食","炸","煎餅/蛋餅類","吐司","貝果","中式"};
    private String[] mn =  {"牛肉","豬肉","雞肉","羊肉","海鮮","漢堡","米食","鴨肉","粥","麵食","異國料理","咖哩","滷味","日/韓式","炸","鐵板","火鍋","餃子類","中式"};
    private String[] ans = {"牛肉炒飯","火腿炒飯","大麥克"};
    boolean bdroute = false , mnroute = false ,anstrue = false, First ;
    Button[] maintype,osn;
    String[] like;
    String[][] tmp1;
    String[] sosolike;
    String[] dontlike;
    Button OK , soso , NO ,bf ,ds,im,ne;
    TextView question;
    int questioncount , score ,countlike , countsoso , countdont,a,aa,eatype;
    Bundle b;
    String tmp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_suggest_act2);

        First=true;
        b = this.getIntent().getExtras();
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();
        OK = (Button)findViewById(R.id.button54);
        soso = (Button)findViewById(R.id.button55);
        NO = (Button)findViewById(R.id.button56);
        bf = (Button)findViewById(R.id.button57);
        ds = (Button)findViewById(R.id.button58);
        im = (Button)findViewById(R.id.button59);
        ne = (Button)findViewById(R.id.button60);
        questioncount = 0; score = 0;a=0;
        maintype = new Button[]{bf, ds, im, ne};
        osn = new Button[]{OK,soso,NO};
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
        question.setText("要吃哪類呢?");
        for(int i = 0 ; i < maintype.length; i++){
            final int ii = i;
            maintype[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(maintype[ii].getText().equals("早餐")||maintype[ii].getText().equals("點心")){
                        sbdroute(maintype[ii]);
                    }else if(maintype[ii].getText().equals("正餐")||maintype[ii].getText().equals("宵夜")){
                        smnroute(maintype[ii]);
                    }
                }
            });
        }
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(a==0) {
                        if (bdroute) {
                            like[countlike] = bd[questioncount];
                            score += 2;
                            questioncount++;
                            countlike++;
                            checkbdroute();
                        }
                        if (mnroute) {
                            like[countlike] = mn[questioncount];
                            score += 2;
                            countlike++;
                            questioncount++;
                            checkmnroute();
                        }
                    }else{
                        anstrue=true;
                        showans();
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
                    if (bdroute) {
                        sosolike[countsoso]=bd[questioncount];
                        countsoso++;
                        questioncount++;
                        score += 1;
                        checkbdroute();
                    }
                    if (mnroute) {
                        sosolike[countsoso] = mn[questioncount];
                        countsoso++;
                        questioncount++;
                        score += 1;
                        checkmnroute();
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
                    if(a==0) {
                        if (bdroute) {
                            dontlike[countdont] = bd[questioncount];
                            countdont++;
                            questioncount++;
                            checkbdroute();
                        }
                        if (mnroute) {
                            dontlike[countdont] = mn[questioncount];
                            countdont++;
                            questioncount++;
                            checkmnroute();
                        }
                    }else{
                        aa++;
                        showans();
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public String setquestiontype(String ww){
        String[] questiontype ={"要吃" + ww + "嗎?","吃" + ww + "好嗎?","要不要吃" + ww + "呢?","不如吃" + ww + "?"};
        int s =(int)(Math.random()*questiontype.length);
        return questiontype[s];
    }
    public void checkbdroute(){
        if(questioncount == 13){
            send();
        }
        if (score < 3 | questioncount < 4) {
            //setquestiontype(bd[questioncount]);
            question.setText(setquestiontype(bd[questioncount]));
        } else {
            send();
        }
    }
    public void checkmnroute(){
        if(questioncount == 19){
            send();
        }
        if (score < 3 | questioncount < 4) {
            question.setText(setquestiontype(mn[questioncount]));
        } else {
            send();
        }
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
    public void sbdroute(Button xx){
        bdroute = true;
        geteatype(xx);
        OK.setText("OK");soso.setText("還好");
        question.setText("要吃" + bd[questioncount] + "嗎?");
        score +=2;
        buttonstart();
    }
    public void smnroute(Button yy){
        mnroute = true;
        question.setText("要吃" + mn[questioncount] + "嗎?");
        geteatype(yy);
        OK.setText("OK");soso.setText("還好");
        score +=2;
        buttonstart();
    }
    public void buttonstart(){
        for(int i=0;i<maintype.length;i++){
            maintype[i].setVisibility(View.INVISIBLE);
        }
        for(int i=0;i<osn.length;i++){
            osn[i].setVisibility(VISIBLE);
        }
    }
    public void geteatype(Button zz){
        if(zz.getText().toString().equals("早餐")){
            eatype = 1;
        }
        if(zz.getText().toString().equals("點心")){
            eatype = 2;
        }
        if(zz.getText().toString().equals("正餐")){
            eatype = 3;
        }
        if(zz.getText().toString().equals("宵夜")){
            eatype = 4;
        }
    }
    public void showans(){
        if(!anstrue){
            if (aa<ans.length) {
                question.setText("要吃" + tmp1[aa][0] + "的"+ tmp1[aa][2]+"嗎?\n價格是"+ tmp1[aa][3]+"元");
            }else {
                aa=0;
                a = 0;
                countlike = 0;
                countdont = 0;
                score = 0;
                soso.setVisibility(View.VISIBLE);
                if (bdroute) {
                    if(questioncount == 13){
                        question.setText("你去吃土吧");
                    }else{
                        question.setText("要吃" + bd[questioncount] + "嗎?");
                    }
                }
                if(mnroute) {
                    if (questioncount == 19) {
                        question.setText("你去吃土吧");
                    } else {
                        question.setText("要吃" + mn[questioncount] + "嗎?");
                    }
                }
            }
        }else{
            Bundle b = new Bundle();
            Intent i;
            i = new Intent(this, randomSuggestRul.class);
            b.putInt("check",2);
            b.putString("data1", tmp1[aa][0]);
            b.putString("data2", tmp1[aa][1]);
            b.putString("data3", tmp1[aa][2]);
            b.putString("data4", tmp1[aa][3]);
            i.putExtras(b);
            startActivity(i);
            this.finish();
        }
    }
    public void send(){
        json_write = new JSONObject();
        try {
            json_write.put("action", "Question");
            json_write.put("First", First);
            if(First) {
                if (b != null) {
                    json_write.put("Longitude", Double.valueOf(b.getString("Longitude")));//經度
                    json_write.put("Latitude", Double.valueOf(b.getString("Latitude")));//緯度
                    json_write.put("Distlimit", Double.valueOf(b.getString("Distlimit")));
                }
                json_write.put("Eatype",eatype);
                First = false;
            }
            String[] like2 = new String[countlike];
            if(countlike!=0){
                for(int i = 0;i < countlike ; i++){
                    like2[i] = like[i];
                }
            }else{
                like2 = new String[1];
                like2 [0] = "false";
            }
            JSONArray jlike= new JSONArray(like2);
            json_write.put("Like", jlike);
           /* String[] soso2;
            if(countsoso!=0){
                soso2 = new String[countsoso];
                for(int i = 0;i < countsoso ; i++){
                    soso2[i] = sosolike[i];
                }
            }else {
                soso2 = new String[1];
                soso2 [0] = "false";
            }*/
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
            a=2;
            tmp = globalVariable.c.receive();
            json_read = new JSONObject(tmp);
            if (!json_read.getBoolean("check")) {
                String reason = json_read.getString("data");
                Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
            }else{
                JSONArray j1 = json_read.getJSONArray("data");
                JSONArray j2= new JSONArray();
                tmp1 = new String[j1.length()][5];
                for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並製作表格顯示
                    j2 = j1.getJSONArray(i);
                    for(int j = 0 ;j < 4;j++ ){
                        tmp1[i][j]=j2.get(j).toString();
                    }
                    ans[i] = j2.get(2).toString();
                }
                showans();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}