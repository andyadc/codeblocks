package com.andyadc.codeblocks.test.socket.initial;

import com.andyadc.codeblocks.test.socket.Tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class Client {

	private static final int LOCAL_PORT = 20000;
	private static final int PORT = 20001;

	public static void main(String[] args) throws Exception {
		Socket socket = create();

		init(socket);

		socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 3000);

		System.out.println(">>>>>>>>");
		System.out.println("Client info: " + socket.getLocalAddress() + ", port:" + socket.getLocalPort());
		System.out.println("Server info: " + socket.getInetAddress() + ", port:" + socket.getPort());

		process(socket);

		socket.close();
		System.out.println("Client closed");
	}

	private static Socket create() throws Exception {
		Socket socket = new Socket();

		InetAddress inetAddress = Inet4Address.getLocalHost();
		SocketAddress socketAddress = new InetSocketAddress(inetAddress, LOCAL_PORT);
		socket.bind(socketAddress);

		return socket;
	}

	private static void init(Socket socket) throws Exception {
		// 读取超时时间
		socket.setSoTimeout(3000);

		// 是否复用未完全关闭的Socket地址，对于指定bind操作后的套接字有效
		socket.setReuseAddress(true);

		// 是否开启Nagle算法
		socket.setTcpNoDelay(true);

		// 是否需要在长时无数据响应时发送确认数据（类似心跳包），时间大约为2小时
		socket.setKeepAlive(true);

		// 对于close关闭操作行为进行怎样的处理；默认为false，0
		// false、0：默认情况，关闭时立即返回，底层系统接管输出流，将缓冲区内的数据发送完成
		// true、0：关闭时立即返回，缓冲区数据抛弃，直接发送RST结束命令到对方，并无需经过2MSL等待
		// true、200：关闭时最长阻塞200毫秒，随后按第二情况处理
		socket.setSoLinger(true, 200);

		// 是否让紧急数据内敛，默认false；紧急数据通过 socket.sendUrgentData(1);发送
		socket.setOOBInline(true);

		// 设置接收发送缓冲器大小 64k
		socket.setReceiveBufferSize(64 * 1024 * 1024);
		socket.setSendBufferSize(64 * 1024 * 1024);

		// 设置性能参数：短链接，延迟，带宽的相对重要性(权重)
		socket.setPerformancePreferences(1, 1, 0);
	}

	private static void process(Socket client) throws Exception {

		// 构建键盘输入流
//		InputStream in = System.in;
//		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		// 得到Socket输出流, 并转换为打印流
		OutputStream outputStream = client.getOutputStream();

		InputStream inputStream = client.getInputStream();
//		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		// 键盘读取一行
//			String str = reader.readLine();
		// 发送到服务器
//			printStream.println(str);

		byte[] buff = new byte[256];
		ByteBuffer byteBuffer = ByteBuffer.wrap(buff);

		// byte
		byteBuffer.put((byte) 126);

		// char
		char c = 125;
		byteBuffer.put((byte) c);

		// int
		int i = 5678221;
		byteBuffer.putInt(i);

		byteBuffer.putLong(Long.MIN_VALUE);

		byteBuffer.put("Hello 你好".getBytes());

//		outputStream.write(Tools.intToByteArray(123465));
		outputStream.write(buff, 0, byteBuffer.position() + 1);

		int read = inputStream.read(buff);
		if (read > 0) {
			System.out.println("Client received " + read + " - " + Tools.byteArrayToInt(buff));
		}

		outputStream.close();
		inputStream.close();
	}
}
