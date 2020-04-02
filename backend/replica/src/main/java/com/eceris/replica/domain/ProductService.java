package com.eceris.replica.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    @Transactional
    public List<Product> getMaster() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getSlave() {
        return repository.findAll();
    }
}
