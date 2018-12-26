package com.cavie.timeserver.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 *	接入操作结果的处理
 * @author created by Cavielee
 * @date 2018年12月25日 下午3:46:16
 */
public class AcceptCompletionHanlder implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServer> {

	public void completed(AsynchronousSocketChannel result, AsyncTimeServer attachment) {
		attachment.serverSocketChannel.accept(attachment, this);
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		// 由于读取是异步处理，因此设置 ReadCompletionHandler 对异步操作完成后处理（成功或失败）。
		result.read(readBuffer, readBuffer, new ServerReadCompletionHandler(result));
	}

	public void failed(Throwable exc, AsyncTimeServer attachment) {
		exc.printStackTrace();
		// 设置栅栏，让主程序结束
		attachment.latch.countDown();
	}

}
