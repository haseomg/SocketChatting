package com.example.socketchatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String TAG = "[MainActiviy]";
    private Handler handler;
    private InetAddress serverAddress;
    private Socket socket;
    private PrintWriter sendWriter;
    private ArrayList<PrintWriter> chatList;

    private String ip = "54.180.155.66";
    private String host = "192.168.35.159";
    private int port = 8888;

    TextView chatView, userTextView;
    EditText message;
    Button send;
    String userID, sendMsg, read;
    MyServer myServer;
    MyClient myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    } // onCreate END








    public void initialize() {
        chatView = findViewById(R.id.chatView);
        userTextView = findViewById(R.id.userTextView);
        message = findViewById(R.id.messageEditText);
        send = findViewById(R.id.sendButton);

        handler = new Handler();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userName");
        userTextView.setText(userID);
        myServer = new MyServer();
        chatList = new ArrayList<PrintWriter>();
        setThread();
        setSend();
//        myClient = new MyClient();
    } // initialize END

    public void setThread() {
        new Thread() {
            public void run() {
                Log.i(TAG, "setThread run Method()");
                try {
                    Log.i(TAG, "setThread run Method Try()");
                    serverAddress = InetAddress.getByName(host);
                    socket = new Socket(serverAddress, port);
                    Log.i(TAG, "setThread sendWriter create before : " + sendWriter);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    Log.i(TAG, "setThread sendWriter create after : " + sendWriter);
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    myClient.connect(host, port);
                    while (true) {
                        Log.i(TAG, "setThread run Method while(true)");
                        read = input.readLine();
                        Log.i(TAG, "--------------- READ CHECK --------------- " + read);
                        if (read != null) {
                            Log.i(TAG, "setThread run Method while(true) if (read != null) : " + read);
                            chatView.setText(read);
                            handler.post(new msgUpdate(read));
                        } else {
                            Log.i(TAG, "read == null : " + read);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "--------------- SET THREAD IOEXCEPTION ERROR --------------- " + e);
                } // catch End
            } // run End
        }.start();
    } // setThread End

    public void setSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg = message.getText().toString();
//                chatList.add(sendWriter);
                Log.i(TAG, "sendMsg Check 1 : " + sendMsg);

//                if (message.equals("") || message == null) {
//                    Toast.makeText(MainActivity.this, "메시지를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.i(TAG, "sendMsg Check 2 : " + sendMsg);
//                }

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.i(TAG, "setSend run Method");

                            Log.i(TAG, "setSend run Method initialize before 1");
                            serverAddress = InetAddress.getByName(ip);
                            Log.i(TAG, "setSend run Method initialize before 2");
//                            socket = new Socket(serverAddress, port);
                            Log.i(TAG, "setSend run Method initialize before 3");
//                            sendWriter = new PrintWriter(socket.getOutputStream());
//                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            Log.i(TAG, "setSend run Method initialize after");

                            try {
                                Log.i(TAG, "setSend run Method sendWriter Check : " + sendWriter);
                            } catch (NullPointerException e) {
                                Log.e(TAG, "--------------- SET SEND NULL ERROR --------------- " + e);
                            }
                            // TODO. ERROR TIMING - sendWriter Null
                            sendWriter.println(userID + "> " + sendMsg);
                            Log.i(TAG, userID + "> " + sendMsg);
                            sendWriter.flush();
                            Log.i(TAG, "sendWritier.flush afterr");
                            Log.i(TAG, "--------------- sendMsg Check --------------- " + sendMsg);
                            message.setText("");
                        } catch (Exception e) {
                            Log.e(TAG, "--------------- SET SEND EXCEPTION ERROR --------------- " + e);
                        } // catch End
                    } // run End
                }.start(); // new Thread End
            } // onClick End
        }); // setOnClickListener End
    } // setSend End

    @Override
    protected void onStop() {
        super.onStop();
//        try {
//            in.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch End
    } // onStop End

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) {
                socket.close();
            }
//            if (input !=null){
//                in.close();
//            }
            if (sendWriter != null) {
                sendWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class msgUpdate implements Runnable {
        private String msg;

        public msgUpdate(String read) {
            this.msg = read;
        } // msgUpdate END

        @Override
        public void run() {
            Log.i(TAG, "msgUpdate run Method()");
            chatView.setText(message.getText().toString() + msg + "\n");
        } // run END
    } // msgUpdate END

} // CLASS END