package com.poyi.io.netty.dubboRpc.consumer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcClient {

    private static ClientChannelHandler clientChannelHandler;

    private static ExecutorService executor = Executors.newFixedThreadPool(
            1);

    public Object createProxy(Class<?> serviceClass, String providerName){
        System.out.println("创建了代理类对象");
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args)->{
                    if(clientChannelHandler==null){
                        System.out.println("连接了服务器");
                        connect("127.0.0.1", 7777);
                    }
                    clientChannelHandler.setParam(providerName + args[0]);
                    return executor.submit(clientChannelHandler).get();
                });
    }

    public void connect(String address, int port){
        clientChannelHandler = new ClientChannelHandler();
        NioEventLoopGroup client = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(client)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder(), new StringDecoder(),
                                clientChannelHandler);
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.connect(address, port).sync();
            //channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("客户端启动异常:"+e.getMessage());
        } finally {
            //client.shutdownGracefully();
        }
    }

}
