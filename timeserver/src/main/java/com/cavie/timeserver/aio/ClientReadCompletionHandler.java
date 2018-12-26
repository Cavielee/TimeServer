package com.cavie.timeserver.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * 客户端读取操作结果的处理
 * 
 * @author created by Cavielee
 * @date 2018年12月26日 下午4:29:36
 */
public class ClientReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
	private AsyncTimeClient client = null;
	private AsynchronousSocketChannel socketChannel = null;

	public ClientReadCompletionHandler(AsyncTimeClient client) {
		this.client = client;
		this.socketChannel = client.socketChannel;
	}

	public void completed(Integer result, ByteBuffer attachment) {
		attachment.flip();
		byte[] bytes = new byte[attachment.remaining()];
		attachment.get(bytes);

		String body;
		try {
			body = new String(bytes, "UTF-8");
			System.out.println("Now is : " + body);
			client.latch.countDown();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
