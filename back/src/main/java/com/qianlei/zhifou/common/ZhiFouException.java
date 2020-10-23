package com.qianlei.zhifou.common;

/** @author qianlei */
public class ZhiFouException extends RuntimeException {
    public ZhiFouException() {
        super();
    }

    public ZhiFouException(String message) {
        super(message);
    }

    public ZhiFouException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZhiFouException(Throwable cause) {
        super(cause);
    }

    protected ZhiFouException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
