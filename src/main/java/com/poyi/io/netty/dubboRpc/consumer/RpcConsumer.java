package com.poyi.io.netty.dubboRpc.consumer;

import com.poyi.io.netty.dubboRpc.consumer.client.RpcClient;
import com.poyi.io.netty.dubboRpc.publicInterface.UserService;

public class RpcConsumer {

    public static void main(String[] args) throws InterruptedException {
        RpcClient rpcClient = new RpcClient();
        UserService userService = (UserService) rpcClient.createProxy(
                UserService.class, "com.poyi.io.netty.dubboRpc.provider.UserServiceImpl#sayHello#");
        /*while(true){
            Thread.sleep(2000);
            System.out.println(userService.sayHello("爱奇艺"));
        }*/
        System.out.println(userService.sayHello("爱奇艺"));
    }

}
