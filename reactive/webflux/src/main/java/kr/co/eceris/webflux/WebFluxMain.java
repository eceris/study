package kr.co.eceris.webflux;

import io.netty.channel.Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import reactor.ipc.netty.NettyPipeline;
import reactor.ipc.netty.http.server.HttpServer;

import java.util.Optional;
import java.util.function.Consumer;

@SpringBootApplication
public class WebFluxMain {

    public static void main(String[] args) throws Exception {
        System.setProperty("server.port", "8082");
        SpringApplication.run(WebFluxMain.class, args);
    }


    @Bean
    public ReactiveWebServerFactory httpServer() {
        Consumer<Channel> channelConfigurator = channel -> Optional
                .ofNullable(channel.pipeline().get(NettyPipeline.ReactiveBridge))
                .map(NettyPipeline.SendOptions.class::cast)
                .ifPresent(NettyPipeline.SendOptions::flushOnEach);

        return httpHandler -> {
            HttpServer httpServer = HttpServer.builder()
                    .options(serverOptionsBuilder -> serverOptionsBuilder
                            .afterChannelInit(channelConfigurator)
                            .port(8080))
                    .build();

            ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
            return new NettyWebServer(httpServer, handlerAdapter);
        };
    }
}
