package com.cavie.timeserver.netty.serialize.objectdecoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 *
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午5:08:12
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
		             // 线程安全的 WeakReferenceMap 对类加载器进行缓存
		             // 弱引用可以在内存不足时自动释放，以防内存泄露
		             p.addLast(new ObjectDecoder(1024, 
		            		 ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
		             p.addLast(new ObjectEncoder());
		             p.addLast(new NettyTimeClentHandler());
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
