package com.andyadc.codeblocks.serialization.binary;

import com.andyadc.codeblocks.serialization.SerializerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * TODO
 * ByteArrayOutputStream 和 ByteArrayInputStream是伪装成流的字节数组，
 * 它们不会锁定任何文件句柄和端口，如果不再被使用，字节数组会被垃圾回收掉，所以不需要关闭。
 */
public class JDKSerializer {

	private static final Logger logger = LoggerFactory.getLogger(JDKSerializer.class);

	private JDKSerializer() {
	}

	public static <T> byte[] serialize(T object) {
		if (object == null) {
			throw new SerializerException("Object is null");
		}
		byte[] bytes;
		try (
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos)
		) {
			oos.writeObject(object);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			logger.error("JDKSerializer serialize error!", e);
			throw new SerializerException(e.getMessage(), e);
		}
		return bytes;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			throw new SerializerException("Bytes is null or empty");
		}
		Object object;
		try (
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais)
		) {
			object = ois.readObject();
		} catch (Exception e) {
			logger.error("JDKSerializer deserialize error!", e);
			throw new SerializerException(e.getMessage(), e);
		}
		return (T) object;
	}
}
