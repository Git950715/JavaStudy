package com.poyi.io.BIO;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static int port = 6379;

    private static String address = "127.0.0.1";

    public void send(String address,int port,String message){
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            System.out.println("收到服务端的返回："+in.readLine());
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        while (scanner.hasNext()) {
            String key = scanner.next();
            client.send(address, port, key);
        }
    }

}
