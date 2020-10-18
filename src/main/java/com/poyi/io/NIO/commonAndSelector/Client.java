package com.poyi.io.NIO.commonAndSelector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client {

    private static final int port = 8200;

    private static String address = "127.0.0.1";

    private Selector selector;

    private SocketChannel socketChannel;

    public void send(String message) throws IOException {
        init();
        byte[] bytes = message.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        handlerSelector(selector);
    }

    public void init() throws IOException {
        selector = Selector.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(address, port);
        socketChannel = SocketChannel.open(inetSocketAddress);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void handlerSelector(Selector selector){
        try {
            while (true){
                if(selector.select(1000)==0){
                    continue;
                }
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()){
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    if(selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int byteSize = socketChannel.read(byteBuffer);
                        if(byteSize>0){
                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.remaining()];
                            byteBuffer.get(bytes);
                            String response = new String(bytes, "UTF-8");
                            System.out.println("收到客户端的返回信息："+response);
                        }
                        socketChannel.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        /*Scanner scanner = new Scanner(System.in);
        Client client = new Client();
        while (scanner.hasNext()) {
            String message = scanner.next();
            System.out.println("发送消息");
            client.send(message);
        }*/
        Client client = new Client();
        client.send("12345");
    }
}
