package cn.easylib.domain.rules;

public interface IParamRule<T> {

    Pair isSatisfy(T model);

}
