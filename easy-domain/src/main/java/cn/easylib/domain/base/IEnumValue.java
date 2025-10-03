package cn.easylib.domain.base;

public interface IEnumValue<T,K extends Enum<?>> {

    T getValue();
    String getName();
   K parse(T t);
}
