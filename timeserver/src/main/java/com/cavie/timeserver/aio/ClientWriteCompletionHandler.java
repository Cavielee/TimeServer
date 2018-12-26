package com.cavie.timeserver.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 客户端写异步结果的处理
 * 
 * @author created by Cavielee
 * @date 2018年12月26日 下午4:25:20
 */
public class ClientWriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
	private AsyncTimeClient client = null;
	private AsynchronousSocketChannel socketChannel = null;

	public ClientWriteCompletionHandler(AsyncTimeClient client) {
		this.client = client;
		this.socketChannel = client.socketChannel;
	}

	public void completed(Integer result, ByteBuffer attachment) {
		// 如果没有发送完则继续发送
		if (attachment.hasRemaining()) {
			socketChannel.write(attachment, attachment, this);
		} else {
			// 接受服务端发送的消息
			ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			socketChannel.read(readBuffer, readBuffer, new ClientReadCompletionHandler(client));
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
			client.latch.countDown();
		}
	}
}
