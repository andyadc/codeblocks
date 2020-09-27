package framework.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andy.an
 * @since 2018/12/5
 */
public class RabbitMQTest {

	private static final String HOST = "www.qq-server.com";
	private static final int PORT = 5672;
	private static final ConnectionFactory CONNECTION_FACTORY;
	private static final Connection connection;

	static {
		CONNECTION_FACTORY = getConnectionFactory();
		connection = getConnection();
	}

	private static Connection getConnection() {
		try {
			return CONNECTION_FACTORY.newConnection();
		} catch (Exception e) {
			throw new RuntimeException("Get connection error", e);
		}
	}

	private static ConnectionFactory getConnectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setPort(PORT);
		factory.setUsername("messager");
		factory.setPassword("123");
		factory.setVirtualHost("/");

		factory.setAutomaticRecoveryEnabled(true); // 网络异常自动连接恢复
		factory.setNetworkRecoveryInterval(10000); // 每10秒尝试重试连接一次

		Map<String, Object> clientProperties = new HashMap<>();
		clientProperties.put("email", "andaicheng@qq.com");
		factory.setClientProperties(clientProperties);

		return factory;
	}

	@Test
	public void testSend() throws Exception {
		String queue = "test";
		Channel channel = connection.createChannel();

		String message = "Hello RabbitMQ";
		channel.basicPublish("", queue, null, message.getBytes(StandardCharsets.UTF_8));

		channel.close();
		connection.close();
	}

	@Test
	public void testRecv() throws Exception {
		String queue = "test";
		Channel channel = connection.createChannel();

		channel.basicConsume(queue, true, (consumerTag, message) ->
				System.out.println(
					"message: " + new String(message.getBody(), StandardCharsets.UTF_8)
						+ ", consumerTag: " + consumerTag
				)
			, (consumerTag) -> {
			}
		);

		channel.close();
		connection.close();
	}

	@Test
	public void checkQueueExist() throws Exception {
		String queue = "test";
		try (Channel channel = connection.createChannel()) {
			channel.queueDeclarePassive(queue);
		} catch (Exception e) {
			System.out.println(queue + " 不存在!");
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	@Test
	public void testQueueDeclare() throws Exception {
		String queue = "test";
		Channel channel = connection.createChannel();
		channel.queueDeclare(
			queue, // 队列名称
			true, // 是否持久化
			false, // 是否排外
			false, // 是否自动删除
			new HashMap<>());

		channel.close();
		connection.close();
	}

	@Test
	public void testDeleteQueue() throws Exception {
		String queue = "test";
		Channel channel = connection.createChannel();
		channel.queueDelete(queue);

		channel.close();
		connection.close();
	}
}
