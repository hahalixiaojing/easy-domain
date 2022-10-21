package cn.easylib.domain.base;

import java.util.List;

/**
 * @author lixiaojing10
 */
public interface IValueObjectTraceCollectionHandler<T> {
    void process(List<T> appendList,List<T> removedList);
}
