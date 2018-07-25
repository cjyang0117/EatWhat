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
    String[] like;//放置OK的種類
    String[][] tmp1;//放置3個答案的資訊
    //String [] storea0,addressa0,sid0,sphone0,star0,storea1,addressa1,sid1,sphone1,star1,storea2,addressa2,sid2,sphone2,star2;
    String[] sosolike;//放置還好的種類
    String[] dontlike;//放置NO的種類
    String lat,lng;
    Button OK ,soso ,NO ,bf ,ds,im,ne;
    TextView question;//問題
    int questioncount , score ,countlike , countsoso , countdont,a,aa,eatype;
    Bundle b;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_suggest_act2);

        b = this.getIntent().getExtras();//接收經緯度
        globalVariable = (GlobalVariable) getApplicationContext().getApplicationContext();

        OK = (Button)findViewById(R.id.button54);
        soso = (Button)findViewById(R.id.button55);
        NO = (Button)findViewById(R.id.button56);
        bf = (Button)findViewById(R.id.button57);
        ds = (Button)findViewById(R.id.button58);
        im = (Button)findViewById(R.id.button59);
        ne = (Button)findViewById(R.id.button60);//按鍵設置
        question = (TextView)findViewById(R.id.question);

        score = 0;a=0;//問題記分初始為0
        countlike =0;countsoso =0; countdont=0;
        lat= b.getString("Latitude");lng =  b.getString("Longitude");
        maintype = new Button[]{bf, ds, im, ne};//早餐,點心,主餐,宵夜
        osn = new Button[]{OK,soso,NO};
        like = new String[19];
        sosolike = new String[19];
        dontlike = new String[19];

        setQuestion1();
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    checkquestion();
                    if(a==0) {
                        if (bdroute) {
                            like[countlike] = bd[questioncount];//儲存至喜歡陣列
                            score += 2;//分數+2
                            questioncount++;//問題+1
                            countlike++;//喜歡數+1
                            checkbdroute();//判斷是否繼續問問題
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
                    checkquestion();
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
                    checkquestion();
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
    public void setQuestion1(){
        question.setText("要吃哪類呢?");//第一個問題固定為問早餐,點心,主餐,宵夜(4選1)
        for(int i=0; i < bd.length; i ++){//洗問題順序
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
        First=true;//判斷是不是第一輪回答用
        questioncount=0;
        bdroute = false;
        mnroute = false;
        for(int i=0;i<maintype.length;i++){
            maintype[i].setVisibility(VISIBLE);
        }
        for(int i=0;i<osn.length;i++){
            osn[i].setVisibility(View.INVISIBLE);
        }
        for(int i = 0 ; i < maintype.length; i++){//依選擇選擇問題路線
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
    }
    public String setquestiontype(String ww){//問題型態變化設定
        String[] questiontype ={"要吃" + ww + "嗎?","吃" + ww + "好嗎?","要不要吃" + ww + "呢?","不如吃" + ww + "?"};
        int s =(int)(Math.random()*questiontype.length);
        return questiontype[s];
    }
    public void checkbdroute(){//早餐.點心問題線
        if(questioncount == 13){//問題問完的話向server取答案
            send();
        }
        else{
            if (score < 3 | questioncount < 4) {//如果問題數不為5的話繼續問，如問題數超過5分數不達3分繼續問
                question.setText(setquestiontype(bd[questioncount]));
            } else {
                send();
            }
        }
    }
    public void checkmnroute(){//主餐,宵夜問題線(同上)
        if(questioncount == 19){
            send();
        }else{
            if (score < 3 | questioncount < 4) {
                question.setText(setquestiontype(mn[questioncount]));
            } else {
                send();
            }
        }
    }
    public void sbdroute(Button xx){//早餐.點心問題判斷設定
        bdroute = true;
        geteatype(xx);
        OK.setText("OK");soso.setText("還好");
        question.setText("要吃" + bd[questioncount] + "嗎?");
        score +=2;
        buttonstart();
    }
    public void smnroute(Button yy){//主餐,宵夜問題判斷設定
        mnroute = true;
        question.setText("要吃" + mn[questioncount] + "嗎?");
        geteatype(yy);
        OK.setText("OK");soso.setText("還好");
        score +=2;
        buttonstart();
    }
    public void buttonstart(){//主種類按鍵隱藏，喜好按鍵顯示
        for(int i=0;i<maintype.length;i++){
            maintype[i].setVisibility(View.INVISIBLE);
        }
        for(int i=0;i<osn.length;i++){
            osn[i].setVisibility(VISIBLE);
        }
    }
    public void geteatype(Button zz){//取得主種類
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
    public void showans(){//顯示答案
        if(!anstrue){//判斷使用者對於推薦答案是否有按OK
            if (aa<tmp1.length) {//3次答案
                //question.setText("要吃" + ans[aa]);
                question.setText("要吃" + tmp1[aa][6] + "嗎?\n價格是"+ tmp1[aa][7]+"元");
            }else {
                return0();
                soso.setVisibility(View.VISIBLE);//還好鍵顯示
                if (bdroute) {//判斷問題是否問完
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
        }else{//當對於推薦答案點擊OK傳送該資料至答案頁面
            Bundle b = new Bundle();
            Intent i;
            i = new Intent(this, randomSuggestRul.class);
            b.putInt("check",2);
            /*if(aa==0){
                b.putStringArray("data1",storea0);
                b.putStringArray("data2",addressa0);
                b.putStringArray("data5",sid0);
                b.putStringArray("data6",sphone0);
                b.putStringArray("data7",star0);
            }else if(aa==1){
                b.putStringArray("data1",storea1);
                b.putStringArray("data2",addressa1);
                b.putStringArray("data5",sid1);
                b.putStringArray("data6",sphone1);
                b.putStringArray("data7",star1);
            }else if(aa==2){
                b.putStringArray("data1",storea2);
                b.putStringArray("data2",addressa2);
                b.putStringArray("data5",sid2);
                b.putStringArray("data6",sphone2);
                b.putStringArray("data7",star2);
            }*/
            b.putString("data1",tmp1[aa][1]);//店名
            b.putString("data2",tmp1[aa][2]);//地址
            b.putString("data5",tmp1[aa][0]);//店號
            b.putString("data6",tmp1[aa][3]);//電話
            b.putString("data7",tmp1[aa][4]);//星
            b.putString("data3", tmp1[aa][6]);//餐點
            b.putString("data4", tmp1[aa][7]);//價格
            b.putString("data8", tmp1[aa][5]);//菜號
            b.putString("Latitude2",lat);
            b.putString("Longitude2",lng);
            i.putExtras(b);
            startActivity(i);
            this.finish();
        }
    }
    public void checkquestion(){
        if(question.getText().equals("你去吃土吧")){
            setQuestion1();
        }
    }
    public void return0(){
        aa=0;//答案序規0
        a = 0;// 跳回問問題
        countlike = 0;//OK數歸0
        countsoso = 0; //都可以歸0
        countdont = 0;//NO數歸0
        score = 0;//分數歸0
    }
    public void send(){//送出使用者資料
        json_write = new JSONObject();
        try {
            json_write.put("action", "Question2");
            json_write.put("First", First);
            if(First) {
                if (b != null) {
                    json_write.put("Longitude", Double.valueOf(lng));//經度
                    json_write.put("Latitude", Double.valueOf(lat));//緯度
                    json_write.put("Distlimit", Double.valueOf(b.getString("Distlimit")));//距離限制
                }
                json_write.put("Eatype",eatype);//主要種類(早餐,點心,主餐,宵夜)
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
            JSONArray jsoso= new JSONArray(soso2);
            json_write.put("Soso", jsoso);
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
            String tmp = globalVariable.c.receive();
            json_read = new JSONObject(tmp);
            if(tmp!=null) {
                if (!json_read.getBoolean("check")) {//如果查無資料回到距離限制頁面，重新選擇
                    String reason = json_read.getString("data");
                    Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
                    android.content.Intent it = new android.content.Intent(this,questionSuggestAct.class);
                    startActivity(it);
                    this.finish();
                } else {//接收距離內答案
                    JSONArray j1 = json_read.getJSONArray("data");
                    JSONArray j2;
                    tmp1 = new String[j1.length()][8];
                    for (int i = 0; i < j1.length(); i++) { //拆解接收的JSON包並放入答案陣列
                        j2 = j1.getJSONArray(i);
                        for (int j = 0; j < 8; j++) {
                            tmp1[i][j] = j2.get(j).toString();
                        }
                        ans[i] = j2.get(6).toString();
                    }
                    showans();
                }
            }else{
                Toast.makeText(this, "連線逾時", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e) {
            e.printStackTrace();
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
}
