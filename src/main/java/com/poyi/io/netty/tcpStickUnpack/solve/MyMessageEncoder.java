package com.poyi.io.netty.tcpStickUnpack.solve;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageEncoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        MyMessage myMessage = new MyMessage();
        myMessage.setLength(length);
        myMessage.setContent(bytes);
        out.add(myMessage);
    }
}
