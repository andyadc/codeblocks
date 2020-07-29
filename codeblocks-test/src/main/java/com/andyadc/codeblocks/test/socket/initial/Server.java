package com.andyadc.codeblocks.test.socket.initial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Server {

	private static final int PORT = 20001;

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = create();

		init(serverSocket);

		serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 10);

		System.out.println(">>>>>>>>");
		System.out.println("Server info: " + serverSocket.getInetAddress() + ", port:" + serverSocket.getLocalPort());

		for (; ; ) {
			Socket socket = serverSocket.accept();
			new Thread(new ClientHandler(socket)).start();
		}
	}

	private static ServerSocket create() throws Exception {
		ServerSocket serverSocket = new ServerSocket();

		return serverSocket;
	}

	private static void init(ServerSocket serverSocket) throws Exception {
		// 是否复用未完全关闭的地址端口
		serverSocket.setReuseAddress(true);
		// 等效Socket#setReceiveBufferSize
		serverSocket.setReceiveBufferSize(64 * 1024 * 1024);
		// 设置serverSocket#accept超时时间
//		serverSocket.setSoTimeout(3000);

		serverSocket.setPerformancePreferences(1, 1, 1);
	}

	private static class ClientHandler implements Runnable {

		private Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("New connect: " + socket.getInetAddress() + ", port:" + socket.getPort());
			try {
				OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = socket.getInputStream();

				byte[] buff = new byte[256];
				int read = inputStream.read(buff);
				ByteBuffer byteBuffer = ByteBuffer.wrap(buff, 0, read);
//				if (read > 0) {
//					System.out.println("Server received " + read + " - " + Tools.byteArrayToInt(buff));
//				}

				byte b = byteBuffer.get();

				System.out.println(b);


				outputStream.write(buff);

				outputStream.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Connect closed: " + socket.getInetAddress() + ", port:" + socket.getPort());
		}
	}
}
