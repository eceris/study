package com.prodo.event;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class ReadablePayloadExchangeDecorator extends ServerWebExchangeDecorator {

    protected ReadablePayloadExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);
    }

    @Override
    public ServerHttpRequest getRequest() {
        return super.getRequest();
    }

    @Override
    public ServerHttpResponse getResponse() {
        return super.getResponse();
    }
}
