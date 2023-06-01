package cn.easylib.domain.event;


public class RegisterDomainEventException extends RuntimeException {

    public RegisterDomainEventException(String message) {
        super(message);
    }

    public RegisterDomainEventException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
