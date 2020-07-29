package com.andyadc.codeblocks.test.socket.tcp_ip;

import com.andyadc.codeblocks.test.socket.upd_ip.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {

	public static void linkWith(ServerInfo info) throws IOException {
		Socket socket = new Socket();
		socket.setSoTimeout(3000);

		socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000);

		System.out.println("已发起服务器连接，并进入后续流程～");
		System.out.println("Client info: " + socket.getLocalAddress() + " P:" + socket.getLocalPort());
		System.out.println("Server info: " + socket.getInetAddress() + " P:" + socket.getPort());

		process(socket);

		socket.close();
		System.out.println("客户端已退出～");
	}

	private static void process(Socket client) throws IOException {
		InputStream in = System.in;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

		OutputStream outputStream = client.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);

		InputStream inputStream = client.getInputStream();
		BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		boolean flag = true;
		do {
			String s = bufferedReader.readLine();
			printStream.println(s);

			String echo = socketBufferedReader.readLine();
			if ("bye".equalsIgnoreCase(echo)) {
				flag = false;
			} else {
				System.out.println(echo);
			}
		} while (flag);

		printStream.close();
		socketBufferedReader.close();
	}
}
