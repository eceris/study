package com.eceris.practice.pdf.domain2;

public class SignedDocument extends Document {

    private Signature signature;

    private SignedDocument(Document document, Signature signature) {
        super(document.getId(), document.getTemplate());
        this.signature = signature;
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String templateId() {
        return super.templateId();
    }

    public static SignedDocument of(Document document, Signature signature) {
        return new SignedDocument(document, signature);
    }



}
