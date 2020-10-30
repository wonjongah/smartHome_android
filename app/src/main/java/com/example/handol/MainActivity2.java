package com.example.handol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {

    TextView recieveText;
    EditText editTextAddress;
    EditText editTextPort;
    EditText messageText;
    TextView rcvText;
    Button connectBtn, clearBtn, ledOnBtn, ledOffBtn;

    Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //앱 기본 스타일 설정
        getSupportActionBar().setElevation(0);

        connectBtn = (Button) findViewById(R.id.buttonConnect);
        clearBtn = (Button) findViewById(R.id.buttonClear);
        editTextAddress = (EditText) findViewById(R.id.addressText);
        editTextPort = (EditText) findViewById(R.id.portText);
        recieveText = (TextView) findViewById(R.id.textViewReciev);
        messageText = (EditText) findViewById(R.id.messageText);
        ledOnBtn = (Button) findViewById(R.id.btn_ledOn);
        ledOffBtn = (Button) findViewById(R.id.btn_ledOff);
        rcvText = (TextView) findViewById(R.id.txt_rcv);

        //connect 버튼 클릭
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), messageText.getText().toString());
                MyClientTask myClientTask = new MyClientTask("192.168.0.103", 8888, messageText.getText().toString());
                myClientTask.execute();
                //messageText.setText("");
            }
        });

        ledOnBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyClientTask myClientTask = new MyClientTask("192.168.0.103", 8888, "on");
                myClientTask.execute();
            }
        });

        ledOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyClientTask myClientTask = new MyClientTask("192.168.0.103", 8888, "off");
                myClientTask.execute();
            }
        });

        //clear 버튼 클릭
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask("192.168.0.103", 8888, "requesttemp");
                myClientTask.execute();
            }
        });


//        timer_start();

        
    }


    public void timer_start(){
        Timer timer = new Timer();

        TimerTask TT = new TimerTask(){
            @Override
            public void run(){
                // 반복실행할 구문
                // 라즈베리파이에게 요청
                // 받아온 데이터 textView에 업데이트
                MyClientTask2 myClientTask3 = new MyClientTask2("192.168.0.103", 8888, "requesttemp");

                myClientTask3.execute();
            }
        };
        timer.schedule(TT, 0, 1000);
        timer.cancel();

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor
        MyClientTask(String addr, int port, String message){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort);
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recieveText.setText(response);
            super.onPostExecute(result);
        }
    }



    public class MyClientTask2 extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor
        MyClientTask2(String addr, int port, String message){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort);
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            rcvText.setText(response);
            super.onPostExecute(result);
        }
    }

}


