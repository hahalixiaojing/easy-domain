package easy.domain.base;

import java.util.List;

/**
 * @author lixiaojing10
 * @date 2021/9/17 6:28 下午
 */
public interface IValueObjectTraceCollectionHandler<T> {
    void process(List<T> appendList,List<T> removedList);
}
