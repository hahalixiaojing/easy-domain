package easy.domain.rules;

public interface IRule<T> {
    /**
     * 验证是否满足规则
     *
     * @param model 要验证的对象
     * @return true 验证成功，false 验证失败
     */
    boolean isSatisfy(T model);


}