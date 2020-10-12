package com.poyi.io.netty.tcpStickUnpack.solve;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * TCP粘包粘包解决Netty客户端
 */
public class TCPStickUnpackSolveClient {

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
                String result = "客户端的消息"+i;
                byte[] content = result.getBytes(CharsetUtil.UTF_8);
                MyMessage myMessage = new MyMessage();
                myMessage.setLength(content.length);
                myMessage.setContent(content);
                channelFuture.channel().writeAndFlush(myMessage);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("客户端启动异常:"+e.getMessage());
        } finally {
            client.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        TCPStickUnpackSolveClient groupChatClient = new TCPStickUnpackSolveClient();
        groupChatClient.connect("127.0.0.1", 7777);
    }

}
