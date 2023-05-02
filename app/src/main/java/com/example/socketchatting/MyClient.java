package com.example.socketchatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

//    public void connect(String ipAddress, int port) throws IOException {
//        // 서버와 연결
//        socket = new Socket(ipAddress, port);
//        // 입출력 스트림 설정
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//    } // connect END
//
//    public void sendMsg(String msg) {
//        // 서버로 메시지 전송
//        out.println(msg);
//        out.flush();
//    } // sendMsg END
//
//    public String receiveMsg() throws IOException {
//        // 서버에서 메시지 수신
//        String msg = in.readLine();
//        return msg;
//    } // receiveMsg END
//
//    public void disconnect() throws IOException {
//        // 연결 해제
//        in.close();
//        out.close();
//        socket.close();
//    } // disconnect END

    public static void main(String[] args) {
        BufferedReader in = null;
        PrintWriter out = null;

        Socket socket = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket("127.0.0.1", 8888);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            while(true) {
                System.out.print("전송하기>>> ");
                String outputMessage = scanner.nextLine();
                out.println(outputMessage);
                out.flush();
                if ("quit".equalsIgnoreCase(outputMessage)) break;

                String inputMessage = in.readLine();
                System.out.println("From Server : " + inputMessage);
                if ("quit".equalsIgnoreCase(inputMessage)) break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                scanner.close();
                if (socket != null) socket.close();
                System.out.println("⏤⏤⏤⏤⏤ Server Connection End ⏤⏤⏤⏤⏤");
            } catch (IOException e) {
                System.out.println("⏤⏤⏤⏤⏤ Socket Error ⏤⏤⏤⏤⏤");
            }
        }
    }
} // CLASS END
