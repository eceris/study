package com.eceris.practice.pdf.domain2;

import lombok.Getter;

import java.util.List;

public class Template {

    @Getter
    private String id;

    private List<String> Fields;


    public Document toDocument(String athnSrvcCd) {
        return new Document(athnSrvcCd, this);
    }
}
