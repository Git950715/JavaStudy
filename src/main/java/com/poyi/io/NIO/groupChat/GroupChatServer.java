package com.poyi.io.NIO.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    private static int PORT = 1234;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        while (true) {
            if (selector.select(1000) == 0) {
                continue;
            }
            HandlerSelectorKeys(selector);
        }
    }

    private void HandlerSelectorKeys(Selector selector) throws IOException {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey = iterator.next();
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
                System.out.println(socketChannel.getRemoteAddress().toString().substring(1) + "上线了");
            }
            if (selectionKey.isReadable()) {
                HandlerSelectionKeyRead(selectionKey);
            }
            iterator.remove();
        }
    }

    private void HandlerSelectionKeyRead(SelectionKey selectionKey) {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String result = "";
        String message = null;
        String remoteAddress = null;
        try {
            int read;
            while ((read = socketChannel.read(byteBuffer)) > 0) {
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                result += new String(bytes);
                byteBuffer.clear();
            }
            remoteAddress = socketChannel.getRemoteAddress().toString().substring(1);
            if (read < 0) {
                System.out.println("[" + remoteAddress + "]下线了");
                socketChannel.close();
                return;
            }
            System.out.println("收到来自[" + remoteAddress + "]的消息[" + result + "]");
            message = remoteAddress + "的消息：" + result;
        } catch (IOException e) {
            System.out.println("收到来自[" + remoteAddress + "]的消息异常可能是掉线了");
        }
        try {
            //获取发送来的消息给其他的连接
            Set<SelectionKey> selectionKeys = selectionKey.selector().keys();
            for (SelectionKey s : selectionKeys) {
                if (s.channel() instanceof SocketChannel) {
                    SocketChannel channel = (SocketChannel) s.channel();
                    if (channel != socketChannel) {
                        channel.write(ByteBuffer.wrap(message.getBytes()));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("给连接上的客户端发消息失败了，异常原因：" + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
