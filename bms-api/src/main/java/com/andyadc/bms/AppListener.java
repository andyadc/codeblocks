package com.andyadc.bms;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;

public class AppListener implements CommandLineRunner, DisposableBean {

	@Override
	public void run(String... args) throws Exception {
		System.out.println("应用启动成功，预加载相关数据");

	}

	@Override
	public void destroy() throws Exception {
		System.out.println("应用正在关闭，清理相关数据");

	}

}
