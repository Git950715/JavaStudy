package com.poyi.io.netty.httpServerClient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class MyHttpHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg)
            throws Exception {
        if(msg instanceof HttpRequest){
            String url = ((HttpRequest) msg).uri();
            if("/favicon.ico".equals(url)){
                return;
            }
            String messageReturn = "服务器端返回";
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, Unpooled.copiedBuffer(messageReturn, CharsetUtil.UTF_8));
            fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8")
                    .set(HttpHeaderNames.CONTENT_LENGTH, messageReturn.getBytes().length);
            ctx.writeAndFlush(fullHttpResponse);
        }
    }
}
