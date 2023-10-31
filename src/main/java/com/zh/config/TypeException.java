package com.zh.config;

import java.io.Serial;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-18
 */
public class TypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2732419195634968950L;

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

}
