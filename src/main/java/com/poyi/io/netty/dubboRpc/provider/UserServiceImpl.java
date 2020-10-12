package com.poyi.io.netty.dubboRpc.provider;

import com.poyi.io.netty.dubboRpc.publicInterface.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public String sayHello(String msg) {
        return "经过处理的消息："+msg;
    }

}
