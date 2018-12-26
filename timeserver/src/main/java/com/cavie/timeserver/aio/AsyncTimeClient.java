package com.cavie.timeserver.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午3:51:40
 */
public class AsyncTimeClient implements Runnable {

	CountDownLatch latch = null;
	AsynchronousSocketChannel socketChannel = null;

	public AsyncTimeClient(int port) {
		super();
		try {
			socketChannel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		latch = new CountDownLatch(1);
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080), this, new ConnectCompletionHandler());
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
