package easy.domainevent.rocketmq;

/**
 * @author lixiaojing
 * @date 2021/3/16 9:52 下午
 */
public class PublishEventException extends RuntimeException {

    public PublishEventException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
