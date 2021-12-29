package com.andyadc.codeblocks.serialization.binary;

import com.andyadc.codeblocks.serialization.SerializerException;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianSerializer {

	public static <T> byte[] serialize(T obj) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Hessian2Output output = new Hessian2Output(os);
		try {
			output.writeObject(obj);
			output.flush();
			return os.toByteArray();
		} catch (IOException e) {
			throw new SerializerException(e.getMessage(), e);
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Object deserialize(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		Hessian2Input input = new Hessian2Input(is);
		try {
			return input.readObject();
		} catch (IOException e) {
			throw new SerializerException(e.getMessage(), e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
