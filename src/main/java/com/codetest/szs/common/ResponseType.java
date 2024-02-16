package com.codetest.szs.common;

public enum ResponseType {
    SUCCESS("200"),
    FAILURE("400");

    private String code;

    ResponseType(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public String getMessage(){
        return this.name();
    }
}
