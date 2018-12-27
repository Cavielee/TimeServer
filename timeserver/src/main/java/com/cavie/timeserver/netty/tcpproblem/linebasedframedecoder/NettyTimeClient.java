package com.cavie.timeserver.netty.tcpproblem.linebasedframedecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
*
*
*	@author		created by Cavielee
*	@date		2018年12月26日 下午5:08:12
*/
public class NettyTimeClient {
	private static final int PORT = 8080;
	private static final String HOST = "127.0.0.1";
	
	public static void main(String[] args) throws Exception {
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
		    Bootstrap b = new Bootstrap();
		    b.group(group)
		     .channel(NioSocketChannel.class)
		     .option(ChannelOption.TCP_NODELAY, true)
		     .handler(new ChannelInitializer<SocketChannel>() {
		         @Override
		         public void initChannel(SocketChannel ch) throws Exception {
		             ChannelPipeline p = ch.pipeline();
		             p.addLast(new LineBasedFrameDecoder(1024));
		             p.addLast(new StringDecoder());
		             p.addLast(new NettyTimeClientHandler());
		         }
		     });

		    // 启动客户端
		    ChannelFuture f = b.connect(HOST, PORT).sync();

		    // 等待连接关闭
		    f.channel().closeFuture().sync();
		} finally {
			// 缓慢关闭
		    group.shutdownGracefully();
		}
	}

}
