package com.eceris.replica.adapter.controller;

import com.eceris.replica.domain.Product;
import com.eceris.replica.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/master")
    public List<Product> getMaster() {
        return service.getMaster();
    }



    @GetMapping("/slave")
    public List<Product> slave() {
        return service.getSlave();
    }

}
