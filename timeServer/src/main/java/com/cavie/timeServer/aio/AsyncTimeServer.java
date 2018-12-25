package com.cavie.timeServer.aio;

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

	private CountDownLatch latch;
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
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void doAccept() {
		serverSocketChannel.accept(this, new AcceptCompletionHanlder());
	}
}
