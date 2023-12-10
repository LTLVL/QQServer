package QQServer.service;

import common.Message;
import common.MessageType;
import common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {
    private ServerSocket ss = null;
    private static ConcurrentHashMap<String, User> validUses = new ConcurrentHashMap<>();

    static {
        validUses.put("100", new User("100", "123456"));
        validUses.put("200", new User("200", "123456"));
        validUses.put("300", new User("300", "123456"));
        validUses.put("400", new User("400", "123456"));
        validUses.put("500", new User("500", "123456"));
    }

    private boolean CheckUser(String id, String pd) {
        User user = validUses.get(id);
        if(user==null){
            System.out.println("用户不存在");
            return false;
        }
        else if(!user.getPasswd().equals(pd)){
            System.out.println("密码错误");
            return false;
        }
        else {
            return true;
        }
    }

    public QQServer() throws IOException {
        try {
            System.out.println("服务端在监听");
            new Thread(new AllMes()).start();
            ss = new ServerSocket(9999);
            while (true) {
                {
                    Socket socket = ss.accept();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    User user = (User) ois.readObject();
                    //简化数据库
                    Message message = new Message();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    if (CheckUser(user.getId(), user.getPasswd())) {
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                        oos.writeObject(message);
                        ServerConnect serverConnect = new ServerConnect(socket, user.getId());
                        serverConnect.start();
                        ManageClientThread.Add(user.getId(), serverConnect);
                        ConcurrentHashMap<String, Message> mes = ManageClientThread.getMes();
                        for (String key:mes.keySet()
                             ) {
                            if(Objects.equals(user.getId(), key)){
                                ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                                oos2.writeObject(mes.get(key));
                                System.out.println(user.getId()+"收到离线消息");
                            }
                        }
                    } else {
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                        System.out.println("登陆失败");
                        oos.writeObject(message);
                        socket.close();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            ss.close();
        }
    }
}
