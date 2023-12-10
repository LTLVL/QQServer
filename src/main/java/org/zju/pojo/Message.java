package org.zju.pojo;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String getter;
    private String content;
    private String sendTime;
    private String mesType;
    private String SenderPath;
    private String GetterPath;
    private byte[] fileBytes;
    private int fileLEN = 0;

    public String getSenderPath() {
        return SenderPath;
    }

    public void setSenderPath(String senderPath) {
        SenderPath = senderPath;
    }

    public String getGetterPath() {
        return GetterPath;
    }

    public void setGetterPath(String getterPath) {
        GetterPath = getterPath;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLEN() {
        return fileLEN;
    }

    public void setFileLEN(int fileLEN) {
        this.fileLEN = fileLEN;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
