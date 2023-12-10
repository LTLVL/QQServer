package org.zju.service;



import org.zju.pojo.Message;
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

    public static void AddMes(String name, Message message) {
        mes.put(name, message);
    }
    public static void Add(String name, ServerConnect serverConnect) {
        hm.put(name, serverConnect);
    }

    public static void Offline(String name){
        hm.remove(name);
    }

    public static ServerConnect getServerConnectThread(String name) {
        return hm.get(name);
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
