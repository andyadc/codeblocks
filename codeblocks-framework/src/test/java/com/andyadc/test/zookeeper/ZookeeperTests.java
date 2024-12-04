package com.andyadc.test.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperTests {

	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper(
			"localhost:2181",
			3000,
			watchedEvent -> System.out.println(watchedEvent.toString())
		);

		// 创建一个节点
		String path = "/test";

		Stat exists = zooKeeper.exists(path, false);
		System.out.println("exists: " + exists);

		byte[] data = "Zookeeper Example".getBytes();
		zooKeeper.create(path, data,
			ZooDefs.Ids.OPEN_ACL_UNSAFE,
			CreateMode.PERSISTENT,
			(rc, p, ctx, name) -> {
				System.out.println("create result - rc: " + rc + ", path: " + p + ", ctx=" + ctx + ", path: " + name);
			}, null
		);

		exists = zooKeeper.exists(path, false);
		System.out.println("exists: " + exists);

		// 获取节点数据
		byte[] nodeData = zooKeeper.getData(path, false, null);
		System.out.println("Node data: " + new String(nodeData));

		// 更新节点数据
		byte[] newData = "Updated Zookeeper Example".getBytes();
		zooKeeper.setData(path, newData, -1);

		// 删除节点
		zooKeeper.delete(path, -1);

		zooKeeper.close();
	}
}
