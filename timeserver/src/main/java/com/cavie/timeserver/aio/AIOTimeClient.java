package com.cavie.timeserver.aio;

public class AIOTimeClient {
	public static void main(String[] args) {
		int port = 8080;
		AsyncTimeClient client = new AsyncTimeClient(port);
		new Thread(client).start();
	}
}
