package tw.com.flag.eatwhat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_APPEND;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
//    private String[] Dataset;
    private JSONArray j1;
    private Button ebtn;
    private JSONObject json_read, json_write;
    private GlobalVariable globalVariable;
    private int[] c_f = new int[100];
    private int[] e_f = new int[100];

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView m1;
        public TextView p1;
        public TextView s1;
        public RatingBar r1;
        public Button e1;
        public Button c1;
        public ConstraintLayout row;
        public TextView match;

        public ViewHolder(View v) {
            super(v);
            m1 =(TextView) v.findViewById(R.id.m1);
            p1 =(TextView) v.findViewById(R.id.p1);
            s1 =(TextView) v.findViewById(R.id.s1);
            r1 =(RatingBar) v.findViewById(R.id.r1);
            e1 =(Button) v.findViewById(R.id.e1);
            c1 =(Button) v.findViewById(R.id.c1);
            row = v.findViewById(R.id.row);
            match = v.findViewById(R.id.match);

        }
    }
    public RecyclerViewAdapter(JSONArray j2) {
        j1 = j2;
    }
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu, parent, false);
        ViewHolder vh = new ViewHolder(v);
        globalVariable = (GlobalVariable) ContentSuggestAct.ActivityC.getApplicationContext().getApplicationContext();
        c_f[0]=0;
        e_f[0]=0;
        return vh;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
//        holder.mTextView.setText(Dataset[position]);
        try {
            JSONArray j2;
//            row = new TableRow[6];
//            for (int i = 0; i < 6; i++) { //動態產生TableRow
//                row[i] = new TableRow(ContentSuggestAct.this);
//                row[i].setBackgroundResource(R.drawable.ripple);
//                row[i].setId(i);
//                tblayout.addView(row[i]);
//            }
             //拆解接收的JSON包並製作表格顯示
                j2 = j1.getJSONArray(i);
                holder.row.setTag(j2.get(0).toString());
                holder.row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConstraintLayout t=(ConstraintLayout) v;
                        gotostore(t.getTag().toString());
                    }
                });
                holder.s1.setText(j2.get(1).toString());//店名
                holder.m1.setText(j2.get(6).toString());//菜名
                holder.r1.setRating(Float.parseFloat(j2.get(4).toString()));//星星
                holder.p1.setText("$ "+j2.get(7).toString());//價格
                holder.p1.setTag(j2.get(7).toString());
                holder.match.setText(j2.get(8).toString()+"%");
                holder.c1.setEnabled(true);
                for(int j=1 ; j<c_f[0]; j++){
                    if(i == c_f[j]) {
                        holder.c1.setEnabled(false);
                        break;
                    }
                }
                holder.c1.setId(i);
                holder.c1.setTag(j2.get(0).toString() + "," + j2.get(5).toString() + ",");
                holder.c1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        try {
                            FileOutputStream out = ContentSuggestAct.Acontext.openFileOutput(globalVariable.account+"think.txt", MODE_APPEND);
//                            String s = b.getTag().toString() + ((TextView) row[b.getId()].getChildAt(0)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(1)).getText().toString() + "," + ((TextView) row[b.getId()].getChildAt(2)).getText().toString() + ",";
                            String s = "4,"+b.getTag().toString() + holder.s1.getText().toString() + "," + holder.m1.getText().toString() + "," + holder.p1.getTag().toString() + ",";
                            out.write(s.getBytes());
                            out.close();

//                            b.setBackgroundTintList(getResources().getColorStateList(R.color.lightPink));
                            b.setEnabled(false);
                            c_f[0]++;
                            c_f[ c_f[0] ] = b.getId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                holder.e1.setId(i);
                holder.e1.setTag(j2.get(0).toString() + "," + j2.get(5).toString() + ",");
                holder.e1.setEnabled(true);
                for(int j=1 ; j<e_f[0]; j++){
                    if(i == e_f[j]) {
                        holder.e1.setEnabled(false);
                        break;
                    }
                }
                holder.e1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ebtn = (Button) v;
                        AlertDialog.Builder b = new AlertDialog.Builder(ContentSuggestAct.ActivityC);
                        //串聯呼叫法
                        b.setTitle("確認")
                                .setMessage("確定要吃這個嗎?")
                                .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            FileOutputStream out = ContentSuggestAct.Acontext.openFileOutput(globalVariable.account+"eat.txt", MODE_APPEND);
                                            String s;
                                            s = ebtn.getTag().toString() + holder.s1.getText().toString() + "," + holder.m1.getText().toString() + "," + holder.p1.getTag().toString() + ",";
                                            out.write(s.getBytes());
                                            out.close();
                                            ebtn.setEnabled(false);
                                            e_f[0]++;
                                            e_f[ e_f[0] ] = ebtn.getId();

                                            s=ebtn.getTag().toString().substring(ebtn.getTag().toString().indexOf(",")+1);
                                            s=s.substring(0,s.indexOf(","));
                                            json_write = new JSONObject();
                                            json_write.put("action", "eat");
                                            json_write.put("mid", Integer.parseInt(s));
                                            globalVariable.c.send(json_write);
                                            String tmp = globalVariable.c.receive();
                                            if(tmp != null) {
                                                json_read = new JSONObject(tmp);
                                                if (!json_read.getBoolean("check")) {
                                                    String reason;
                                                    reason = json_read.getString("data");
                                                    Toast.makeText(ContentSuggestAct.ActivityC, reason, Toast.LENGTH_SHORT).show();
                                                }else{
                                                    json_write = new JSONObject();
                                                    json_write.put("action", "eatLog");
                                                    json_write.put("Fid", 4);
                                                    globalVariable.c.send(json_write);
                                                }
                                            }else{
                                                AlertDialog.Builder b=new AlertDialog.Builder(ContentSuggestAct.ActivityC);
                                                //串聯呼叫法
                                                b.setCancelable(false);
                                                b.setTitle("警告")
                                                        .setMessage("連線逾時，請重新連線")
                                                        .setPositiveButton("連線", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {
                                                                    globalVariable.c.close();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Intent it = new android.content.Intent(ContentSuggestAct.ActivityC, MainActivity.class);
                                                                ContentSuggestAct.ActivityC.startActivity(it);
                                                                if(!Main2Activity.ActivityM.isFinishing()) Main2Activity.ActivityM.finish();
                                                                ContentSuggestAct.ActivityC.finish();
                                                            }
                                                        })       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                                        .setNegativeButton("離開", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try {
                                                                    globalVariable.c.close();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                if(!Main2Activity.ActivityM.isFinishing()) Main2Activity.ActivityM.finish();
                                                                ContentSuggestAct.ActivityC.finish();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })       //若只是要顯示文字窗，沒有處理事件，第二個參數為null
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                });

            }
         catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "ContentError=" + e.toString());
        }
    }
    @Override
    public int getItemCount() {
        return j1.length();
    }
    public void addItem(JSONArray j2, int index) {
        j1.put(j2);
        notifyItemInserted(index);
    }
    public void gotostore(String id){
        Bundle b = new Bundle();
        Intent i = new Intent(ContentSuggestAct.Acontext, StoreAct.class);
        b.putBoolean("mode", true);
        b.putString("datanum", id);
        b.putInt("Activity", 5);
        i.putExtras(b);
        ContentSuggestAct.ActivityC.startActivity(i);
    }
}
