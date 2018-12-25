package com.cavie.timeServer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author created by Cavielee
 * @date 2018年12月25日 下午12:20:39
 */
public class TimeClient implements Runnable {
	private SocketChannel socketChannel = null;
	private Selector selector = null;
	private boolean stop;

	public TimeClient(int port) {
		try {
			selector = Selector.open();
			socketChannel = socketChannel.open();
			socketChannel.configureBlocking(false);
			doConnect(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	private void doConnect(int port) throws IOException {
		if (socketChannel.connect(new InetSocketAddress("127.0.0.1", port))) {
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		} else {
			// 三次握手还没建立成功，继续监听连接事件
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}

	private void doWrite(SocketChannel sc) throws IOException {
		byte[] bytes = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		sc.write(writeBuffer);
		if (!writeBuffer.hasRemaining()) {
			System.out.println("Send order 2 server succeed");
		}
	}

	public void run() {
		while (!stop) {
			try {
				selector.select(1000);
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
			} catch (IOException e) {
				e.printStackTrace();
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
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					socketChannel.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				} else {
					System.exit(1);
				}
			}
			if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				byte[] bytes = new byte[readBytes];
				if (readBytes > 0) {
					readBuffer.flip();
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("Now is :" + body);
					this.stop();
				} else if (readBytes < 0) {
					key.cancel();
					sc.close();
				}
			}
		}
	}

	private void stop() {
		this.stop = true;
	}
}
