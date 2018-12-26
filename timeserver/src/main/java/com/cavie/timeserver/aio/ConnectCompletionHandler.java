package com.cavie.timeserver.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

/**
 * 连接操作结果的处理
 * 
 * @author created by Cavielee
 * @date 2018年12月26日 下午4:08:45
 */
public class ConnectCompletionHandler implements CompletionHandler<Void, AsyncTimeClient> {

	public void completed(Void result, AsyncTimeClient attachment) {
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		attachment.socketChannel.write(writeBuffer, writeBuffer, new ClientWriteCompletionHandler(attachment));
	}

	public void failed(Throwable exc, AsyncTimeClient attachment) {
		try {
			attachment.socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
			attachment.latch.countDown();
		}
	}

}
