package tw.com.flag.eatwhat;


import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;

public class Client extends Thread {//2222222222222222222222222223
    private Socket clientSocket;        //客戶端的socket
    private BufferedWriter bw;            //取得網路輸出串流
    private BufferedReader br;            //取得網路輸入串流
    private String tmp;                    //做為接收時的緩存
    private JSONObject json_write;        //從java伺服器傳遞與接收資料的json
    private HandlerThread handlerThread;
    private Handler handler;
    private Boolean READ_OK=false; //先睡200，若沒收到資料
    private int conTime=0;
    private String IP;
    private int Port;
    public Client(String ip, int port){
        IP=ip;
        Port=port;
        
        handlerThread=new HandlerThread("name"); //宣告常駐工人(執行緒)，等待執行工作
        handlerThread.start();
        handler=new Handler(handlerThread.getLooper());
        handler.post(Connection);
    }
    public void send(JSONObject j) {
        json_write=new JSONObject();
        json_write=j;
        handler.post(send);
    }
    public String receive() throws Exception {
        handler.post(res);
        while(!READ_OK){ //控制接收資料，連線逾時
            Thread.sleep(100);
            conTime++;
            if(conTime==50){
                return null;
            }
        }
        READ_OK=false;
        return URLDecoder.decode(tmp, "utf-8");
    }
    private Runnable send=new Runnable() { //向Server傳送資料
        @Override
        public void run() {
            try {
                bw.write(json_write+"\n");
                bw.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private  Runnable res=new Runnable() { //向Server接收資料
        @Override
        public void run() {
            try {
                tmp=br.readLine();
                tmp=tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                READ_OK=true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
    private Runnable Connection=new Runnable(){ //進行TCP連線
        @Override
        public void run() {
            try{
                // IP為Server端
                clientSocket=new Socket(InetAddress.getByName(IP),Port);
                //取得網路輸出串流
                bw=new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                // 取得網路輸入串流
                br=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }catch(Exception e){
                e.printStackTrace();
                Log.e("text","IOException when connecting Server! Socket連線="+e.toString());
            }
        }
    };
    public void close() throws IOException {
        if(handlerThread!=null){
            handlerThread.quit();
        }
        if(handler!=null){
            handler.removeCallbacks(send);
        }
        bw.close();
        br.close();
        clientSocket.close();
    }
    public boolean isConnect(){
        if(clientSocket.isConnected()){
            return true;
        }else{
            return false;
        }
    }
}
