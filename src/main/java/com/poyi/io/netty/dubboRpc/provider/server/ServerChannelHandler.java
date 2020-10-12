package com.poyi.io.netty.dubboRpc.provider.server;

import com.poyi.io.netty.dubboRpc.provider.UserServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;


public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有通道注册了连接");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("收到客户端的消息：" + msg);
        String[] msgStr = msg.split("#");
        Class clazz = Class.forName(msgStr[0]);
        Method method = clazz.getMethod(msgStr[1], String.class);
        String result = (String) method.invoke(clazz.newInstance(), msgStr[2]);
        System.out.println("回复客户端的消息：" + result);
        ctx.channel().writeAndFlush(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("服务端发生异常："+cause.getMessage());
        ctx.close();
    }
}
