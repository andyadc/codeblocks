package com.andyadc.codeblocks.test.socket.tcp_channel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServer {

	private final int port;
	private ClientListener listener;
	private List<ClientHandler> clientHandlerList = new ArrayList<>();

	public TCPServer(int port) {
		this.port = port;
	}

	public boolean start() {
		try {
			listener = new ClientListener(port);
			listener.start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void stop() {
		if (listener != null) {
			listener.exit();
		}
		for (ClientHandler clientHandler : clientHandlerList) {
			clientHandler.exit();
		}
		clientHandlerList.clear();
	}

	public void broadcast(String str) {
		for (ClientHandler clientHandler : clientHandlerList) {
			clientHandler.send(str);
		}
	}

	class ClientListener extends Thread {
		private ServerSocket server;
		private boolean done = false;

		public ClientListener(int port) throws IOException {
			this.server = new ServerSocket(port);
			System.out.println("服务器信息：" + server.getInetAddress() + " P:" + server.getLocalPort());
		}

		@Override
		public void run() {
			super.run();

			System.out.println("服务器准备就绪～");
			do {
				Socket socket;
				try {
					socket = server.accept();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				try {
					// 客户端构建异步线程
					ClientHandler clientHandler = new ClientHandler(socket, (handler) -> clientHandlerList.remove(handler));

					// 读取数据并打印
					clientHandler.readToPrint();
					clientHandlerList.add(clientHandler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (!done);
		}

		void exit() {
			done = true;
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
