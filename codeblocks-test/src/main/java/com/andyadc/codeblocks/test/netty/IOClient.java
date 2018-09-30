package com.andyadc.codeblocks.test.netty;

import java.net.Socket;
import java.util.Date;

/**
 * @author andy.an
 * @since 2018/9/30
 */
public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8888);
                while (true) {
                    socket.getOutputStream().write((new Date() + " hello world").getBytes());
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
            }
        }).start();
    }
}
