package org.zju.service;



import org.zju.pojo.Message;
import org.zju.pojo.MessageType;
import org.zju.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class AllMes implements Runnable {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入服务器要推送的新闻[输入exit退出该服务]");
            String mess = Utility.readString(1000);
            if(mess.equals("exit")){
                System.out.println("推送服务已退出");
                break;
            }
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_COMM_MES);
            message.setSender("服务器");
            message.setContent(mess);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人：" + mess);

            HashMap<String, ServerConnect> hm = ManageClientThread.getHm();
            for (String s : hm.keySet()) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(s).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
