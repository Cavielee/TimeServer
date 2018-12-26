package com.cavie.timeserver.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
*
*	@author		created by Cavielee
*	@date		2018年12月25日 下午3:35:50
*/
public class AsyncTimeServer implements Runnable{

	CountDownLatch latch;
	AsynchronousServerSocketChannel serverSocketChannel;
	
	public AsyncTimeServer(int port) {
		try {
			serverSocketChannel = AsynchronousServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		latch = new CountDownLatch(1);
		doAccept();
		try {
			// 防止由于异步操作而直接结束主线程
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void doAccept() {
		// 由于连接是异步处理，因此设置 AcceptCompletionHanlder 对异步操作完成后处理（成功或失败）。
		serverSocketChannel.accept(this, new AcceptCompletionHanlder());
	}
}
