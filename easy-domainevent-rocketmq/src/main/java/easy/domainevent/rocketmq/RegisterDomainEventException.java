package easy.domainevent.rocketmq;

/**
 * @author lixiaojing10
 * @date 2021/3/16 8:21 下午
 */
public class RegisterDomainEventException extends RuntimeException {

    public RegisterDomainEventException(String message) {
        super(message);
    }

    public RegisterDomainEventException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
