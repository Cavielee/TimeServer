package com.cavie.timeserver.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * 服务端读取操作结果的处理
 * 
 * @author created by Cavielee
 * @date 2018年12月26日 下午3:20:21
 */
public class ServerReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel socketChannel = null;

	public ServerReadCompletionHandler(AsynchronousSocketChannel result) {
		this.socketChannel = result;
	}

	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] bytes = new byte[attachment.remaining()];
		attachment.get(bytes);

		String body;
		try {
			body = new String(bytes, "UTF-8");
			System.out.println("Time server receive order : " + body);
			String currentTime = "QUERY TIME ORDER".equals(body) ? new Date(System.currentTimeMillis()).toString()
					: "BAD ORDER";
			doWrite(currentTime);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void doWrite(String response) {
		if (response != null && !response.trim().isEmpty()) {
			ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
			byte[] bytes = response.getBytes();
			writeBuffer.put(bytes);
			writeBuffer.flip();
			socketChannel.write(writeBuffer, writeBuffer, new ServerWriteCompletionHandler(socketChannel));
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
