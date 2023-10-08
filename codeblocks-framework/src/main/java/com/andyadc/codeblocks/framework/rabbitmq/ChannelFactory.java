package com.andyadc.codeblocks.framework.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChannelFactory implements PooledObjectFactory<Channel> {

	private final Connection connection;

	public ChannelFactory() {
		this(null);
	}

	public ChannelFactory(String uri) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			if (uri != null) {
				factory.setUri(uri);
			}
			connection = factory.newConnection();
		} catch (Exception e) {
			throw new ChannelException("连接失败", e);
		}
	}

	@Override
	public void activateObject(PooledObject<Channel> p) throws Exception {

	}

	@Override
	public void destroyObject(PooledObject<Channel> p) throws Exception {
		final Channel channel = p.getObject();
		if (channel.isOpen()) {
			try {
				channel.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	@Override
	public PooledObject<Channel> makeObject() throws Exception {
		return new DefaultPooledObject<>(connection.createChannel());
	}

	@Override
	public void passivateObject(PooledObject<Channel> p) throws Exception {

	}

	@Override
	public boolean validateObject(PooledObject<Channel> p) {
		final Channel channel = p.getObject();
		return channel.isOpen();
	}
}
