package com.cavie.timeserver.netty.serialize.protobuf;

import java.util.Date;

import com.cavie.timeserver.netty.serialize.protobuf.TimeResponseProto.TimeResponse.Builder;
import com.cavie.timeserver.netty.serialize.protobuf.UserRequestProto.UserRequest;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 读取获取时间请求并写回响应。
 *
 * @author created by Cavielee
 * @date 2018年12月26日 下午5:56:34
 */
public class NettyTimeServerHandler extends ChannelDuplexHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		UserRequest req = (UserRequestProto.UserRequest) msg;
		System.out.println(req.toString());
		Builder builder = TimeResponseProto.TimeResponse.newBuilder();
		if ("QUERY TIME ORDER".equalsIgnoreCase(req.getOrder())) {
			builder.setRespCode(0);
			builder.setTime(new Date(System.currentTimeMillis()).toString());
		} else {
			builder.setRespCode(1);
			builder.setErrMsg("Is not time query");
		}
		ctx.writeAndFlush(builder.build());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
