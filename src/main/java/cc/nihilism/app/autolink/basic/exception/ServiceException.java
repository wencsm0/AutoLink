package cc.nihilism.app.autolink.basic.exception;

import lombok.Getter;

@Getter
public final class ServiceException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误详细信息
     */
    private String detail;

    public ServiceException() {
    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(Integer code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }
}
