package com.poyi.io.netty.groupChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class GroupChatClient {

    public void connect(String address, int port){
        NioEventLoopGroup client = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(client)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ClientChannelInitializer());

        try {
            ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                channelFuture.channel().writeAndFlush(message);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("客户端启动异常:"+e.getMessage());
        } finally {
            client.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();
        groupChatClient.connect("127.0.0.1", 7777);
    }

}
