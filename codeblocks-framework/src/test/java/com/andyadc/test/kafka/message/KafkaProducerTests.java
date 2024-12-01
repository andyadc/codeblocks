package com.andyadc.test.kafka.message;

import com.andyadc.codeblocks.framework.kafka.message.KafkaMessageProducer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;

public class KafkaProducerTests extends MessageTests {

	public static void main(String[] args) throws Exception {

		// -UserLogin-
		UserLoginBody userLoginBody = new UserLoginBody();
		userLoginBody.setLoginTime(LocalDateTime.now());
		userLoginBody.setUserId(1L);
		userLoginBody.setUsername("u1");
		userLoginBody.setResult(true);

		UserLoginMessage userLoginMessage = new UserLoginMessage();
		userLoginMessage.setEventType("user-login");
		userLoginMessage.setBody(userLoginBody);

		// -UserRegister-
		UserRegisterBody userRegisterBody = new UserRegisterBody();
		userRegisterBody.setUsername("u1");
		userRegisterBody.setResult(true);
		userRegisterBody.setRegisterTime(LocalDateTime.now());

		UserRegisterMessage userRegisterMessage = new UserRegisterMessage();
		userRegisterMessage.setEventType("user-register");
		userRegisterMessage.setBody(userRegisterBody);

		// -UserBankAuth-
		UserBankAuthBody userBankAuthBody = new UserBankAuthBody();
		userBankAuthBody.setUsername("u1");
		userBankAuthBody.setUserId(1L);
		userBankAuthBody.setBankcardNo("6216");
		userBankAuthBody.setMobile("13933995533");
		userBankAuthBody.setResult(true);
		userBankAuthBody.setAuthTime(LocalDateTime.now());

		UserBankAuthMessage userBankAuthMessage = new UserBankAuthMessage();
		userBankAuthMessage.setEventType("user-bank-auth");
		userBankAuthMessage.setBody(userBankAuthBody);

		// -UserBankBindBody-
		UserBankBindBody userBankBindBody = new UserBankBindBody();
		userBankBindBody.setUsername("u1");
		userBankBindBody.setUserId(1L);
		userBankBindBody.setBankcardNo("6216");
		userBankBindBody.setResult(true);
		userBankBindBody.setBindTime(LocalDateTime.now());

		UserBankBindMessage userBankBindMessage = new UserBankBindMessage();
		userBankBindMessage.setEventType("user-bank-bind");
		userBankBindMessage.setBody(userBankBindBody);

		// --- context ---
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context-message-producer-kafka.xml");
		KafkaMessageProducer producer = ctx.getBean("kafkaMessageProducer", KafkaMessageProducer.class);

		for (int i = 0; i < 100; i++) {
			producer.syncSend("user-behavior", userRegisterMessage);
			producer.send("user-behavior", userLoginMessage);
			producer.send("user-bank-behavior", userBankAuthMessage);
			producer.syncSend("user-bank-behavior", userBankBindMessage);
		}


		printToClose(ctx);
	}

}
