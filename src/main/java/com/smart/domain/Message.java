package com.smart.domain;

import cn.hutool.core.util.ArrayUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xizhonghuai
 * @date 2022/05/23
 */
@Getter
public class Message {
    public static char UPLOAD = 0x00;
    public static char READ = 0x01;
    public static char WRITE = 0x02;

    private final char functionCode;
    private final char commandType;
    private final char sensor;
    private final char sensorIndex;
    private String payLoad;


    public Message(char functionCode, char commandType, char sensor, char sensorIndex) {
        this.functionCode = functionCode;
        this.commandType = commandType;
        this.sensor = sensor;
        this.sensorIndex = sensorIndex;
    }

    public void setPayLoad(String data) {
        payLoad = data;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public byte[] getBuffer() {
        List<Byte> msgBuffer = new ArrayList<>();
        msgBuffer.add((byte) 0x68);
        msgBuffer.add((byte) functionCode);
        msgBuffer.add((byte) commandType);
        msgBuffer.add((byte) sensor);
        msgBuffer.add((byte) sensorIndex);
        msgBuffer.add((byte) (payLoad.length() / 256));
        msgBuffer.add((byte) (payLoad.length() % 256));
        byte[] payLoadBytes = payLoad.getBytes();
        for (byte b : payLoadBytes) {
            msgBuffer.add(b);
        }
        int sum = 0;
        for (byte b : msgBuffer) {
            sum = sum + b;
        }
        msgBuffer.add((byte) sum);
        Byte[] toArray = ArrayUtil.toArray(msgBuffer, Byte.class);
        byte[] bytes = new byte[toArray.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = toArray[i];
        }
        return bytes;
    }

    @Override
    public String toString() {
        return new String(getBuffer());
    }

    public static void main(String[] args) {
//        Message message = new Message((byte) 0X74, Message.UPLOAD, (byte) 0, (byte) 0);
//        message.setPayLoad("9999");
//        System.out.println(HexUtil.encodeHexStr(message.getData()));
    }
}
