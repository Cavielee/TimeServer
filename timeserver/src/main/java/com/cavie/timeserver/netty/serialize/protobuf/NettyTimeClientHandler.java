package com.cavie.timeserver.netty.serialize.protobuf;

import com.cavie.timeserver.netty.serialize.protobuf.TimeResponseProto.TimeResponse;
import com.cavie.timeserver.netty.serialize.protobuf.UserRequestProto.UserRequest.Builder;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 发送获取时间请求并读取响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午6:33:38
 */
public class NettyTimeClientHandler extends ChannelDuplexHandler {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Builder builder = UserRequestProto.UserRequest.newBuilder();
		builder.setOrder("QUERY TIME ORDER");
		builder.setUsername("CavieLee");
		for (int i = 1; i <= 20; i++) {
			builder.setUid(i);
			ctx.writeAndFlush(builder.build());
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		TimeResponse resp = (TimeResponse) msg;
		if (0 == resp.getRespCode()) {
			System.out.println("Now is : " + resp.getTime());
		} else {
			System.out.println(resp.getErrMsg());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
