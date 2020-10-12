package com.poyi.io.netty.commonServerClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyClient {

    private String address;
    private int port;

    public NettyClient(String address, int port){
        this.address = address;
        this.port = port;
    }

    private void connect(){
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
            ByteBuf byteBuf = Unpooled.copiedBuffer("客户端的消息", CharsetUtil.UTF_8);
            channelFuture.channel().writeAndFlush(byteBuf);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            nioEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 1234);
        nettyClient.connect();
    }

}
