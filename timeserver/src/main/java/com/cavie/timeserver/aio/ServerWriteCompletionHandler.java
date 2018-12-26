package com.cavie.timeserver.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 服务端写操作结果的处理
 * 
 * @author created by Cavielee
 * @date 2018年12月26日 下午3:42:07
 */
public class ServerWriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel socketChannel = null;

	public ServerWriteCompletionHandler(AsynchronousSocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public void completed(Integer result, ByteBuffer attachment) {
		// 如果没有发送完则继续发送
		if (attachment.hasRemaining()) {
			socketChannel.write(attachment, attachment, this);
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
