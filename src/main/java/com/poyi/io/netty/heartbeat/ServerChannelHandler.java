package com.poyi.io.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.state().equals(IdleState.WRITER_IDLE)){
            System.out.println("服务器写空闲："+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else if(event.state().equals(IdleState.READER_IDLE)){
            System.out.println("服务器读空闲："+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else if(event.state().equals(IdleState.ALL_IDLE)){
            System.out.println("服务器读写都空闲："+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("客户端发送的消息：" + msg+";"+LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        ctx.channel().writeAndFlush("客户端["+ctx.channel().remoteAddress()+"]发送了消息:"+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
