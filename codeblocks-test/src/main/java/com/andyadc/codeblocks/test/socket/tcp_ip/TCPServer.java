package com.andyadc.codeblocks.test.socket.tcp_ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	private final int port;
	private ClientListener listener;

	public TCPServer(int port) {
		this.port = port;
	}

	public boolean start() {
		try {
			listener = new ClientListener(port);
			listener.start();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void stop() {
		if (listener != null) {
			listener.exit();
		}
	}

	private static class ClientListener extends Thread {
		private ServerSocket serverSocket;
		private boolean done = false;

		private ClientListener(int port) throws IOException {
			serverSocket = new ServerSocket(port);
			System.out.println("Server info: " + serverSocket.getInetAddress() + " P:" + serverSocket.getLocalPort());
		}

		@Override
		public void run() {
			super.run();
			System.out.println("Server is ready.");

			do {
				Socket client;

				try {
					client = serverSocket.accept();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				ClientHandler clientHandler = new ClientHandler(client);
				clientHandler.start();
			} while (!done);

			System.out.println("服务器已关闭！");
		}

		void exit() {
			done = true;
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class ClientHandler extends Thread {
		private Socket socket;
		private boolean flag = true;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			super.run();
			System.out.println("新客户端连接: " + socket.getInetAddress() + " P:" + socket.getPort());

			try {
				// 得到打印流，用于数据输出；服务器回送数据使用
				PrintStream printStream = new PrintStream(socket.getOutputStream());
				// 得到输入流，用于接收数据
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				do {
					// 客户端拿到一条数据
					String str = bufferedReader.readLine();
					if ("bye".equalsIgnoreCase(str)) {
						flag = false;
						printStream.println("bye");
					} else {
						System.out.println(str);
						printStream.println("回送: " + str.length());
					}

				} while (flag);

				printStream.close();
				bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("客户端已退出: " + socket.getInetAddress() + " P:" + socket.getPort());
		}
	}
}
