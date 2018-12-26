package com.cavie.timeserver.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 发送获取时间请求并读取响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午6:33:38
 */
public class NettyTimeClentHandler extends ChannelDuplexHandler {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuf buf = Unpooled.buffer(req.length);
		buf.writeBytes(req);
		ctx.writeAndFlush(buf);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] resp = new byte[buf.readableBytes()];
		buf.readBytes(resp);
		String body = new String(resp, "UTF-8");
		System.out.println("Now is : " + body);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
