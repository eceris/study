package com.eceris.practice.pdf.domain1;

import java.util.List;

public class Template {

    private String id;

    private List<String> Fields;


    public Document toDocument() {
        return new Document();
    }
}
