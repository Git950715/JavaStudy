package com.poyi.io.netty.tcpStickUnpack.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 多启动几个客户端演示
 */
public class TCPStickUnpackClient {

    public void connect(String address, int port){
        NioEventLoopGroup client = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(client)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ClientChannelInitializer());

        try {
            ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
            for(int i=0;i<10;i++){
                ByteBuf byteBuf = Unpooled.copiedBuffer("客户端的消息"+i, CharsetUtil.UTF_8);
                channelFuture.channel().writeAndFlush(byteBuf);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("客户端启动异常:"+e.getMessage());
        } finally {
            client.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        TCPStickUnpackClient groupChatClient = new TCPStickUnpackClient();
        groupChatClient.connect("127.0.0.1", 7777);
    }

}
