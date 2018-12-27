package com.cavie.timeserver.netty.tcpproblem;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
*
*
*	@author		created by Cavielee
*	@date		2018年12月26日 下午5:08:58
*/
public class NettyTimeServer {
	private static final int PORT = 8080;
	
	public static void main(String[] args) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<Channel>() {
	
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new NettyTimeServerHandler());
					}
				});
		
			// 同步启动服务器
            ChannelFuture f = b.bind(PORT).sync();
            System.out.println("Time server start");
            // 等待服务器监听Channel关闭
            f.channel().closeFuture().sync();
		} finally {
			// 缓慢关闭
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
