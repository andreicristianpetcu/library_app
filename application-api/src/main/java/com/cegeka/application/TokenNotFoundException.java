package com.cegeka.application;

public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(String message){
        super(message);
    }
}
