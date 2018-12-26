package com.cavie.timeserver.nio;

public class NIOTimeClient {

	public static void main(String[] args) {
		int port = 8080;

		TimeClient timeClient = new TimeClient(port);

		new Thread(timeClient).start();
	}

}
