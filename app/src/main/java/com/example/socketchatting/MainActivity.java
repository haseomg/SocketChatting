package com.example.socketchatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private String TAG = "[MainActiviy]";
    static Context context;

    private Handler handler;
    private InetAddress serverAddress;
    private Socket socket;
    private PrintWriter sendWriter;
    //    private String ip = "54.180.155.66";
//    private String host = "192.168.35.159";
    private String host = "127.0.0.1";
    private int port = 7777;

    TextView chatView, userTextView;
    EditText message;
    Button send;
    String userID, sendMsg, read;
    //    MyServer myServer;
    MyClient myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    } // onCreate END


    public void initialize() {
        context = this;
        chatView = findViewById(R.id.chatView);
        userTextView = findViewById(R.id.userTextView);
        message = findViewById(R.id.messageEditText);
        send = findViewById(R.id.sendButton);

        handler = new Handler();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userName");
        userTextView.setText(userID);
//        myServer = new MyServer();
//        chatList = new ArrayList<PrintWriter>();
//        setThread();
        setServer();
        setSend();
        myClient = new MyClient();
    } // initialize END

    void setServer() {
        new Thread() {
            @Override
            public void run() {
                ServerSocket serverSocket;
                try {
                    serverSocket = new ServerSocket(7777);
                    System.out.println("⎻⎻⎻⎻⎻ SERVER READY ⎻⎻⎻⎻⎻");
                    socket = serverSocket.accept();
                    System.out.println(TAG + "socket check : " + socket);
                    Sender sender = new Sender(socket);
                    System.out.println(TAG + "sender check : " + sender);
                    Receiver receiver = new Receiver(socket);
                    System.out.println(TAG + "Receiver class initialize check : " + receiver);

                    sender.start();
                    System.out.println(TAG + "sender start");
                    receiver.start();
                    System.out.println(TAG + "receiver start");
                } catch (IOException e) {
                    System.out.println(TAG + "⎻⎻⎻⎻⎻ ERROR ⎻⎻⎻⎻⎻ : " + e);
                } // catch END
            }
        }.start();
    } // setServer END


    public void setThread() {
        new Thread() {
            public void run() {
                Log.i(TAG, "setThread run Method()");
                try {
                    Log.i(TAG, "setThread run Method Try()");
                    serverAddress = InetAddress.getByName(host);
                    Log.i(TAG, "setThread serverAddress : " + serverAddress);
                    socket = new Socket(host, port);
                    Log.i(TAG, "setThread socket : " + socket.toString());
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    Log.i(TAG, "setThread sendWriterr : " + sendWriter.toString());
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                    myClient.connect(host, port);

                    while (true) {
                        Log.i(TAG, "setThread run Method while(true)");
                        read = input.readLine();
                        Log.i(TAG, "--------------- READ CHECK --------------- " + read);

                        if (read != null) {
                            Log.i(TAG, "setThread run Method while(true) if (read != null) : " + read);
                            handler.post(new msgUpdate(read));

                        } else {
                            Log.i(TAG, "read == null : " + read);
                        } // else END
                    } // while (true) END
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
                Log.i(TAG, "setSend Method()");
                sendMsg = message.getText().toString();
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
//                            try {
//                                Log.i(TAG, "setSend run Method sendWriter Check : " + sendWriter);
//                            } catch (NullPointerException e) {
//                                Log.e(TAG, "--------------- SET SEND NULL ERROR --------------- " + e);
//                            }  // catch END
                            // TODO. ERROR TIMING - sendWriter Null
                            Log.i(TAG, "sendWriter Check : " + sendWriter);
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
        try {
            sendWriter.close();
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "------- on stop ERROR -------  : " + e);
        }
    } // onStop End

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            if (socket != null) {
//                socket.close();
//            }
////            if (input !=null){
////                in.close();
////            }
//            if (sendWriter != null) {
//                sendWriter.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

    class Sender extends Thread {
        Socket socket;
        DataOutputStream out;
        String name;

        String TAG = "[Sender Class]";
        String constructor = "Sender Constructor";

        Sender(Socket socket) {
            Log.i(TAG, constructor);
            this.socket = socket;
            try {
                Log.i(TAG, constructor + "try");
                out = new DataOutputStream(socket.getOutputStream());
                Log.i(TAG, constructor + "out check : " + out);
                name = "[" + socket.getInetAddress() + ":" + socket.getPort() + "]";
                Log.i(TAG, constructor + "name check : " + name);
            } catch (IOException e) {
                Log.i(TAG, constructor + "ERROR CHECK : " + e);
            } // catch END
        } // Constructor END

        @Override
        public void run() {
            Log.i(TAG, "run Method");
            while (message.getText().toString() != null) {
                Log.i(TAG, "while (message.getText().toStringg != null");
                try {
                    Log.i(TAG, "try");
                    out.writeUTF(userID + "> " + message.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "ERROR CHECK : " + e);
                } // catck END
            } // while END
        } // run END
    } // Sender Class END

   class Receiver extends Thread {
        Socket socket;
        DataInputStream in;
        String TAG = "[Receiver Class]";

        Receiver(Socket socket) {
            try {
                System.out.println(TAG + "Receiver Constructor try");
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println(TAG + "⎻⎻⎻⎻⎻ Receiver Constructor catch ERROR ⎻⎻⎻⎻⎻ : " + e);
            } // catch END
        } // constructor END



       @Override
       public void run() {
           System.out.println(TAG + "run Method");
           while (in != null) {
               System.out.println(TAG + "while (in != null)");
               try {
                   System.out.println(TAG + "run Method try");
                   System.out.println("🚼Client : " + in.readUTF());
               } catch (IOException e) {
                   System.out.println(TAG + "⎻⎻⎻⎻⎻ run Method catch ERROR ⎻⎻⎻⎻⎻ : " + e);
               } // catch END
           } // while END
       } // run END

    } // Receiver CLASS END

} // CLASS END