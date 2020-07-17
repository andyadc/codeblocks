package com.andyadc.codeblocks.test.rpc;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RpcClient {

	public Object start(RpcRequest request, String host, int port) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		HessianInput hessianInput = null;
		HessianOutput hessianOutput = null;

		try {
			Socket socket = new Socket(host, port);
			// 封装请求信息
			outputStream = socket.getOutputStream();
			hessianOutput = new HessianOutput(outputStream);
			// 发起请求
			hessianOutput.writeObject(request);

			// 获取响应信息
			inputStream = socket.getInputStream();
			hessianInput = new HessianInput(inputStream);

			Object object = hessianInput.readObject();
			if (!(object instanceof RpcResponse)) {
				throw new RuntimeException("服务器参数格式不正确");
			}
			RpcResponse response = (RpcResponse) object;
			// 判断服务器是否存在异常
			if (response.getError() != null)
				throw new RuntimeException("服务器繁忙");
			return response.getResult();
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
		return null;
	}
}
