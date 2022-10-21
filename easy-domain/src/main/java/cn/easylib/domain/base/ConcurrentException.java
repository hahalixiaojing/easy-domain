package cn.easylib.domain.base;

/**
 * 并发异常
 *
 * @author lixiaojing10
 */
public class ConcurrentException extends RuntimeException {
    public ConcurrentException() {
    }

    public ConcurrentException(String message) {
        super(message);
    }
}
