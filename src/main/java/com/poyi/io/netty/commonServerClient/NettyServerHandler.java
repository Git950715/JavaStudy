package com.poyi.io.netty.commonServerClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"连接服务器");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到来自["+ctx.channel().remoteAddress()+"]发送的消息"+
                byteBuf.toString(CharsetUtil.UTF_8));

    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        System.out.println("读取客户端["+ctx.channel().remoteAddress()+"]完毕，回复消息。");
        ByteBuf byteBuf = Unpooled.copiedBuffer("回复客户端的消息", CharsetUtil.UTF_8);
        ctx.channel().writeAndFlush(byteBuf);
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    ByteBuf byteBuf = Unpooled.copiedBuffer("回复客户端的消息2", CharsetUtil.UTF_8);
                    ctx.channel().writeAndFlush(byteBuf);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                ByteBuf byteBuf = Unpooled.copiedBuffer("回复客户端的消息3", CharsetUtil.UTF_8);
                ctx.channel().writeAndFlush(byteBuf);
            }
        }, 5, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端"+ctx.channel().remoteAddress()+"异常，原因："+
                cause.getMessage());
        ctx.close();
    }
}
