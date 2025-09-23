package com.andyadc.codeblocks.test.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractNotifyService<T> implements NotifyService {

	@Override
	public void notify(String message) throws Exception {
		Class<T> notifyMessageClass = getNotifyMessage();
		System.out.println(notifyMessageClass);
	}

	public abstract void notify(T msg);

	private Class<T> getNotifyMessage() {
		Type type = this.getClass().getGenericSuperclass();
		Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
		return (Class) arguments[0];
	}
}
