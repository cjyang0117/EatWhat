package tw.com.flag.eatwhat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class questionSuggestAct2 extends AppCompatActivity {
    private String[] eatime =  {"早餐","點心","正餐","宵夜"};
    private String[] bd =  {"甜","辣","牛肉","豬肉","雞肉","海鮮","漢堡","麵食","炸","煎餅/蛋餅類","吐司","貝果","中式"};
    private String[] mn =  {"牛肉","豬肉","雞肉","羊肉","海鮮","漢堡","米食","鴨肉","粥","麵食","異國料理","咖哩","滷味","日/韓式","炸","鐵板","火鍋","餃子類","中式"};
    boolean bdroute = false , mnroute = false ;
    Button OK , soso , NO;
    ArrayList<Integer> mybd = new ArrayList<Integer>();
    ArrayList<Integer> mymn = new ArrayList<Integer>();
    TextView question;
    int questioncount;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_suggest_act2);

        OK =(Button)findViewById(R.id.button54);
        soso =(Button)findViewById(R.id.button55);
        NO =(Button)findViewById(R.id.button56);
        questioncount=0;
        question = (TextView)findViewById(R.id.question);
        int x = (int)(Math.random()* 3);
        int y = (int)(Math.random()* 3);
        if(y == x){
            y = (int)(Math.random()* 3);
        }
        question.setText("要吃"+eatime[x]+"還是"+eatime[y]);
        OK.setText(eatime[x]);soso.setText(eatime[y]);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OK.getText().equals("早餐")||OK.getText().equals("點心")){
                    bdroute = true;
                    questioncount++;
                    OK.setText("OK");soso.setText("還好");
                }else if(OK.getText().equals("正餐")||OK.getText().equals("宵夜")){
                    mnroute = true;
                    questioncount++;
                    OK.setText("OK");soso.setText("還好");
                }
                if(bdroute){
                    int b =(int)(Math.random()*12);
                    question.setText("要吃"+bd[b]+"嗎?");
                }
                if(mnroute){
                    int m =(int)(Math.random()*18);
                    question.setText("要吃"+mn[m]+"嗎?");
                }
            }
        });
    }
    public void ILike(){

    }
    public void Soso(){
        questioncount++;
    }
    public void Notlike(){
        questioncount++;
    }
    public void gotoRandomSuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,randomSuggestAct.class);
        startActivity(it);
    }
    public void gotoQuestionSuggestAct(View v){
        android.content.Intent it = new android.content.Intent(this,questionSuggestAct.class);
        startActivity(it);
    }
    public void gotoRecordAct(View v){
        android.content.Intent it = new android.content.Intent(this,recordAct.class);
        startActivity(it);
    }
    public void gotoSearchAct(View v){
        android.content.Intent it = new android.content.Intent(this,SearchAct.class);
        startActivity(it);
    }
    public void gotoMain2Activity(View v){
        android.content.Intent it = new android.content.Intent(this,Main2Activity.class);
        startActivity(it);
    }
}
