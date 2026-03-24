package kr.magicbox.user.adapter.out.communication.grpc;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.NegotiationType;
import kr.magicbox.user.adapter.out.communication.grpc.exception.GrpcServiceConfigurationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class GrpcChannelFactory {
    private final GrpcClientConfigurationProperties grpcProperties;
    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();

    public ManagedChannel getChannel(String serviceName) {
        return channels.computeIfAbsent(serviceName, this::createChannel);
    }

    private ManagedChannel createChannel(String serviceName) {
        GrpcClientConfigurationProperties.ServiceConfig config = grpcProperties.getClient().get(serviceName);

        if (config == null) {
            throw new GrpcServiceConfigurationNotFoundException(serviceName);
        }

        NegotiationType negotiationType = NegotiationType.valueOf(config.negotiationType().toUpperCase());

        NettyChannelBuilder builder = NettyChannelBuilder.forTarget(config.address())
                .negotiationType(negotiationType);

        applyKeepAlive(builder, config.keepAlive());

        return builder.build();
    }

    private void applyKeepAlive(NettyChannelBuilder builder, GrpcClientConfigurationProperties.KeepAlive keepAlive) {
        if (keepAlive == null) {
            return;
        }
        if (keepAlive.time() != null) {
            builder.keepAliveTime(keepAlive.time().toMillis(), TimeUnit.MILLISECONDS);
        }
        if (keepAlive.timeout() != null) {
            builder.keepAliveTimeout(keepAlive.timeout().toMillis(), TimeUnit.MILLISECONDS);
        }
    }
}