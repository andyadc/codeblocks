package com.andyadc.codeblocks.test.rpc;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcServer {

	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
		5,
		10,
		60, TimeUnit.SECONDS,
		new ArrayBlockingQueue<>(100));

	private final List<String> classNames = new ArrayList<>();
	private volatile Boolean flag = true;

	private void scanPackage(String packageName) {
		URL url = this.getClass().getClassLoader().getResource(replaceTo(packageName));

		String urlFile = url.getFile();
		File file = new File(urlFile);
		String[] list = file.list();
		for (String path : list) {
			File eachFile = new File(urlFile + File.separator + path);
			if (eachFile.isDirectory()) {
				scanPackage(packageName + "." + eachFile.getName());
			}
			if (eachFile.getName().endsWith(".class")) {
				classNames.add(packageName + "." + eachFile.getName().replace(".class", ""));
			}
		}
	}

	private String replaceTo(String packageName) {
		return packageName.replaceAll("\\.", "/");
	}

	public Map<String, Object> getService(String packageNames) {
		// 封装所有服务提供者的service key:接口名的全限定类名  value：服务的具体实现类
		Map<String, Object> map = new ConcurrentHashMap<>();
		// 扫描指定的包 class的全限定类名封装在了classNames集合中
		String[] packageName = packageNames.split(",");
		for (String path : packageName) {
			scanPackage(path);
		}
		// 实例化服务的提供者
		if (classNames != null) {
			try {
				for (String className : classNames) {
					Class clazz = Class.forName(className);
					if (clazz.isAnnotationPresent(Rpc.class)) {
						// 获取接口
						Class[] interfaces = clazz.getInterfaces();
						// 实例化
						Object o = clazz.newInstance();
						// 将接口和对象放入map集合中
						map.put(interfaces[0].getName(), o);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public void start(int port, String packageName) {
		ServerSocket serverSocket = null;

		Map<String, Object> service = getService(packageName);
		// 创建ServerSocket链接
		try {
			serverSocket = new ServerSocket(port);
			while (flag) {
				// 获取客户端链接
				Socket accept = serverSocket.accept();
				System.out.println(">>>>>>>>");
				executor.execute(new RpcServiceRunnable(accept, service));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
				executor.shutdown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stop() {
		this.flag = false;
	}
}
