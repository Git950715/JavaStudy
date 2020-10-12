package com.poyi.io.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {

    private static int port = 6380;

    public void start(int port) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        handlerServerSelector(selector);
    }

    private void handlerServerSelector(Selector selector){
        try {
            while (true){
                if(selector.select(1000)==0){
                    continue;
                }
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while(selectionKeyIterator.hasNext()){
                    SelectionKey selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();
                    handlerSelectorKey(selectionKey, selector);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlerSelectorKey(SelectionKey selectionKey,Selector selector) throws IOException {
        if(selectionKey.isAcceptable()){
            System.out.println("服务端收到连接请求");
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
        if(selectionKey.isReadable()){
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String request = null;
            int readBytes = socketChannel.read(byteBuffer);
            if(readBytes>0){
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                request = new String(bytes, "UTF-8");
                System.out.println("收到客户端请求信息：" + request);
                String response = "客户端请求["+request+"]的服务端回复信息";
                byte[] responseBytes = response.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(responseBytes.length);
                writeBuffer.put(responseBytes);
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start(port);
    }

}
