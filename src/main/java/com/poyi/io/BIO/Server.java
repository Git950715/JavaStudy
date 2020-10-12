package com.poyi.io.BIO;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static int port = 6379;

    public void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true){
            Socket socket = serverSocket.accept();
            handlerServerSocket(socket);
        }

    }

    private void handlerServerSocket(Socket socket){
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String request = null;
            while((request = in.readLine())!=null){
                System.out.println("收到客户端请求的信息："+request);
                out.println("客户端消息["+request+"]的回复：服务端的回复");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if(out!=null){
                out.close();
                out = null;
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start(port);
    }

}
