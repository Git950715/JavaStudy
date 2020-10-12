package com.poyi.io.NIO.groupChat;

import com.poyi.io.BIO.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private static String ADDRESS = "127.0.0.1";
    private static int PORT = 1234;
    private SocketChannel socketChannel;
    private Selector selector;

    public GroupChatClient(){
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(ADDRESS, PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handlerSelector(selector);
                    } catch (IOException e) {
                        System.out.println("另起一个线程异常");
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws IOException {
        byte[] bytes = message.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
    }

    private void handlerSelector(Selector selector) throws IOException {
            while (true){
                if(selector.select(1000)==0){
                    continue;
                }
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()){
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    if(selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int byteSize = socketChannel.read(byteBuffer);
                        if(byteSize>0){
                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            String response = new String(bytes);
                            System.out.println("收到客户端的返回信息："+response);
                        }
                    }
                    selectionKeyIterator.remove();
                }
            }

    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        GroupChatClient groupChatClient = new GroupChatClient();
        while (scanner.hasNext()) {
            String key = scanner.next();
            groupChatClient.sendMessage(key);
        }
    }

}
