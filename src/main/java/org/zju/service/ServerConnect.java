package org.zju.service;


import org.zju.pojo.Message;
import org.zju.pojo.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnect extends Thread {
    private Socket socket;
    private String name;
    public boolean loop = true;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserName() {
        return name;
    }

    public void setUserid(String name) {
        this.name = name;
    }

    public ServerConnect(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    @Override
    public void run() {
        label:
        while (loop) {
            try {
                System.out.println("服务端和客户端" + name + "建立连接，正在读取数据");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                switch (message.getMesType()) {
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND -> {
                        System.out.println(message.getSender() + " 查询在线用户列表");
                        String allOnlineUser = ManageClientThread.gerAllOnlineUser();
                        Message message1 = new Message();
                        message1.setContent(allOnlineUser);
                        message1.setGetter(message.getSender());
                        message1.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message1);
                    }
                    case MessageType.MESSAGE_CLIENT_EXIT -> {
                        System.out.println("用户：" + message.getSender() + "退出系统");
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getServerConnectThread(message.getGetter()).socket.getOutputStream());
                        oos.writeObject(message);
                        socket.close();
                        ManageClientThread.Offline(message.getSender());
                        break label;
                    }
                    case MessageType.MESSAGE_CLIENT_PRIVATE_MESSAGE -> {
                        if (ManageClientThread.getServerConnectThread(message.getGetter()) == null) {
                            ManageClientThread.AddMes(message.getGetter(), message);
                            System.out.println("用户 " + message.getGetter() + "不在线，已发送留言");
                        } else {
                            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getServerConnectThread(message.getGetter()).socket.getOutputStream());
                            oos.writeObject(message);
                            System.out.println("用户 " + message.getSender() + "向用户 " + message.getGetter() + "发送了一条私聊消息");
                        }
                    }
                    case MessageType.MESSAGE_COMM_MES -> {
                        ManageClientThread.CommonMes(message);
                        System.out.println("用户 " + message.getSender() + " 群发了一条消息");
                    }
                    case MessageType.MESSAGE_CLIENT_FILE -> {
                        ObjectOutputStream oos = new ObjectOutputStream(ManageClientThread.getServerConnectThread(message.getGetter()).socket.getOutputStream());
                        oos.writeObject(message);
                        System.out.println("用户 " + message.getSender() + "向用户 " + message.getGetter() + "发送了文件");
                    }
                }
            } catch (IOException ignored) {

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
