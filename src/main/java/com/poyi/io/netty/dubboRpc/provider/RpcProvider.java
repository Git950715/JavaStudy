package com.poyi.io.netty.dubboRpc.provider;

import com.poyi.io.netty.dubboRpc.provider.server.RpcServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcProvider {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        RpcServer groupChatServer = new RpcServer();
        groupChatServer.start(7777);
    }

}
