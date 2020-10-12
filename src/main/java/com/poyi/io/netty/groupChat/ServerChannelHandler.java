package com.poyi.io.netty.groupChat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;


public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端["+ctx.channel().remoteAddress()+"]上线了");
        System.out.println("channelGroup 的数量："+channelGroup.stream().count());
        channelGroup.writeAndFlush("客户端["+ctx.channel().remoteAddress()+"]上线了");
        channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端["+ctx.channel().remoteAddress()+"]下线了");
        channelGroup.writeAndFlush("客户端["+ctx.channel().remoteAddress()+"]下线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("客户端发送消息：" + msg);
        channelGroup.stream().forEach((channel)->{
            if(channel!=ctx.channel()){
                channel.writeAndFlush("客户端["+ctx.channel().remoteAddress()+"]发送了消息:"+msg);
            }else{
                channel.writeAndFlush("自己的消息:"+msg);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
