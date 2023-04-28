package com.example.socketchatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MyServer {
    private int port;
    private ServerSocket serverSocket;
    private MyClient myClient;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static ArrayList<PrintWriter> outList;

    public static void main(String[] args) {
        outList = new ArrayList<PrintWriter>();

//        int count = 0;
//        Thread thread[] = new Thread[10];

        try {
            ServerSocket s_socket = new ServerSocket(8888);
            while (true) {
                Socket c_socket = s_socket.accept();
                ClientManagerThread c_thread = new ClientManagerThread();
                c_thread.setSocket(c_socket);

                outList.add(new PrintWriter(c_socket.getOutputStream()));
                System.out.println("outList size check : " + outList.size());
                c_thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } // catch END
    } // Main Method END

//    public MyServer(int port) {
//        this.port = port;
//    }

//    public void start(int port) throws IOException {
//        // 서버 소켓 생성 및 바인딩
//        serverSocket = new ServerSocket(port);
//        // 클라이언트 연결 대기
//        socket = serverSocket.accept();
//        // 입출력 스트림 설정
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//    }
//
//    public String receiveMsg() throws IOException {
//        // 클라이언트에서 메시지 수신
//        String msg = in.readLine();
//        return msg;
//    }
//
//    public void sendMessage(String message) {
//        // 클라이언트로 메시지 전송
//        out.println(message);
//        out.flush();
//    }
//
//    public void stop() throws IOException {
//        // 연결 해제
//        in.close();
//        out.close();
//        socket.close();
//        serverSocket.close();
//    }

//    public static void main(String[] args) {
//        BufferedReader in = null;
//        PrintWriter out = null;
//
//        ServerSocket serverSocket = null;
//        Socket socket = null;
//        Scanner scanner = new Scanner(System.in);
//
//        try {
//            serverSocket = new ServerSocket(8000);
//
//            System.out.println("[Server실행] Client연결대기중...");
//            socket = serverSocket.accept();			// 연결대기
//
//            System.out.println("Client 연결됨.");
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            out = new PrintWriter(socket.getOutputStream());
//
//            while(true) {
//                String inputMessage = in.readLine();	// 수신데이터 한줄씩 읽기
//                if ("quit".equalsIgnoreCase(inputMessage)) break;
//
//                System.out.println("From Client: " + inputMessage);
//                System.out.print("전송하기>>> ");
//
//                String outputMessage = scanner.nextLine();
//                out.println(outputMessage);
//                out.flush();
//                if ("quit".equalsIgnoreCase(outputMessage)) break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                scanner.close();		// Scanner 닫기
//                socket.close();			// Socket 닫기
//                serverSocket.close();		// ServerSocket 닫기
//                System.out.println("연결종료");
//            } catch (IOException e) {
//                System.out.println("소켓통신에러");
//            }
//        }
//    }
} // CLASS END
