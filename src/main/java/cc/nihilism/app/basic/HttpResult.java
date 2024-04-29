package cc.nihilism.app.basic;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class HttpResult {
    private int code;
    private int serviceCode;
    private String message;
    private Object data;

    public HttpResult(int code, String message, Object data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static HttpResult success() {
        return new HttpResult(HttpStatus.OK.value(), "success", null);
    }

    public static HttpResult error() {
        return new HttpResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error", null);
    }


    public static HttpResult success(String message) {
        return new HttpResult(HttpStatus.OK.value(), message, null);
    }

    public static HttpResult error(String message) {
        return new HttpResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }


    public static HttpResult data(Object data) {
        return new HttpResult(HttpStatus.OK.value(), "success", data);
    }
}
