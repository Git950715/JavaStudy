package com.poyi.io.NIO.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class Client {

    private static final int port = 6380;

    private static String address = "127.0.0.1";

    private SocketChannel socketChannel;

    public void send(String message) throws IOException {
        byte[] bytes = message.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        handlerServerWrite();
    }

    public void init() throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);
        socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);
    }

    private void handlerServerWrite(){
        try {
            while (true){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int byteSize = socketChannel.read(byteBuffer);
                if(byteSize>0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String response = new String(bytes, "UTF-8");
                    System.out.println("收到客户端的返回信息："+response);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        client.init();
        while (scanner.hasNext()) {
            String message = scanner.next();
            System.out.println("发送消息");
            client.send(message);
        }
    }
}
