package QQServer.service;

import common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

//用于管理和客户端通信的线程
public class ManageClientThread {
    private static HashMap<String, ServerConnect> hm = new HashMap<>();
    private static ConcurrentHashMap<String, Message> mes = new ConcurrentHashMap<>();
    public static HashMap<String, ServerConnect> getHm() {
        return hm;
    }

    public static ConcurrentHashMap<String, Message> getMes() {
        return mes;
    }

    public static void AddMes(String id, Message message) {
        mes.put(id, message);
    }
    public static void Add(String id, ServerConnect serverConnect) {
        hm.put(id, serverConnect);
    }

    public static void Offline(String id){
        hm.remove(id);
    }

    public static ServerConnect getServerConnectThread(String id) {
        return hm.get(id);
    }
    public static void CommonMes(Message message) throws IOException {
        for (String key:hm.keySet()) {
            if(!Objects.equals(key, message.getSender())){
                ObjectOutputStream oos = new ObjectOutputStream(hm.get(key).getSocket().getOutputStream());
                oos.writeObject(message);
            }
        }
    }

    public static int GetSize() {
        return hm.size();
    }



    public static String gerAllOnlineUser() {
        Iterator<String> iterator = hm.keySet().iterator();
        StringBuilder s = new StringBuilder();
        while (iterator.hasNext()) {
            s.append(iterator.next()).append(" ");
        }
        return s.toString();
    }
}
