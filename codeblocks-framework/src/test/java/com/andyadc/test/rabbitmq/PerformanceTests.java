package com.andyadc.test.rabbitmq;

import com.andyadc.codeblocks.framework.rabbitmq.ChannelPool;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class PerformanceTests {

	public static void main(String[] args) throws Exception {
		testThreadPool();
		testNormal();
	}

	private static void testNormal() throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		final Connection connection = connectionFactory.newConnection();

		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					while (true) {
						Channel channel = connection.createChannel();

						final String EXCHANGE_NAME = "dyh";
						channel.exchangeDeclare(EXCHANGE_NAME, "direct");

						//发送消息
						channel.basicPublish(EXCHANGE_NAME, "1", null, "s".getBytes());

						//接收消息
//                        String queueName = channel.queueDeclare().getQueue();
//                        channel.queueBind(queueName, EXCHANGE_NAME, "1");
//                        QueueingConsumer consumer = new QueueingConsumer(channel);
//                        channel.basicConsume(queueName, false, consumer);
//                        QueueingConsumer.Delivery delivery = consumer.nextDelivery(1);
//                        if (delivery != null) {
//                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                        }
//                        channel.basicCancel(consumer.getConsumerTag());

						//关闭资源
						channel.close();
					}
				} catch (Exception e) {

				}
			}).start();
		}

	}

	private static void testThreadPool() {
		final ChannelPool channelPool = new ChannelPool();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					while (true) {
						Channel channel = channelPool.getChannel();

						final String EXCHANGE_NAME = "dyh";
						channel.exchangeDeclare(EXCHANGE_NAME, "direct");

						//发送消息
						channel.basicPublish(EXCHANGE_NAME, "1", null, "s".getBytes());

						//接收消息
//                        String queueName = channel.queueDeclare().getQueue();
//                        channel.queueBind(queueName, EXCHANGE_NAME, "1");
//                        QueueingConsumer consumer = new QueueingConsumer(channel);
//                        channel.basicConsume(queueName, false, consumer);
//                        QueueingConsumer.Delivery delivery = consumer.nextDelivery(1);
//                        if (delivery != null) {
//                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                        }
//                        channel.basicCancel(consumer.getConsumerTag());

						//关闭资源
						channelPool.returnChannel(channel);
					}
				} catch (Exception e) {

				}
			}).start();
		}

	}
}
