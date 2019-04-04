package kr.co.eceris.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebFluxMain {

    public static void main(String[] args) throws Exception {
        System.setProperty("server.port", "8082");
        System.setProperty("reactor.ipc.netty.workerCount", "1");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(WebFluxMain.class, args);
    }
}

//    @Bean
//    public ReactiveWebServerFactory httpServer() {
//        Consumer<Channel> channelConfigurator = channel -> Optional
//                .ofNullable(channel.pipeline().get(NettyPipeline.ReactiveBridge))
//                .map(NettyPipeline.SendOptions.class::cast)
//                .ifPresent(NettyPipeline.SendOptions::flushOnEach);
//
//        return httpHandler -> {
//            HttpServer httpServer = HttpServer.builder()
//                    .options(serverOptionsBuilder -> serverOptionsBuilder
//                            .afterChannelInit(channelConfigurator)
//                            .port(8080))
//                    .build();
//
//            ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
//            return new NettyWebServer(httpServer, handlerAdapter);
//        };
//    }
