package com.kakao.tech.springaibasic;

public class SqlGenerationException extends RuntimeException {
    public SqlGenerationException(String response) {
        super(response);
    }
}
