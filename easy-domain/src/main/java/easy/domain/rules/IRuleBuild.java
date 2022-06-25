package easy.domain.rules;

/**
 * @author lixiaojing10
 */
public interface IRuleBuild {
    default void init(){}
    void reset();
}
