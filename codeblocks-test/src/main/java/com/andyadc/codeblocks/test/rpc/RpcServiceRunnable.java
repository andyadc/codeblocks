package com.andyadc.codeblocks.test.rpc;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class RpcServiceRunnable implements Runnable {

	private Socket client;
	private Map<String, Object> service;

	public RpcServiceRunnable(Socket client, Map<String, Object> service) {
		this.client = client;
		this.service = service;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		HessianInput hessianInput = null;
		HessianOutput hessianOutput = null;

		RpcResponse response = new RpcResponse();

		try {
			// 获取请求的流信息
			inputStream = client.getInputStream();
			hessianInput = new HessianInput(inputStream);

			// 准备响应相关的流和序列化技术
			outputStream = client.getOutputStream();
			hessianOutput = new HessianOutput(outputStream);

			// 获取请求的具体信息，转换成RPCRequest对象
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
			if (method == null) {
				setError(response, hessianOutput, "没有找到对应的方法");
				return;
			}

			Object result = method.invoke(impl, request.getParams());
			response.setResult(result);
			hessianOutput.writeObject(response);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				outputStream.close();
				hessianInput.close();
				hessianOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void setError(RpcResponse response, HessianOutput out, String error) {
		response.setError(new Exception(error));
		try {
			out.writeObject(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
