package com.andyadc.codeblocks.test.socket.tcp_channel;

import com.andyadc.codeblocks.test.socket.CloseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {

	private final Socket socket;
	private final ClientReadHandler clientReadHandler;
	private final ClientWriteHandler clientWriteHandler;
	private final CloseNotify closeNotify;

	public ClientHandler(Socket socket, CloseNotify closeNotify) throws IOException {
		this.socket = socket;
		this.closeNotify = closeNotify;

		clientReadHandler = new ClientReadHandler(socket.getInputStream());
		clientWriteHandler = new ClientWriteHandler(socket.getOutputStream());
		System.out.println("新客户端连接：" + socket.getInetAddress() + " P:" + socket.getPort());
	}

	public void send(String str) {
		clientWriteHandler.send(str);
	}

	public void readToPrint() {
		clientReadHandler.start();
	}

	private void exitBySelf() {
		exit();
		closeNotify.onSelfClosed(this);
	}

	public void exit() {
		clientReadHandler.exit();
		clientWriteHandler.exit();
		CloseUtils.close(socket);
		System.out.println("客户端已退出: " + socket.getInetAddress() + " P:" + socket.getPort());
	}

	interface CloseNotify {
		void onSelfClosed(ClientHandler handler);
	}

	class ClientReadHandler extends Thread {
		private final InputStream inputStream;
		private boolean done = false;

		public ClientReadHandler(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		void exit() {
			done = true;
			CloseUtils.close(inputStream);
		}

		@Override
		public void run() {
			super.run();
			try {
				// 得到输入流，用于接收数据
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				do {
					// 客户端拿到一条数据
					String str = bufferedReader.readLine();
					if (str == null) {
						System.out.println("客户端已无法读取数据！");
						// 退出当前客户端
						ClientHandler.this.exitBySelf();
						break;
					}
					System.out.println(str);
				} while (!done);
			} catch (Exception e) {
				e.printStackTrace();
				if (!done) {
					System.out.println("连接异常断开");
					ClientHandler.this.exitBySelf();
				}
			} finally {
				CloseUtils.close(inputStream);
			}
		}
	}

	class ClientWriteHandler {
		private final PrintStream printStream;
		private final ExecutorService executorService;
		private boolean done = false;

		public ClientWriteHandler(OutputStream outputStream) {
			this.printStream = new PrintStream(outputStream);
			executorService = Executors.newSingleThreadExecutor();
		}

		void exit() {
			done = true;
			CloseUtils.close(printStream);
			executorService.shutdown();
		}

		void send(String s) {
			executorService.execute(new Writable(s));
		}

		class Writable implements Runnable {

			private final String msg;

			public Writable(String msg) {
				this.msg = msg;
			}

			@Override
			public void run() {
				if (ClientWriteHandler.this.done) {
					return;
				}
				try {
					ClientWriteHandler.this.printStream.println(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
