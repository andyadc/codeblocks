package com.andyadc.codeblocks.test.socket.tcp_channel;

import com.andyadc.codeblocks.test.socket.CloseUtils;
import com.andyadc.codeblocks.test.socket.upd_ip.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {

	public static void linkWith(ServerInfo info) throws IOException {
		Socket socket = new Socket();
		socket.setSoTimeout(3000);

		socket.connect(new InetSocketAddress(InetAddress.getByName(info.getAddress()), info.getPort()), 3000);

		System.out.println("已发起服务器连接，并进入后续流程～");
		System.out.println("客户端信息: " + socket.getLocalAddress() + " P:" + socket.getLocalPort());
		System.out.println("服务器信息: " + socket.getInetAddress() + " P:" + socket.getPort());

		try {
			ReadHandler readHandler = new ReadHandler(socket.getInputStream());
			readHandler.start();

			// 发送接收数据
			write(socket);

			readHandler.exit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		socket.close();
		System.out.println("客户端已退出～");
	}

	private static void write(Socket socket) throws IOException {
		InputStream in = System.in;
		BufferedReader input = new BufferedReader(new InputStreamReader(in));

		OutputStream outputStream = socket.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);

		do {
			// 键盘读取一行
			String str = input.readLine();
			// 发送到服务器
			printStream.println(str);

			if ("exit".equalsIgnoreCase(str)) {
				break;
			}
		} while (true);

		// 资源释放
		printStream.close();
	}

	static class ReadHandler extends Thread {
		private final InputStream inputStream;
		private boolean done = false;

		public ReadHandler(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		@Override
		public void run() {
			super.run();

			try {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

				do {
					String str;
					try {
						// 客户端拿到一条数据
						str = bufferedReader.readLine();
					} catch (SocketTimeoutException e) {
						continue;
					}
					if (str == null) {
						System.out.println("连接已关闭，无法读取数据！");
						break;
					}
					// 打印到屏幕
					System.out.println(str);
				} while (!done);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				CloseUtils.close(inputStream);
			}
		}

		void exit() {
			done = true;
			CloseUtils.close(inputStream);
		}
	}
}
