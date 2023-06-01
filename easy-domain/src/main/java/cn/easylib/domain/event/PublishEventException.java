package cn.easylib.domain.event;


public class PublishEventException extends RuntimeException {

    public PublishEventException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
