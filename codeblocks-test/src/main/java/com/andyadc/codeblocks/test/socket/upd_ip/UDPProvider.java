package com.andyadc.codeblocks.test.socket.upd_ip;

import com.andyadc.codeblocks.test.socket.ByteUtils;
import com.andyadc.codeblocks.test.socket.UDPConstants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UDPProvider {

	private static Provider PROVIDER_INSTANCE;

	public static void start(int port) {
		stop();
		String sn = UUID.randomUUID().toString();
		Provider provider = new Provider(sn, port);
		provider.start();
		PROVIDER_INSTANCE = provider;
	}

	public static void stop() {
		if (PROVIDER_INSTANCE != null) {
			PROVIDER_INSTANCE.exist();
			PROVIDER_INSTANCE = null;
		}
	}

	private static class Provider extends Thread {
		final byte[] buffer = new byte[128];
		private final byte[] sn;
		private final int port;
		private boolean done = false;
		private DatagramSocket ds = null;

		public Provider(String sn, int port) {
			this.sn = sn.getBytes();
			this.port = port;
		}

		@Override
		public void run() {
			super.run();
			System.out.println("UDPProvider Started.");

			try {
				// 监听20000 端口
				ds = new DatagramSocket(UDPConstants.PORT_SERVER);
				// 接收消息的Packet
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

				while (!done) {
					// 接收
					ds.receive(packet);

					// 打印接收到的信息与发送者的信息
					// 发送者的IP地址
					String clientIp = packet.getAddress().getHostAddress();
					int clientPort = packet.getPort();
					int clientDataLen = packet.getLength();
					byte[] clientData = packet.getData();

					boolean isValid = clientDataLen >= (UDPConstants.HEADER.length + 2 + 4) && ByteUtils.startsWith(clientData, UDPConstants.HEADER);

					System.out.println("UDPProvider receive form ip:" + clientIp + "\tport:" + clientPort + "\tdataValid:" + isValid);

					if (!isValid) {
						continue;
					}

					// 解析命令与回送端口
					int index = UDPConstants.HEADER.length;
					short cmd = (short) ((clientData[index++] << 8) | (clientData[index++] & 0xff));
					int responsePort = (((clientData[index++]) << 24) |
						((clientData[index++] & 0xff) << 16) |
						((clientData[index++] & 0xff) << 8) |
						((clientData[index] & 0xff)));
					// 判断合法性
					if (cmd == 1 && responsePort > 0) {
						// 构建一份回送数据
						ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
						byteBuffer.put(UDPConstants.HEADER);
						byteBuffer.putShort((short) 2);
						byteBuffer.putInt(port);
						byteBuffer.put(sn);
						int len = byteBuffer.position();
						// 直接根据发送者构建一份回送信息
						DatagramPacket responsePacket = new DatagramPacket(buffer,
							len,
							packet.getAddress(),
							responsePort);
						ds.send(responsePacket);
						System.out.println("UDPProvider response to:" + clientIp + "\tport:" + responsePort + "\tdataLen:" + len);
					} else {
						System.out.println("UDPProvider receive cmd nonsupport; cmd:" + cmd + "\tport:" + port);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
			System.out.println("UDPProvider Finished.");
		}

		private void close() {
			if (ds != null) {
				ds.close();
				ds = null;
			}
		}

		void exist() {
			done = true;
			close();
		}
	}
}
