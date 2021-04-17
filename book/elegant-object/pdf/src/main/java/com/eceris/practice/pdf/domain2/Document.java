package com.eceris.practice.pdf.domain2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

@AllArgsConstructor
public class Document {

    private String athnSrvcCd;
    @Getter(value = AccessLevel.PROTECTED)
    private Template template;

    public String getId() {
        return athnSrvcCd;
    }

    public String templateId() {
        return template.getId();
    }

    public SignedDocument sign(final Signature signature) {
        SignedDocument cloned = null;
        try {
            cloned = (SignedDocument) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return SignedDocument.of(cloned, signature);
    }

    public class Signature { }
}
