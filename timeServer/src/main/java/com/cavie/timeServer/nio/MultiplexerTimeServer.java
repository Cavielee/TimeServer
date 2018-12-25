package com.cavie.timeServer.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 处理逻辑
 * 
 * @author created by Cavielee
 * @date 2018年12月25日 上午11:39:18
 */
public class MultiplexerTimeServer implements Runnable {
	private volatile boolean stop;
	private ServerSocketChannel serverChannel = null;
	private Selector selector = null;

	public MultiplexerTimeServer(int port) {
		try {
			selector = selector.open();
			serverChannel = ServerSocketChannel.open();
			serverChannel.configureBlocking(false);
			serverChannel.socket().bind(new InetSocketAddress(port));
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void stop() {
		this.stop = true;
	}

	public void run() {
		while (!stop) {
			try {
				int num = selector.select(1000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				SelectionKey key = null;
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					try {
						handle(key);
					} catch (Exception e) {
						if (key != null) {
							key.cancel();
							if (key.channel() != null) {
								key.channel().close();
							}
						}
					}
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		// 关闭资源
		if (selector != null) {
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handle(SelectionKey key) throws IOException {
		if (key.isValid()) {
			// 处理接入请求
			if (key.isAcceptable()) {
				ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
				SocketChannel sc = serverChannel.accept();
				sc.configureBlocking(false);
				sc.register(selector, SelectionKey.OP_READ);
			}

			// 处理读取请求
			if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBytes];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("The time server receive order : " + body);
					String currentTime = "QUERY TIME ORDER".equals(body)
							? new Date(System.currentTimeMillis()).toString()
							: "BAD ORDER";
					doWrite(sc, currentTime);
				} else if (readBytes < 0) {
					// 关闭资源
					key.cancel();
					sc.close();
				} else {

				}
			}
		}
	}

	private void doWrite(SocketChannel sc, String response) throws IOException {
		if (response != null && !response.trim().isEmpty()) {
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			sc.write(writeBuffer);
		}
	}
}
