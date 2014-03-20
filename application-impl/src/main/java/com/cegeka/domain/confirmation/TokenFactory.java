package com.cegeka.domain.confirmation;

import java.util.UUID;

public class TokenFactory {
    public static String generateToken(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
