package com.andyadc.codeblocks.test.rpc;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class RpcServiceRunnable implements Runnable {

	private final Socket client;
	private final Map<String, Object> service;

	public RpcServiceRunnable(Socket client, Map<String, Object> service) {
		this.client = client;
		this.service = service;
	}

	@Override
	public void run() {
		RpcResponse response = new RpcResponse();

		HessianInput hessianInput = null;
		HessianOutput hessianOutput = null;
		try (
			InputStream inputStream = client.getInputStream();
			OutputStream outputStream = client.getOutputStream()
		) {
			hessianInput = new HessianInput(inputStream);
			hessianOutput = new HessianOutput(outputStream);

			// 获取请求的具体信息，转换成 RPCRequest 对象
			Object object = hessianInput.readObject();
			if (!(object instanceof RpcRequest)) {
				setError(response, hessianOutput, "非法参数");
				return;
			}

			// 请求信息对象
			RpcRequest request = (RpcRequest) object;
			// 找到接口的实现类
			Object impl = service.get(request.getClassName());
			if (impl == null) {
				setError(response, hessianOutput, "没有对应的实现类");
				return;
			}

			Method method = impl.getClass().getMethod(request.getMethodName(), request.getParamsType());

			Object result = method.invoke(impl, request.getParams());
			response.setResult(result);
			hessianOutput.writeObject(response);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			setError(response, hessianOutput, "没有找到对应的方法");
		} catch (IllegalAccessException | InvocationTargetException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (hessianInput != null) {
					hessianInput.close();
				}
				if (hessianOutput != null) {
					hessianOutput.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setError(RpcResponse response, HessianOutput out, String error) {
		response.setError(new Exception(error));
		try {
			out.writeObject(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
