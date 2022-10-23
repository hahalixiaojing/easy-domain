package cn.easylib.domainevent.rocketmq;


public class PublishEventException extends RuntimeException {

    public PublishEventException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
