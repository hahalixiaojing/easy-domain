package cn.easylib.domain.rules;

/**
 * @author lixiaojing10
 */
public interface IRuleBuild {
    default void init(){}
    void reset();
}
