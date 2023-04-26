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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private String TAG = "[MainActiviy]";
    private Handler handler;
    InetAddress serverAddress;
    Socket socket;
    PrintWriter sendWriter;

    private String ip = "http://54.180.155.66/";
    private int port = 8888;

    TextView messageTextView, userTextView;
    EditText message;
    Button send;
    String userID, sendMsg, read;
    MyClient myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    public void initialize() {
        messageTextView = findViewById(R.id.messageTextView);
        userTextView = findViewById(R.id.userTextView);
        message = findViewById(R.id.messageEditText);
        send = findViewById(R.id.sendButton);

        handler = new Handler();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userName");
        userTextView.setText(userID);
        setThread();
        setSend();
    }

    public void setThread() {
        new Thread() {
            public void run() {
                try {
                    serverAddress = InetAddress.getByName(ip);
                    socket = new Socket(serverAddress, port);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        read = input.readLine();
                        Log.i(TAG, "--------------- READ --------------- " + read);
                        if (read != null) {
                            handler.post(new msgUpdate(read));
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "--------------- Error --------------- " + e);
                } // catch End
            } // run End
        }.start();
    } // setThread End

    public void setSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg = message.getText().toString();
                Log.i(TAG, "sendMsg Check : " + sendMsg);
                new Thread() {
                    public void run() {
                        super.run();
                        try {
                            sendWriter.println(userID + ">" + sendMsg);
                            sendWriter.flush();
                            Log.i(TAG, "--------------- sendMsg Check --------------- " + sendMsg);
                            message.setText("");
                        } catch (Exception e) {
                            Log.e(TAG, "--------------- ERROR --------------- " + e);
                        } // catch End
                    } // run End
                }.start(); // new Thread End
            } // onClick End
        }); // setOnClickListener End
    } // setSend End

    @Override
    protected void onStop() {
        super.onStop();
        try {
            sendWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // onStop End


    class msgUpdate implements Runnable {
        private String msg;
        public msgUpdate(String read) {
            this.msg = read;
        }

        @Override
        public void run() {
            messageTextView.setText(messageTextView.getText().toString() + msg + "\n");
        }
    }
}