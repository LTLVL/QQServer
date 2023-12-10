package org.zju.pojo;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1";
    String MESSAGE_LOGIN_FAIL = "2";
    String MESSAGE_COMM_MES = "3";
    String MESSAGE_GET_ONLINE_FRIEND = "4";//要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";//客户端请求推出
    String MESSAGE_CLIENT_PRIVATE_MESSAGE = "7";//客户端请求推出
    String MESSAGE_CLIENT_FILE = "8";//客户端请求推出

}