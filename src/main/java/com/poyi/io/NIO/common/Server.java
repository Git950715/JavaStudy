package com.poyi.io.NIO.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Server {

    private List<SocketChannel> socketChannelList = new ArrayList<>();

    private static int port = 6380;

    public void start(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            if(socketChannel!=null){
                socketChannel.configureBlocking(false);
                socketChannelList.add(socketChannel);
            }
            handlerServerSocketChannel();
        }
    }

    private void handlerServerSocketChannel(){
        if(socketChannelList.size()>0){
            for(SocketChannel socketChannel : socketChannelList){
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    String request = null;
                    int readBytes = socketChannel.read(byteBuffer);
                    if(readBytes>0){
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        request = new String(bytes, "UTF-8");
                        System.out.println("收到客户端请求信息：" + request);
                        byteBuffer.clear();
                        String response = "客户端请求["+request+"]的服务端回复信息";
                        byte[] responseBytes = response.getBytes();
                        ByteBuffer writeBuffer = ByteBuffer.allocate(responseBytes.length);
                        writeBuffer.put(responseBytes);
                        writeBuffer.flip();
                        socketChannel.write(writeBuffer);
                        writeBuffer.clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start(port);
    }

}
