package org.zju.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zju.mapper.userMapper;
import org.zju.pojo.Message;
import org.zju.pojo.MessageType;
import org.zju.pojo.User;
import org.zju.utils.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {
    private ServerSocket ss = null;


    private boolean CheckUser(User user) throws IOException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        userMapper users = sqlSession.getMapper(userMapper.class);
        if (user.getPhone() != null && user.getAge() != null) {
            //注册用户
            User actulaUser = users.selectByName(user.getName());
            if (actulaUser != null) {
                return false;
            }
            users.saveUser(user);
            sqlSession.commit();
            return true;
        }
        User actulaUser = users.selectByName(user.getName());

        if (actulaUser == null) {
            System.out.println("用户不存在");
            return false;
        } else if (!user.getPassword().equals(actulaUser.getPassword())) {
            System.out.println("密码错误");
            return false;
        } else {
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
                    if (CheckUser(user)) {
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                        oos.writeObject(message);
                        ServerConnect serverConnect = new ServerConnect(socket, user.getName());
                        serverConnect.start();
                        ManageClientThread.Add(user.getName(), serverConnect);
                        ConcurrentHashMap<String, Message> mes = ManageClientThread.getMes();
                        for (String key : mes.keySet()
                        ) {
                            if (Objects.equals(user.getName(), key)) {
                                ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                                oos2.writeObject(mes.get(key));
                                System.out.println(user.getName() + "收到离线消息");
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
