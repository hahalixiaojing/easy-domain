package cn.easylib.domain.visual;

public class VisualException extends RuntimeException{
    public VisualException(Throwable cause) {
        super(cause);
    }

    public VisualException(String message) {
        super(message);
    }
}
